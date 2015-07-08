/*
 * Created by Daniel Marell 2011-09-17 16:22
 */
package se.marell.dvesta.iodevices.razberry.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import se.marell.dvesta.iodevices.AbstractIoController;
import se.marell.dvesta.iodevices.razberry.config.RazberryConfiguration;
import se.marell.dvesta.iodevices.razberry.config.RazberryDeviceAddress;
import se.marell.dvesta.iodevices.razberry.config.RazberryIoConfigurationService;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.data.ZWayAlarm;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.data.ZWayDataReply;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation.ZAutomationDevice;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation.ZAutomationDevicesReply;
import se.marell.dvesta.ioscan.*;
import se.marell.dvesta.tickengine.AbstractTickConsumer;
import se.marell.dvesta.tickengine.TickConsumer;
import se.marell.dvesta.tickengine.TickEngine;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class RazberryIoController extends AbstractIoController implements RazberryIoConfigurationService {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final class RazberrySlot {
        public String razberryUri;

        public Future<ResponseEntity<ZAutomationDevicesReply>> devicesResponse;
        public long updateTimeLastDevicesReply;

        public Future<ResponseEntity<ZWayDataReply>> dataResponse;
        public long updateTimeLastDataReply;

        public RazberrySlot(String razberryUri) {
            this.razberryUri = razberryUri;
        }
    }

    @Autowired
    private RazberryClient razberryClient;

    private static final String MODULE_NAME = "RazberryIoController";
    private List<RazberrySlot> razberrySlots = new ArrayList<>();

    @Autowired
    private TickEngine tickEngine;
    @Autowired
    private IoMapper ioMapper;

    private Map<String/*url*/, List<BitInput>> bitInputMap = new HashMap<>();
    private Map<String/*url*/, List<BitOutput>> bitOutputMap = new HashMap<>();
    private Map<String/*url*/, List<FloatInput>> floatInputMap = new HashMap<>();
    private Map<String/*url*/, List<FloatOutput>> floatOutputMap = new HashMap<>();
    private Map<String/*url*/, List<AlarmInput>> alarmInputMap = new HashMap<>();

    private Map<String/*Logical name*/, RazberryDeviceAddress> deviceAddressMap = new HashMap<>();

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
        int tickFrequency = tickEngine.findFrequency(1, 20, 20);
        if (tickFrequency == 0) {
            throw new RuntimeException("Failed to find a suitable tick frequency");
        }
        tickEngine.addPreTickConsumer(tickFrequency, preTickConsumer);
        tickEngine.addPostTickConsumer(tickFrequency, postTickConsumer);
        log.info("activated " + MODULE_NAME + ",frequency: " + tickFrequency + " Hz");
    }

    @PreDestroy
    private void deactivate() {
        tickEngine.removeTickConsumer(preTickConsumer);
        tickEngine.removeTickConsumer(postTickConsumer);
        log.info("deactivated " + MODULE_NAME);
    }

    @Override
    public void setConfiguration(List<RazberryConfiguration> configs) {
        log.info("Configure RazberryIoController using " + configs.size() + " devices.");

        // Find out the set of Razberry addresses we are expected to have
        for (RazberryConfiguration config : configs) {
            mapDevice(config, config.getControllerUrl());
            razberrySlots.add(new RazberrySlot(config.getControllerUrl()));

            // Build up map for inputs and outputs for fast lookup during execution
            configureBitInputs(config);
            configureBitOutputs(config);
            configureFloatInputs(config);
            configureFloatOutputs(config);
            configureAlarmInputs(config);
        }
        log.info("Configured RazberryIoController");

        activate();
    }

    private void configureBitInputs(RazberryConfiguration config) {
        for (RazberryConfiguration.BitIO io : config.getInputs()) {
            if (io.getControllerUrl().equals(config.getControllerUrl())) {
                RazberryDeviceAddress deviceAddress = new RazberryDeviceAddress(io.getControllerUrl(), io.getDeviceId());
                deviceAddressMap.put(io.getLogicalName(), deviceAddress);
                BitInput din = findIoDevice(deviceAddress, ioMapper.getBitInputs());
                if (din != null) {
                    List<BitInput> c = bitInputMap.get(io.getControllerUrl());
                    if (c == null) {
                        c = new ArrayList<>();
                        bitInputMap.put(io.getControllerUrl(), c);
                    }
                    c.add(din);
                }
            }
        }
    }

    private void configureBitOutputs(RazberryConfiguration config) {
        for (RazberryConfiguration.BitIO io : config.getOutputs()) {
            if (io.getControllerUrl().equals(config.getControllerUrl())) {
                RazberryDeviceAddress deviceAddress = new RazberryDeviceAddress(io.getControllerUrl(), io.getDeviceId());
                deviceAddressMap.put(io.getLogicalName(), deviceAddress);
                BitOutput dout = findIoDevice(deviceAddress, ioMapper.getBitOutputs());
                if (dout != null) {
                    List<BitOutput> c = bitOutputMap.get(io.getControllerUrl());
                    if (c == null) {
                        c = new ArrayList<>();
                        bitOutputMap.put(io.getControllerUrl(), c);
                    }
                    c.add(dout);
                }
            }
        }
    }

    private void configureFloatInputs(RazberryConfiguration config) {
        for (RazberryConfiguration.AnalogIO io : config.getAnalogInputs()) {
            if (io.getControllerUrl().equals(config.getControllerUrl())) {
                RazberryDeviceAddress deviceAddress = new RazberryDeviceAddress(io.getControllerUrl(), io.getDeviceId(), io.isConvertPercentFactor());
                deviceAddressMap.put(io.getLogicalName(), deviceAddress);
                FloatInput ain = findIoDevice(deviceAddress, ioMapper.getFloatInputs());
                if (ain != null) {
                    List<FloatInput> c = floatInputMap.get(io.getControllerUrl());
                    if (c == null) {
                        c = new ArrayList<>();
                        floatInputMap.put(io.getControllerUrl(), c);
                    }
                    c.add(ain);
                }
            }
        }
    }

    private void configureFloatOutputs(RazberryConfiguration config) {
        for (RazberryConfiguration.AnalogIO io : config.getAnalogOutputs()) {
            if (io.getControllerUrl().equals(config.getControllerUrl())) {
                RazberryDeviceAddress deviceAddress = new RazberryDeviceAddress(io.getControllerUrl(), io.getDeviceId(), io.isConvertPercentFactor());
                deviceAddressMap.put(io.getLogicalName(), deviceAddress);
                FloatOutput aout = findIoDevice(deviceAddress, ioMapper.getFloatOutputs());
                if (aout != null) {
                    List<FloatOutput> c = floatOutputMap.get(io.getControllerUrl());
                    if (c == null) {
                        c = new ArrayList<>();
                        floatOutputMap.put(io.getControllerUrl(), c);
                    }
                    c.add(aout);
                }
            }
        }
    }

    private void configureAlarmInputs(RazberryConfiguration config) {
        for (RazberryConfiguration.AlarmInput io : config.getAlarmInputs()) {
            if (io.getControllerUrl().equals(config.getControllerUrl())) {
                RazberryDeviceAddress deviceAddress = new RazberryDeviceAddress(io.getControllerUrl(), io.getDeviceNumber(), io.getInstanceNumber());
                deviceAddressMap.put(io.getLogicalName(), deviceAddress);
                AlarmInput ain = findIoDevice(deviceAddress, ioMapper.getAlarmInputs());
                if (ain != null) {
                    List<AlarmInput> c = alarmInputMap.get(io.getControllerUrl());
                    if (c == null) {
                        c = new ArrayList<>();
                        alarmInputMap.put(io.getControllerUrl(), c);
                    }
                    c.add(ain);
                }
            }
        }
    }

    private void preTick() {
        for (RazberrySlot slot : razberrySlots) {
            handleDevicesResponse(slot);
            handleDataResponse(slot);
        }
        triggerRazberryRequests();
    }

    private void handleDevicesResponse(RazberrySlot slot) {
        if (slot.devicesResponse != null && slot.devicesResponse.isDone()) {
            try {
                ResponseEntity<ZAutomationDevicesReply> responseEntity = slot.devicesResponse.get();
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    ZAutomationDevicesReply reply = responseEntity.getBody();
                    log.trace("Reply from uri: " + slot.razberryUri + ", reply: " + reply);
                    if (reply.getData().getDevices().length > 0) {
                        log.debug("Reply with devices from uri: " + slot.razberryUri + ", reply: " + reply);
                    }
                    copyRazberryBitInputsToLogicalBitInputs(slot, reply, bitInputMap.get(slot.razberryUri));
                    copyRazberryBitInputsToLogicalBitInputs(slot, reply, bitOutputMap.get(slot.razberryUri));
                    copyRazberryAnalogInputsToLogicalAnalogInputs(slot, reply, floatInputMap.get(slot.razberryUri));
                    copyRazberryAnalogInputsToLogicalAnalogInputs(slot, reply, floatOutputMap.get(slot.razberryUri));
                    if (reply.getData().getDevices().length > 0) {
                        slot.updateTimeLastDevicesReply = reply.getData().getUpdateTime();
                    }
                } else {
                    log.error("getDevices failed, uri: " + slot.razberryUri +
                            ", status: " + responseEntity.getStatusCode());
                    slot.updateTimeLastDevicesReply = 0;
                }
                slot.devicesResponse = null;
            } catch (InterruptedException | ExecutionException e) {
                if (e.getCause() instanceof RestClientException) {
                    log.debug("RestClientException,razberryUri:" + slot.razberryUri + ": " + e.getMessage());
                } else {
                    throw new IllegalStateException("Unexpected exception: " + e.getClass().getName(), e);
                }
            }
        }
    }

    private void handleDataResponse(RazberrySlot slot) {
        if (slot.dataResponse != null && slot.dataResponse.isDone()) {
            try {
                ResponseEntity<ZWayDataReply> responseEntity = slot.dataResponse.get();
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    ZWayDataReply reply = responseEntity.getBody();
                    log.trace("Reply from uri: " + slot.razberryUri + ", reply: " + reply);
                    if (!reply.getAlarmMap().isEmpty()) {
                        log.debug("Reply with alarmMap from uri: " + slot.razberryUri + ", reply: " + reply);
                    }
                    copyRazberryAlarmInputsToLogicalAlarmInputs(slot, reply, alarmInputMap.get(slot.razberryUri));
                    if (!reply.getAlarmMap().isEmpty()) {
                        slot.updateTimeLastDataReply = reply.getUpdateTime();
                    }
                } else {
                    log.error("getDataSince failed, uri: " + slot.razberryUri +
                            ", status: " + responseEntity.getStatusCode());
                    slot.updateTimeLastDataReply = 0;
                }
                slot.dataResponse = null;
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException("Unexpected exception: " + e.getClass().getName(), e);
            }
        }
    }

    private void triggerRazberryRequests() {
        for (RazberrySlot slot : razberrySlots) {
            if (slot.devicesResponse == null) {
                // No ongoing devices request, add a new one
                slot.devicesResponse = razberryClient.getDevices(slot.razberryUri, slot.updateTimeLastDevicesReply + 1);
            }
            if (slot.dataResponse == null) {
                // No ongoing data request, add a new one
                slot.dataResponse = razberryClient.getDataSince(slot.razberryUri, slot.updateTimeLastDevicesReply + 1);
            }
        }
    }

    private void copyRazberryBitInputsToLogicalBitInputs(RazberrySlot slot, ZAutomationDevicesReply reply,
                                                         List<? extends BitInput> bitInputs) {
        // Copy Razberry digital inputs and outputs to mapped logical inputs or outputs
        if (bitInputs != null) {
            // There are mapped logical inputs on this Razberry
            for (BitInput din : bitInputs) {
                RazberryDeviceAddress deviceAddress = deviceAddressMap.get(din.getName());
                ZAutomationDevice device = getRazberryDevice(reply, deviceAddress);
                if (device != null) {
                    boolean level = getLevelOfSwitchBinary(device);
                    log.info("SwitchBinary " + din.getName() + " " + (level ? "on" : "off"));
                    din.setInputStatus(device.getUpdateTime(), level);
                }
            }
        }
    }

    private void copyRazberryAnalogInputsToLogicalAnalogInputs(RazberrySlot slot, ZAutomationDevicesReply reply,
                                                               List<? extends FloatInput> analogInputs) {
        // Copy Razberry analog inputs and outputs to mapped logical inputs or outputs
        if (analogInputs != null) {
            // There are mapped analog inputs on this Razberry
            for (FloatInput ain : analogInputs) {
                RazberryDeviceAddress deviceAddress = deviceAddressMap.get(ain.getName());
                ZAutomationDevice device = getRazberryDevice(reply, deviceAddress);
                if (device != null) {
                    float value = getLevelOfSensorMultilevel(device, deviceAddressMap.get(ain.getName()));
                    value = convertAnalogInputValue(deviceAddress.isConvertPercentFactor(), value);
                    log.info(String.format("Sensor %s changed value to %s", ain.getName(), ain.getValueAsString()));
                    ain.setStatus(device.getUpdateTime(), value);
                }
            }
        }
    }

    private void copyRazberryAlarmInputsToLogicalAlarmInputs(RazberrySlot slot, ZWayDataReply reply,
                                                             List<? extends AlarmInput> alarmInputs) {
        // Copy Razberry alarm inputs and outputs to mapped logical alarm inputs
        if (alarmInputs != null) {
            // There are mapped alarm inputs on this Razberry
            for (AlarmInput ain : alarmInputs) {
                RazberryDeviceAddress deviceAddress = deviceAddressMap.get(ain.getName());
                if (deviceAddress.getDeviceNumber() != null && deviceAddress.getInstanceNumber() != null) {
                    Map<Integer, ZWayAlarm> dataMap = reply.getAlarmMap().get(deviceAddress.getDeviceNumber());
                    if (dataMap != null) {
                        for (Map.Entry<Integer, ZWayAlarm> e : dataMap.entrySet()) {
                            long alarmTime = e.getValue().getUpdateTime() * 1000;
                            if (ain.getTimestamp() != alarmTime) {
                                ZWayAlarm zalarm = e.getValue();
                                if (zalarm.getEventValue() != 0) {
                                    ain.triggerAlarm(e.getValue().getEventValue(), e.getValue().getEventString(), alarmTime);
                                    log.info(String.format("Alarm %s triggered: %s",
                                            ain.getName(), ain.getValueAsString()));
                                } else {
                                    ain.resetAlarm(alarmTime);
                                    log.info(String.format("Alarm %s reset with parameters: %s",
                                            ain.getName(), toString(zalarm.getEventParameters())));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private String toString(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : list) {
            if (sb.length() == 0) {
                sb.append(",");
            }
            sb.append(String.format("%d", i));
        }
        return sb.toString();
    }

    private ZAutomationDevice getRazberryDevice(ZAutomationDevicesReply reply, RazberryDeviceAddress deviceAddress) {
        if (reply != null) {
            for (ZAutomationDevice device : reply.getData().getDevices()) {
                if (device.getId().equals(deviceAddress.getDeviceId())) {
                    return device;
                }
            }
        }
        return null;
    }

    private boolean getLevelOfSwitchBinary(ZAutomationDevice device) {
        return device.getMetrics().getLevel().equals("on");
    }

    private float getLevelOfSensorMultilevel(ZAutomationDevice device, RazberryDeviceAddress deviceAddress) {
        try {
            return Float.parseFloat(device.getMetrics().getLevel());
        } catch (NumberFormatException e) {
            log.error("Expected value of type float for deviceAddress: + " + deviceAddress +
                    ", got value: " + device.getMetrics().getLevel());
        }
        return 0f;
    }

    private void postTick() {
        for (RazberrySlot slot : razberrySlots) {
            writeLogicalBitOutputsToRazberry(slot.razberryUri);
            writeLogicalAnalogOutputsToRazberry(slot.razberryUri);
        }
        triggerRazberryRequests();
    }

    private void writeLogicalBitOutputsToRazberry(String razberryUri) {
        // Copy touched mapped logical outputs to Razberry digital outputs
        List<BitOutput> bitOutputs = bitOutputMap.get(razberryUri);
        if (bitOutputs != null) {
            // There are mapped logical outputs using this Razberry address. Set touched logical outputs in Razberry
            for (BitOutput dout : bitOutputs) {
                if (dout != null && dout.isTouched()) {
                    log.info("Set SwitchBinary " + dout.getName() + " to " + dout.getInputStatus() +
                            ",address: " + dout.getDeviceAddress());
                    String deviceId = deviceAddressMap.get(dout.getName()).getDeviceId();
                    razberryClient.setSwitch(razberryUri, deviceId, dout.getInputStatus());
                    dout.clearTouched();
                }
            }
        }
    }

    private void writeLogicalAnalogOutputsToRazberry(String razberryUri) {
        List<FloatOutput> analogOutputs = floatOutputMap.get(razberryUri);
        if (analogOutputs != null) {
            // There are mapped logical inputs using this Razberry address. Set touched logical outputs in Razberry
            for (FloatOutput aout : analogOutputs) {
                if (aout != null && aout.isTouched()) {
                    log.info(String.format("Set output %s to level %s, address: %s", aout.getName(),
                            aout.getValueAsString(), aout.getDeviceAddress()));
                    RazberryDeviceAddress deviceAddress = deviceAddressMap.get(aout.getName());
                    razberryClient.setFloatOutput(razberryUri, deviceAddress.getDeviceId(),
                            convertAnalogOutputValue(deviceAddress.isConvertPercentFactor(), aout.getValue()));
                    aout.clearTouched();
                }
            }
        }
    }

    private float convertAnalogOutputValue(boolean convertToPercentage, float value) {
        if (convertToPercentage) {
            return value * 100f;

        }
        return value;
    }

    private float convertAnalogInputValue(boolean convertToFactor, float value) {
        if (convertToFactor) {
            return value / 100f;

        }
        return value;
    }

    private void mapDevice(RazberryConfiguration config, String razberryUri) {
        for (RazberryConfiguration.BitIO io : config.getInputs()) {
            DeviceAddress address = new RazberryDeviceAddress(razberryUri, io.getDeviceId());
            ioMapper.mapBitInput(io.getLogicalName(), address.toString(), io.getDescription());
            log.info("Mapped Di " + io.getLogicalName() + " to address " + address.toString());
        }
        for (RazberryConfiguration.BitIO io : config.getOutputs()) {
            DeviceAddress address = new RazberryDeviceAddress(razberryUri, io.getDeviceId());
            ioMapper.mapBitOutput(io.getLogicalName(), address.toString(), io.getDescription());
            log.info("Mapped Do " + io.getLogicalName() + " to address " + address.toString());
        }
        for (RazberryConfiguration.AnalogIO io : config.getAnalogInputs()) {
            DeviceAddress address = new RazberryDeviceAddress(razberryUri, io.getDeviceId());
            try {
                ioMapper.mapFloatInput(io.getLogicalName(), address.toString(), io.getDescription(),
                        io.getUnit(), io.getNumDecimals(), io.getMin(), io.getMax());
            } catch (IoMappingException e) {
                log.error("Failed to map floatInput " + io.getDeviceId() + ":" + e.getMessage());
            }
            log.info("Mapped Ai " + io.getLogicalName() + " to address " + address.toString());
        }
        for (RazberryConfiguration.AnalogIO io : config.getAnalogOutputs()) {
            DeviceAddress address = new RazberryDeviceAddress(razberryUri, io.getDeviceId());
            try {
                ioMapper.mapFloatOutput(io.getLogicalName(), address.toString(), io.getDescription(),
                        io.getUnit(), io.getNumDecimals(), io.getMin(), io.getMax());
            } catch (IoMappingException e) {
                log.error("Failed to map floatOutput " + io.getDeviceId() + ":" + e.getMessage());
            }
            log.info("Mapped Ao " + io.getLogicalName() + " to address " + address.toString());
        }
        for (RazberryConfiguration.AlarmInput ali : config.getAlarmInputs()) {
            DeviceAddress address = new RazberryDeviceAddress(razberryUri, ali.getDeviceNumber(), ali.getInstanceNumber());
            try {
                ioMapper.mapAlarmInput(ali.getLogicalName(), address.toString(), ali.getDescription());
            } catch (IoMappingException e) {
                log.error("Failed to map alarmInput " + ali.getDeviceId() + ":" + e.getMessage());
            }
            log.info("Mapped Li " + ali.getLogicalName() + " to address " + address.toString());
        }
    }
}

