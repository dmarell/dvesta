/*
 * Created by Daniel Marell 14-01-21 21:29
 */
package se.marell.dvesta.iodevices.owfs.impl;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.marell.dvesta.iodevices.owfs.config.DS2405DeviceAddress;

import java.io.File;
import java.io.IOException;

public class DS2405Device extends OwfsDevice {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private DS2405DeviceAddress address;

    public DS2405Device(String mountPoint, DS2405DeviceAddress address) {
        super(mountPoint);
        this.address = address;
    }

    @Override
    public DS2405DeviceAddress getAddress() {
        return address;
    }

    @Override
    public void initDevice() {
        // Nothing to do
    }

    public boolean readValue() throws IOException {
        try {
            File dir = new File(getMountPoint(), address.getDeviceAddress());
            String value = FileUtils.readFileToString(new File(dir, "sensed"));
            boolean status = !value.equals("0");
            return address.isInvert() ? !status : status;
        } catch (IOException e) {
            log.debug("Failed to read device " + address.getDeviceAddress() + ":" + e.getMessage());
            return false;
        }
    }

    public void setValue(boolean value) {
        //TODO implement
    }
}
