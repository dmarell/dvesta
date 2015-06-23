/*
 * Created by Daniel Marell 14-01-21 21:31
 */
package se.marell.dvesta.iodevices.owfs.impl;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.marell.dvesta.iodevices.owfs.config.DS2450DeviceAddress;

import java.io.File;
import java.io.IOException;

public class DS2450Device extends OwfsDevice {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private DS2450DeviceAddress address;

    public DS2450Device(String mountPoint, DS2450DeviceAddress address) {
        super(mountPoint);
        this.address = address;
    }

    @Override
    public DS2450DeviceAddress getAddress() {
        return address;
    }

    @Override
    public void initDevice() {
        if (address.isPowered()) {
            // Write "1" to file "power"
            File dir = new File(getMountPoint(), address.getDeviceAddress());
            try {
                FileUtils.writeStringToFile(new File(dir, "power"), "1");
            } catch (Exception e) {
                log.debug("Failed to init device:" + address.getDescriptiveDeviceName() + ":" + e.getMessage());
            }
        }
    }

    public double readValue() throws IOException {
        try {
            File dir = new File(getMountPoint(), address.getDeviceAddress());
            File f;
            if (address.getSelectedResolution() == 0) {
                f = new File(dir, getFilename());
            } else {
                f = new File(new File(dir, "8bit"), getFilename());
            }
            log.debug("Reading voltage from file " + f.getAbsolutePath());
            String value = FileUtils.readFileToString(f);
            return (Double.parseDouble(value) + address.getOffset()) * address.getFactor();
        } catch (IOException e) {
            log.debug("Failed to read device " + address.getDeviceAddress() + ":" + e.getMessage());
            throw e;
        }
    }

    private String getFilename() {
        /*
        A/D-bits selectedRange selectedResolution range      filename
        16       0             0                  0-5.10V    volt.A
        8        0             1                  0-5.10V    8bit/volt.A
        16       1             0                  0-2.55V    volt2.A
        8        1             1                  0-2.55V    8bit/volt2.A-D
        */
        if (address.getSelectedRange() == 0) {
            return "volt." + getSuffix(address.getChannel());
        }
        return "volt2." + getSuffix(address.getChannel());
    }

    private char getSuffix(int channel) {
        switch (channel) {
            case 0:
                return 'A';
            case 1:
                return 'B';
            case 2:
                return 'C';
            case 3:
                return 'D';
            default:
                return '-';
        }
    }
}
