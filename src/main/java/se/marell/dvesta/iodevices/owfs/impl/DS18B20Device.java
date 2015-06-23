/*
 * Created by Daniel Marell 14-01-21 21:35
 */
package se.marell.dvesta.iodevices.owfs.impl;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.marell.dvesta.iodevices.owfs.config.DS18B20DeviceAddress;

import java.io.File;
import java.io.IOException;

public class DS18B20Device extends OwfsDevice {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private DS18B20DeviceAddress address;

    public DS18B20Device(String mountPoint, DS18B20DeviceAddress address) {
        super(mountPoint);
        this.address = address;
    }

    @Override
    public DS18B20DeviceAddress getAddress() {
        return address;
    }

    @Override
    public void initDevice() {
        // Nothing todo
    }

    public double readValue() throws IOException {
        try {
            File dir = new File(getMountPoint(), getAddress().getDeviceAddress());
            File f = new File(dir, "temperature" + (address.getSelectedResolution() + 9));
            log.debug("Reading temperature from file " + f.getAbsolutePath());
            String value = FileUtils.readFileToString(f);
            return Double.parseDouble(value);
        } catch (IOException e) {
            log.debug("Failed to read device " + getAddress().getDeviceAddress() + ":" + e.getMessage());
            throw e;
        }
    }
}
