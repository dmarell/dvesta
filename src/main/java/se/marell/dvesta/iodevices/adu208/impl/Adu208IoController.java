/*
 * Created by Daniel Marell 2011-09-11 12:35
 */
package se.marell.dvesta.iodevices.adu208.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.marell.dvesta.iodevices.AbstractIoController;
import se.marell.dvesta.iodevices.adu208.config.Adu208Configuration;
import se.marell.dvesta.iodevices.adu208.config.Adu208IoConfigurationService;
import se.marell.dvesta.iodevices.adu208.config.DigitalInputAdu208Address;
import se.marell.dvesta.iodevices.adu208.config.DigitalOutputAdu208Address;
import se.marell.dvesta.ioscan.BitInput;
import se.marell.dvesta.ioscan.BitOutput;
import se.marell.dvesta.ioscan.DeviceAddress;
import se.marell.dvesta.ioscan.IoMapper;
import se.marell.dvesta.tickengine.AbstractTickConsumer;
import se.marell.dvesta.tickengine.TickConsumer;
import se.marell.dvesta.tickengine.TickEngine;
import se.marell.iodevices.ontrak.Adu208Adutux;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Adu208IoController extends AbstractIoController implements Adu208IoConfigurationService {
    private static class InvertibleBitInput {
        private boolean invert;
        private BitInput bitInput;

        public void set(boolean invert, BitInput din) {
            this.invert = invert;
            bitInput = din;
        }

        public boolean isInvert() {
            return invert;
        }

        public BitInput getBitInput() {
            return bitInput;
        }
    }

    private static class InvertibleBitOutput {
        private boolean invert;
        private BitOutput bitOutput;

        public void set(boolean invert, BitOutput dout) {
            this.invert = invert;
            bitOutput = dout;
        }

        public boolean isInvert() {
            return invert;
        }

        public BitOutput getBitOutput() {
            return bitOutput;
        }
    }

    private class DeviceIO {
        private InvertibleBitInput[] bitInputs;
        private InvertibleBitOutput[] bitOutputs;

        private DeviceIO() {
            bitInputs = new InvertibleBitInput[8];
            bitOutputs = new InvertibleBitOutput[8];
        }

        public InvertibleBitInput[] getBitInputs() {
            return bitInputs;
        }

        public void setBitInput(int bitno, boolean invert, BitInput din) {
            InvertibleBitInput iin = bitInputs[bitno];
            if (iin == null) {
                iin = bitInputs[bitno] = new InvertibleBitInput();
            }
            iin.set(invert, din);
        }

        public InvertibleBitOutput[] getBitOutputs() {
            return bitOutputs;
        }

        public void setBitOutput(int bitno, boolean invert, BitOutput dout) {
            InvertibleBitOutput iout = bitOutputs[bitno];
            if (iout == null) {
                iout = bitOutputs[bitno] = new InvertibleBitOutput();
            }
            iout.set(invert, dout);
        }
    }

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String MODULE_NAME = "Adu208IoController";
    private List<Adu208Adutux> devices = new ArrayList<Adu208Adutux>();
    private Adu208Adutux lastPolledDevice;
    private int nextDeviceIndex;
    private int[] digitalOutputValues;

    @Autowired
    private IoMapper ioMapper;
    @Autowired
    private TickEngine tickEngine;
    private Map<Integer/*Adu208 deviceNumber*/, DeviceIO> deviceMap = new HashMap<Integer, DeviceIO>();

    private TickConsumer preTickConsumer;
    private TickConsumer postTickConsumer;

    private void activate() {
        if (preTickConsumer != null) {
            return; // Already activated
        }
        preTickConsumer = new AbstractTickConsumer(MODULE_NAME + "-pre") {
            @Override
            public void executeTick() {
                preTick();
            }
        };
        postTickConsumer = new AbstractTickConsumer(MODULE_NAME + "-post") {
            @Override
            public void executeTick() {
                postTick();
            }
        };
        log.info("activated " + MODULE_NAME);
    }

    @PreDestroy
    private void deactivate() {
        tickEngine.removeTickConsumer(preTickConsumer);
        tickEngine.removeTickConsumer(postTickConsumer);
        for (Adu208Adutux d : devices) {
            d.stop();
        }
        log.info("deactivated " + MODULE_NAME);
    }

    @Override
    public void setConfiguration(List<Adu208Configuration> configs) {
        log.info("Starting " + MODULE_NAME + " using " + configs.size() + " devices.");

        activate();

        int n = tickEngine.removeTickConsumer(preTickConsumer);
        if (n > 0) {
            log.error("removeTickConsumer preTickConsumer=" + n);
        }
        n = tickEngine.removeTickConsumer(postTickConsumer);
        if (n > 0) {
            log.error("removeTickConsumer postTickConsumer=" + n);
        }

        digitalOutputValues = new int[configs.size()];

        int tickFrequency = tickEngine.findFrequency(1, 150, 20);
        if (tickFrequency == 0) {
            throw new RuntimeException("Failed to find a suitable tick frequency");
        }

        // ADU208 subsystem initialization

        // Find out the expected set of ADU208 devices
        log.trace("Configured deviceNumbers={}", configs);
        for (Adu208Configuration config : configs) {
            log.trace("Processing config={}", config.toString());
            Adu208Adutux device = new Adu208Adutux(config.getDeviceNumber());
            mapDevice(config, device);
            devices.add(device);
            device.requestGetDigitalInputs(); // Initial request
            log.info("Added ADU 208 with deviceNumber={}", config.getDeviceNumber());

            // Build up maps for fast lookup during execution

            log.trace("Processing inputs:{}", config.getInputs().size());
            for (Adu208Configuration.BitIO io : config.getInputs()) {
                if (io.getDeviceNumber() == config.getDeviceNumber()) {
                    BitInput din = findIoDevice(new DigitalInputAdu208Address(io.getDeviceNumber(), io.getBitno()), ioMapper.getBitInputs());
                    if (din != null) {
                        log.trace("Adding Di " + io.getBitno() + " to deviceMap");
                        DeviceIO dio = deviceMap.get(io.getDeviceNumber());
                        if (dio == null) {
                            dio = new DeviceIO();
                            deviceMap.put(io.getDeviceNumber(), dio);
                        }
                        dio.setBitInput(io.getBitno(), io.isInvert(), din);
                        log.trace("Added Di " + io.getBitno() + " to deviceMap");
                    }
                }
            }

            log.trace("Processing outputs:{}", config.getOutputs().size());
            for (Adu208Configuration.BitIO io : config.getOutputs()) {
                BitOutput dout = findIoDevice(new DigitalOutputAdu208Address(io.getDeviceNumber(), io.getBitno()), ioMapper.getBitOutputs());
                if (dout != null) {
                    DeviceIO dio = deviceMap.get(io.getDeviceNumber());
                    if (dio == null) {
                        dio = new DeviceIO();
                        deviceMap.put(io.getDeviceNumber(), dio);
                    }
                    dio.setBitOutput(io.getBitno(), io.isInvert(), dout);
                    log.trace("Added Do " + io.getBitno() + " to deviceMap");
                }
            }
        }

        tickEngine.addPreTickConsumer(tickFrequency, preTickConsumer);
        tickEngine.addPostTickConsumer(tickFrequency, postTickConsumer);

        log.info("Started " + MODULE_NAME);
    }

    private void preTick() {
        if (lastPolledDevice != null) {
            Integer diValue = lastPolledDevice.getDigitalInputs();
            if (diValue != null) { // Check if inputs are available
                // Copy ADU208 digital inputs to mapped logical inputs
                long timestamp = lastPolledDevice.getTimestampLatestContact();
                DeviceIO dio = deviceMap.get(lastPolledDevice.getDeviceNumber());
                if (dio != null) {
                    // There are mapped logical inputs using this ADU208 device
                    InvertibleBitInput[] inputs = dio.getBitInputs();
                    for (int bitno = 0; bitno < inputs.length; ++bitno) {
                        InvertibleBitInput din = inputs[bitno];
                        if (din != null) {
                            boolean status = (diValue & (1 << bitno)) != 0;
                            din.getBitInput().setInputStatus(timestamp, status ^ din.isInvert());
                        }
                    }
                }
                lastPolledDevice.requestGetDigitalInputs();
            }
        }
        pollNextDevice();
    }

    private void pollNextDevice() {
        Adu208Adutux device = getNextDeviceToPoll();
        if (device != null) {
            lastPolledDevice = device;
        }
    }

    private void postTick() {
        if (lastPolledDevice != null) {
            // Copy touched mapped logical outputs to ADU208 digital outputs
            DeviceIO dio = deviceMap.get(lastPolledDevice.getDeviceNumber());
            if (dio != null) {
                InvertibleBitOutput[] outputs = dio.getBitOutputs();
                for (int bitno = 0; bitno < outputs.length; ++bitno) {
                    InvertibleBitOutput dout = outputs[bitno];
                    if (dout != null) {
                        BitOutput bitOut = dout.getBitOutput();
                        if (dout.getBitOutput().isTouched()) {
                            boolean status = dout.getBitOutput().getInputStatus() ^ dout.isInvert();
                            if (status) {
                                digitalOutputValues[nextDeviceIndex] |= (1 << bitno);
                            } else {
                                digitalOutputValues[nextDeviceIndex] &= ~(1 << bitno);
                            }
                            bitOut.clearTouched();
                        }
                    }
                }
                lastPolledDevice.requestSetDigitalOutputs(digitalOutputValues[nextDeviceIndex]);
            }
        }
    }

    private Adu208Adutux getNextDeviceToPoll() {
        if (devices.isEmpty()) {
            return null;
        }
        Adu208Adutux device = devices.get(nextDeviceIndex);
        if (++nextDeviceIndex >= devices.size()) {
            nextDeviceIndex = 0;
        }
        return device;
    }

    private void mapDevice(Adu208Configuration config, Adu208Adutux device) {
        for (Adu208Configuration.BitIO io : config.getInputs()) {
            DeviceAddress address = new DigitalInputAdu208Address(device.getDeviceNumber(), io.getBitno());
            ioMapper.mapBitInput(io.getLogicalName(), address.toString(), io.getDescription());
            log.info("Mapped Di " + io.getLogicalName() + " to address " + address.toString());
        }
        for (Adu208Configuration.BitIO io : config.getOutputs()) {
            DeviceAddress address = new DigitalOutputAdu208Address(device.getDeviceNumber(), io.getBitno());
            ioMapper.mapBitOutput(io.getLogicalName(), address.toString(), io.getDescription());
            log.info("Mapped Do " + io.getLogicalName() + " to address " + address.toString());
        }
    }
}
