/*
 * Created by Daniel Marell 2011-09-14 21:53
 */
package se.marell.dvesta.iodevices.owfs.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.marell.dcommons.time.PassiveTimer;
import se.marell.dcommons.time.TimeSource;
import se.marell.dvesta.iodevices.owfs.config.*;
import se.marell.dvesta.ioscan.BitInput;
import se.marell.dvesta.ioscan.FloatInput;
import se.marell.dvesta.ioscan.IoMapper;
import se.marell.dvesta.ioscan.IoMappingException;
import se.marell.dvesta.tickengine.AbstractTickConsumer;
import se.marell.dvesta.tickengine.TickConsumer;
import se.marell.dvesta.tickengine.TickEngine;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class OwfsIoController implements OwfsIoConfigurationService {

    private static class AiStatus {
        private double value;
        private boolean isConnected;

        public AiStatus(double value, boolean isConnected) {
            this.value = value;
            this.isConnected = isConnected;
        }

        public double getValue() {
            return value;
        }

        public boolean isConnected() {
            return isConnected;
        }
    }

    private static class DiStatus {
        private boolean value;
        private boolean isConnected;

        public DiStatus(boolean value, boolean isConnected) {
            this.value = value;
            this.isConnected = isConnected;
        }

        public boolean getValue() {
            return value;
        }

        public boolean isConnected() {
            return isConnected;
        }
    }

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final int LOOP_TIME_MS = 1000;

    private static final String MODULE_NAME = "OwfsIoController";
    private OwfsConfiguration config;
    @Autowired
    private TimeSource timeSource;
    @Autowired
    private IoMapper ioMapper;
    @Autowired
    private TickEngine tickEngine;

    private List<DS18B20Device> devicesDs18b20 = new ArrayList<DS18B20Device>();
    private List<DS2450Device> devicesDs2450 = new ArrayList<DS2450Device>();
    private List<DS2405Device> devicesDs2405 = new ArrayList<DS2405Device>();

    private Map<String, AiStatus> analogInputValueMap = new HashMap<String, AiStatus>();
    private Map<String, DiStatus> bitInputValueMap = new HashMap<String, DiStatus>();

    private Thread thread;
    private Set<String> detectedDevices;
    private Map<String, Integer> deviceLostCounterMap = new TreeMap<String, Integer>();
    private boolean threadStopRequest;

    private PassiveTimer initTimer = new PassiveTimer(10000);

    private TickConsumer preTickConsumer;

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
        log.info("activated " + MODULE_NAME);
    }

    /**
     * Read owfs filesystem asynchronously as fast as the devices allow.
     */
    private void pollOwfs() {
        while (true) {
            long start = timeSource.currentTimeMillis();

            boolean initDevices = initTimer.hasExpired();
            if (initDevices) {
                initTimer.restart();
            }

            reportDeviceChanges();

            for (DS18B20Device device : devicesDs18b20) {
                if (initDevices) {
                    device.initDevice();
                }
                double value;
                try {
                    value = device.readValue();
                    synchronized (OwfsIoController.this) {
                        analogInputValueMap.put(device.getAddress().getLogicalName(), new AiStatus(value, true));
                    }
                } catch (IOException e) {
                    log.debug("Failed to read device " + device.getAddress().getLogicalName() + ":" + e.getMessage());
                }
            }

            for (DS2450Device device : devicesDs2450) {
                if (initDevices) {
                    device.initDevice();
                }
                double value;
                try {
                    value = device.readValue();
                    synchronized (OwfsIoController.this) {
                        analogInputValueMap.put(device.getAddress().getLogicalName(), new AiStatus(value, true));
                    }
                } catch (IOException e) {
                    log.debug("Failed to read device " + device.getAddress().getLogicalName() + ":" + e.getMessage());
                }
            }

            for (DS2405Device device : devicesDs2405) {
                if (initDevices) {
                    device.initDevice();
                }
                boolean value;
                try {
                    value = device.readValue();
                    synchronized (OwfsIoController.this) {
                        bitInputValueMap.put(device.getAddress().getLogicalName(), new DiStatus(value, true));
                    }
                } catch (IOException e) {
                    log.debug("Failed to read device " + device.getAddress().getLogicalName() + ":" + e.getMessage());
                }
            }

            int executionTime = (int) (timeSource.currentTimeMillis() - start);
            log.debug("executionTime=" + executionTime + " ms" +
                    ",number of DS18B20: " + config.getDs18b20Devices().size() +
                    ",number of DS2450: " + config.getDs2450Devices().size() +
                    ",number of DS2405: " + config.getDs2405Devices().size());
            if (executionTime < LOOP_TIME_MS) {
                try {
                    Thread.sleep(LOOP_TIME_MS - executionTime);
                } catch (InterruptedException ignore) {
                }
            }
            if (threadStopRequest) {
                thread = null;
                return;
            }
        }
    }

    @PreDestroy
    private void deactivate() {
        tickEngine.removeTickConsumer(preTickConsumer);
        log.info("deactivated " + MODULE_NAME);
        synchronized (this) {
            threadStopRequest = true;
            notify();
        }
    }

    @Override
    public void setConfiguration(OwfsConfiguration config) {
        log.info("setConfiguration " + MODULE_NAME);

        activate();

        this.config = config;

        for (DS18B20DeviceAddress a : config.getDs18b20Devices()) {
            devicesDs18b20.add(new DS18B20Device(config.getMountPoint(), a));
        }
        for (DS2450DeviceAddress a : config.getDs2450Devices()) {
            devicesDs2450.add(new DS2450Device(config.getMountPoint(), a));
        }
        for (DS2405DeviceAddress a : config.getDs2405Devices()) {
            devicesDs2405.add(new DS2405Device(config.getMountPoint(), a));
        }

        log.info("Detecting devices");

        detectedDevices = getDevices();

        log.info("Mapping DS18B20 devices");

        for (DS18B20Device device : devicesDs18b20) {
            try {
                ioMapper.mapFloatInput(
                        device.getAddress().getLogicalName(),
                        device.getAddress().getDeviceAddress(),
                        device.getAddress().getDescription(),
                        "C",
                        device.getAddress().getNumDecimals(),
                        device.getAddress().getMinValue(),
                        device.getAddress().getMaxValue());
                deviceLostCounterMap.put(device.getAddress().getDeviceAddress(), 0);
            } catch (IoMappingException e) {
                log.error("Failed to map " + device.getAddress().getDescriptiveDeviceName() + ":" + e.getMessage());
            }
        }

        log.info("Mapping DS2450 devices");

        for (DS2450Device device : devicesDs2450) {
            try {
                ioMapper.mapFloatInput(
                        device.getAddress().getLogicalName(),
                        device.getAddress().getDeviceAddress(),
                        device.getAddress().getDescription(),
                        device.getAddress().getUnit(),
                        device.getAddress().getNumDecimals(),
                        device.getAddress().getMinValue(),
                        device.getAddress().getMaxValue());
                deviceLostCounterMap.put(device.getAddress().getDeviceAddress(), 0);
            } catch (IoMappingException e) {
                log.error("Failed to map " + device.getAddress().getDescriptiveDeviceName() + ":" + e.getMessage());
            }
        }

        log.info("Mapping DS2405 devices");

        for (DS2405Device device : devicesDs2405) {
            ioMapper.mapBitInput(
                    device.getAddress().getLogicalName(),
                    device.getAddress().getDeviceAddress(),
                    device.getAddress().getDescription());
            deviceLostCounterMap.put(device.getAddress().getDeviceAddress(), 0);
        }

        int n = tickEngine.removeTickConsumer(preTickConsumer);
        if (n > 0) {
            log.error("removeTickConsumer preTickConsumer=" + n);
        }

        int tickFrequency = tickEngine.findFrequency(1, 10, 1);
        if (tickFrequency == 0) {
            throw new RuntimeException("Failed to find a suitable tick frequency");
        }

        tickEngine.addPreTickConsumer(tickFrequency, preTickConsumer);

        log.info("Starting OWFS thread");

        startOwfsUpdateThread();

        log.info("Started " + MODULE_NAME);
    }

    private void startOwfsUpdateThread() {
        synchronized (this) {
            if (thread != null) {
                throw new IllegalStateException("Already started");
            }
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    pollOwfs();
                }
            });
        }
        thread.start();
    }

    private void preTick() {
        synchronized (OwfsIoController.this) {
            for (Map.Entry<String, AiStatus> e : analogInputValueMap.entrySet()) {
                FloatInput input = ioMapper.findFloatInput(e.getKey());
                if (e.getValue().isConnected()) {
                    input.setStatus(timeSource.currentTimeMillis(), (float) e.getValue().getValue());
                } else {
                    input.setDisconnected();
                }
            }
            for (Map.Entry<String, DiStatus> e : bitInputValueMap.entrySet()) {
                BitInput input = ioMapper.findBitInput(e.getKey());
                if (e.getValue().isConnected()) {
                    input.setInputStatus(timeSource.currentTimeMillis(), e.getValue().getValue());
                } else {
                    input.setDisconnected();
                }
            }
        }
    }

    private void reportDeviceChanges() {
        log.debug("Checking lost and new owfs devices");
        Set<String> devices = getDevices();

        // Report lost devices
        for (String s : detectedDevices) {
            if (!devices.contains(s)) {
                // Increment "lost device counter"
                if (deviceLostCounterMap.get(s) != null) {
                    deviceLostCounterMap.put(s, deviceLostCounterMap.get(s) + 1);
                } else {
                    deviceLostCounterMap.put(s, 0);
                }
                log.info("Lost device " + s + " count=" + deviceLostCounterMap.get(s));
            }
        }

        // Report new devices
        for (String s : devices) {
            if (!detectedDevices.contains(s)) {
                if (deviceLostCounterMap.containsKey(s)) {
                    log.info("Redetected known lost device " + s + " count=" + deviceLostCounterMap.get(s));
                } else {
                    log.info("Detected new unknown device " + s);
                }
            }
        }

        detectedDevices = devices;
    }

    private Set<String> getDevices() {
        File[] dirs = new File(config.getMountPoint()).getAbsoluteFile().listFiles(file -> file.isDirectory()
                && file.getName().matches("^[0-9][0-9].[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]"));

        Set<String> result = new HashSet<String>();
        if (dirs != null) {
            for (File f : dirs) {
                result.add(f.getName());
            }
        }
        return result;
    }
}
