/*
 * Created by Daniel Marell 2011-09-17 16:22
 */
package se.marell.dvesta.iodevices.k8055.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.marell.dcommons.time.TimeSource;
import se.marell.dvesta.iodevices.AbstractIoController;
import se.marell.dvesta.iodevices.k8055.config.*;
import se.marell.dvesta.ioscan.*;
import se.marell.dvesta.tickengine.AbstractTickConsumer;
import se.marell.dvesta.tickengine.TickConsumer;
import se.marell.dvesta.tickengine.TickEngine;
import se.marell.iodevices.velleman.SynchronousK8055;
import se.marell.libusb.LibUsbSystem;
import se.marell.libusb.UsbSystem;

import javax.annotation.PreDestroy;
import java.util.*;

@Service
public class K8055IoController extends AbstractIoController implements K8055IoConfigurationService {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String MODULE_NAME = "K8055IoController";
    private static final int POLL_INDICATOR_PORT = 8;
    private boolean pollIndicatorState;
    private List<SynchronousK8055> devices = new ArrayList<SynchronousK8055>();
    private SynchronousK8055 lastPolledDevice;
    private int nextDeviceIndex;

    @Autowired
    private TimeSource timeSource;
    @Autowired
    private TickEngine tickEngine;
    @Autowired
    private IoMapper ioMapper;

    private Map<Integer/*K8055 address*/, List<BitInput>> bitInputMap = new HashMap<Integer, List<BitInput>>();
    private Map<Integer/*K8055 address*/, List<BitOutput>> bitOutputMap = new HashMap<Integer, List<BitOutput>>();
    private Map<Integer/*K8055 address*/, List<IntegerInput>> analogInputMap = new HashMap<Integer, List<IntegerInput>>();
    private Map<Integer/*K8055 address*/, List<IntegerOutput>> analogOutputMap = new HashMap<Integer, List<IntegerOutput>>();
    private UsbSystem usbSystem;

    private TickConsumer preTickConsumer;
    private TickConsumer postTickConsumer;

    @PreDestroy
    private void deactivate() {
        tickEngine.removeTickConsumer(preTickConsumer);
        tickEngine.removeTickConsumer(postTickConsumer);
        log.info("deactivated " + MODULE_NAME);
    }

