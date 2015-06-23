/*
 * Created by Daniel Marell 14-01-27 19:43
 */
package se.marell.dvesta.iodevices.owfs.config;

import se.marell.dvesta.ioscan.DeviceAddress;

public abstract class OwfsDeviceAddress extends DeviceAddress {
    private String logicalName;
    private String description;
    private String deviceAddress;

    public OwfsDeviceAddress(String logicalName, String description, String deviceAddress) {
        this.logicalName = logicalName;
        this.description = description;
        this.deviceAddress = deviceAddress;
    }

    public String getLogicalName() {
        return logicalName;
    }

    public String getDescription() {
        return description;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public abstract String getDeviceType();

    public String getDescriptiveDeviceName() {
        return getDeviceType() + ":" + getDeviceAddress() + ":" + getDescription();
    }
}
