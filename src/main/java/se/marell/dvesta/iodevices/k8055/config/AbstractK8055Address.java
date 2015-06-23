/*
 * Created by Daniel Marell 2011-10-09 19:06
 */
package se.marell.dvesta.iodevices.k8055.config;

import se.marell.dvesta.ioscan.DeviceAddress;

public abstract class AbstractK8055Address extends DeviceAddress {
    private static final long serialVersionUID = 1;
    private int deviceNumber;
    private int port;
    private String str;

    protected AbstractK8055Address(String portPrefix, int deviceNumber, int port) {
        this.deviceNumber = deviceNumber;
        this.port = port;
        this.str = "K8055:" + deviceNumber + ":" + portPrefix + port;
    }

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractK8055Address that = (AbstractK8055Address) o;

        if (deviceNumber != that.deviceNumber) return false;
        if (port != that.port) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = deviceNumber;
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return str;
    }
}
