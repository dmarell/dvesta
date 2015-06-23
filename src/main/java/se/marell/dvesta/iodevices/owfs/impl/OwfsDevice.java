/*
 * Created by Daniel Marell 14-01-27 19:36
 */
package se.marell.dvesta.iodevices.owfs.impl;

import se.marell.dvesta.iodevices.owfs.config.OwfsDeviceAddress;

public abstract class OwfsDevice {
    private String mountPoint;

    protected OwfsDevice(String mountPoint) {
        this.mountPoint = mountPoint;
    }

    public String getMountPoint() {
        return mountPoint;
    }

    public abstract OwfsDeviceAddress getAddress();

    /**
     * Initialize device. This method is called at startup and periodically
     * about once a minute in order to re-init devices after power loss/disconnection+reconnection.
     */
    public abstract void initDevice();
}