    private void activate() {
        if (preTickConsumer != null) {
            return; // Already activated
        }
        usbSystem = new LibUsbSystem(false, 0);
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

    @Override
    public void setConfiguration(List<K8055Configuration> configs) {
        log.info("Starting Adu208IoController using " + configs.size() + " devices.");

        try {
            activate();
        } catch (UnsatisfiedLinkError e) {
            log.info("K8055IoController activate failed: " + e.getMessage());
            return;
        }

        int tickFrequency = tickEngine.findFrequency(1, 150, 20);
        if (tickFrequency == 0) {
            throw new RuntimeException("Failed to find a suitable tick frequency");
        }

        // K8055 subsystem initialization

        // Find out the set of K8055 addresses we are expected to have on the serial port
        for (K8055Configuration config : configs) {
            SynchronousK8055 device = new SynchronousK8055(usbSystem, config.getDeviceNumber());
            mapDevice(config, device);
            devices.add(device);

            // Build up map for inputs and outputs for fast lookup during execution

            for (K8055Configuration.BitIO io : config.getInputs()) {
                if (io.getDeviceNumber() == config.getDeviceNumber()) {
                    BitInput din = findIoDevice(new DigitalInputK8055Address(io.getDeviceNumber(), io.getBitno()), ioMapper.getBitInputs());
                    if (din != null) {
                        List<BitInput> c = bitInputMap.get(io.getDeviceNumber());
                        if (c == null) {
                            final int length = 5; // Number of digital inputs on a K8055
                            c = new ArrayList<BitInput>(length);
                            for (int i = 0; i < length; ++i) {
                                c.add(null);
                            }
                            bitInputMap.put(io.getDeviceNumber(), c);
                        }
                        c.set(io.getBitno(), din);
                    }
                }
            }

            for (K8055Configuration.BitIO io : config.getOutputs()) {
                if (io.getDeviceNumber() == config.getDeviceNumber()) {
                    BitOutput dout = findIoDevice(new DigitalOutputK8055Address(io.getDeviceNumber(), io.getBitno()), ioMapper.getBitOutputs());
                    if (dout != null) {
                        List<BitOutput> c = bitOutputMap.get(io.getDeviceNumber());
                        if (c == null) {
                            int length = 8; // Number of digital outputs on a K8055
                            c = new ArrayList<BitOutput>(length);
                            for (int i = 0; i < length; ++i) {
                                c.add(null);
                            }
                            bitOutputMap.put(io.getDeviceNumber(), c);
                        }
                        c.set(io.getBitno(), dout);
                    }
                }
            }

            for (K8055Configuration.AnalogIO io : config.getAnalogInputs()) {
                if (io.getDeviceNumber() == config.getDeviceNumber()) {
                    IntegerInput ain = findIoDevice(new AnalogInputK8055Address(io.getDeviceNumber(), io.getPortno()), ioMapper.getIntegerInputs());
                    if (ain != null) {
                        List<IntegerInput> c = analogInputMap.get(io.getDeviceNumber());
                        if (c == null) {
                            final int length = 2; // Number of analog inputs on a K8055
                            c = new ArrayList<IntegerInput>(length);
                            for (int i = 0; i < length; ++i) {
                                c.add(null);
                            }
                            analogInputMap.put(io.getDeviceNumber(), c);
                        }
                        c.set(io.getPortno(), ain);
                    }
                }
            }

            for (K8055Configuration.AnalogIO io : config.getAnalogOutputs()) {
                if (io.getDeviceNumber() == config.getDeviceNumber()) {
                    IntegerOutput aout = findIoDevice(new AnalogOutputK8055Address(io.getDeviceNumber(), io.getPortno()), ioMapper.getIntegerOutputs());
                    if (aout != null) {
                        List<IntegerOutput> c = analogOutputMap.get(io.getDeviceNumber());
                        if (c == null) {
                            final int length = 2; // Number of analog outputs on a K8055
                            c = new ArrayList<IntegerOutput>(length);
                            for (int i = 0; i < length; ++i) {
                                c.add(null);
                            }
                            analogOutputMap.put(io.getDeviceNumber(), c);
                        }
                        c.set(io.getPortno(), aout);
                    }
                }
            }

        }

        tickEngine.addPreTickConsumer(tickFrequency, preTickConsumer);
        tickEngine.addPostTickConsumer(tickFrequency, postTickConsumer);

        log.info("Started K8055IoController");
    }

    private void preTick() {
        //log.info("preTick");
        if (lastPolledDevice != null) {

            long timestamp = 0;//todo lastPolledDevice.getTimestampLatestContact();

            // Copy K8055 digital inputs to mapped logical inputs
            List<BitInput> bitInputs = bitInputMap.get(lastPolledDevice.getDeviceNumber());
            if (bitInputs != null) {
                // There are mapped logical inputs using this K8055 device
                for (int port = 0; port < bitInputs.size(); ++port) {
                    BitInput din = bitInputs.get(port);
                    if (din != null) {
                        din.setInputStatus(timestamp, lastPolledDevice.getDi(port));
                    }
                }
            }

            List<IntegerInput> analogInputs = analogInputMap.get(lastPolledDevice.getDeviceNumber());
            if (analogInputs != null) {
                // There are mapped logical inputs using this K8055 device
                for (int port = 0; port < analogInputs.size(); ++port) {
                    IntegerInput ain = analogInputs.get(port);
                    if (ain != null) {
                        int value = 0;
                        if (port == 0) {
                            value = lastPolledDevice.getAi1();
                        } else if (port == 1) {
                            value = lastPolledDevice.getAi1();
                        }
                        ain.setStatus(timestamp, true, value);
                    }
                }
            }

            //log.info("di1=" + lastPolledDevice.getDi(1));//todo test remove
            lastPolledDevice.setDo(1, lastPolledDevice.getDi(1));

        }
        pollNextDevice();
    }

    private void pollNextDevice() {
        SynchronousK8055 k8055 = getNextDeviceToPoll();
        if (k8055 != null) {
            boolean rc = k8055.poll(); // Todo: synchronous call causes CPU to wait here for answer. Change to async calls
            //Todo handle communication failure
            //log.info("rc=" + rc);//todo test remove

            lastPolledDevice = k8055;
        }
    }

    private void postTick() {
        //log.info("postTick");//todo test remove

        if (lastPolledDevice != null) {

            // Copy touched mapped logical outputs to K8055 digital outputs
            List<BitOutput> bitOutputs = bitOutputMap.get(lastPolledDevice.getDeviceNumber());
            if (bitOutputs != null) {
                // There are mapped logical inputs using this K8055 address. Set touched logical outputs in K8055
                for (int port = 0; port < bitOutputs.size(); ++port) {
                    BitOutput dout = bitOutputs.get(port);
                    if (dout != null && dout.isTouched()) {
                        lastPolledDevice.setDo(port, dout.getInputStatus());
                        dout.setIoStatus(timeSource.currentTimeMillis(), true);
                        dout.clearTouched();
                    }
                }
            }

            List<IntegerOutput> analogOutputs = analogOutputMap.get(lastPolledDevice.getDeviceNumber());
            if (analogOutputs != null) {
                // There are mapped logical inputs using this K8055 address. Set touched logical outputs in K8055
                for (int port = 0; port < analogOutputs.size(); ++port) {
                    IntegerOutput aout = analogOutputs.get(port);
                    if (aout != null && aout.isTouched()) {
                        int value = aout.getValue();
                        if (port == 0) {
                            lastPolledDevice.setAo1(value);
                            aout.setIoStatus(timeSource.currentTimeMillis(), true);
                            log.trace("setAo1 " + value);
                        } else if (port == 1) {
                            lastPolledDevice.setAo2(value);
                            log.trace("setAo2 " + value);
                            aout.setIoStatus(timeSource.currentTimeMillis(), true);
                        }
                        aout.clearTouched();
                    }
                }
            }

            // Flip highest output bit = poll indicator. K8055 seems to use this itself as an indicator output too at startup
            lastPolledDevice.setDo(POLL_INDICATOR_PORT, pollIndicatorState = !pollIndicatorState);
        }
    }

    private SynchronousK8055 getNextDeviceToPoll() {
        if (devices.isEmpty()) {
            return null;
        }
        SynchronousK8055 device = devices.get(nextDeviceIndex);
        if (++nextDeviceIndex >= devices.size()) {
            nextDeviceIndex = 0;
        }
        return device;
    }

    private void mapDevice(K8055Configuration config, SynchronousK8055 device) {
        for (K8055Configuration.BitIO io : config.getInputs()) {
            DeviceAddress address = new DigitalInputK8055Address(device.getDeviceNumber(), io.getBitno());
            ioMapper.mapBitInput(io.getLogicalName(), address.toString(), io.getDescription());
            log.trace("Mapped Di " + io.getLogicalName() + " to address " + address.toString());
        }
        for (K8055Configuration.BitIO io : config.getOutputs()) {
            DeviceAddress address = new DigitalOutputK8055Address(device.getDeviceNumber(), io.getBitno());
            ioMapper.mapBitOutput(io.getLogicalName(), address.toString(), io.getDescription());
            log.trace("Mapped Do " + io.getLogicalName() + " to address " + address.toString());
        }
        for (K8055Configuration.AnalogIO io : config.getAnalogInputs()) {
            DeviceAddress address = new AnalogInputK8055Address(device.getDeviceNumber(), io.getPortno());
            try {
                ioMapper.mapIntegerInput(io.getLogicalName(), address.toString(), io.getDescription(), "", 0, 255); // todo
            } catch (IoMappingException e) {
                log.error("Failed to map integerInput " + io.getLogicalName() + ":" + e.getMessage());
            }
            log.trace("Mapped Ai " + io.getLogicalName() + " to address " + address.toString());
        }
        for (K8055Configuration.AnalogIO io : config.getAnalogOutputs()) {
            DeviceAddress address = new AnalogOutputK8055Address(device.getDeviceNumber(), io.getPortno());
            try {
                ioMapper.mapIntegerOutput(io.getLogicalName(), address.toString(), io.getDescription(), "", 0, 255); // todo
            } catch (IoMappingException e) {
                log.error("Failed to map integerOutput " + io.getLogicalName() + ":" + e.getMessage());
            }
            log.trace("Mapped Ao " + io.getLogicalName() + " to address " + address.toString());
        }
    }
}

