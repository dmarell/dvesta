/*
 * Created by Daniel Marell 2011-10-09 19:06
 */
package se.marell.dvesta.iodevices.razberry.config;

import se.marell.dvesta.ioscan.DeviceAddress;

public class RazberryDeviceAddress extends DeviceAddress {
    private static final long serialVersionUID = 1;
    private String url;
    private String deviceId;
    private boolean convertPercentFactor;
    private Integer deviceNumber;
    private Integer instanceNumber;

    public RazberryDeviceAddress(String url, String deviceId) {
        this.url = url;
        this.deviceId = deviceId;
    }

    public RazberryDeviceAddress(String url, int deviceNumber, int instanceNumber) {
        this.url = url;
        this.deviceNumber = deviceNumber;
        this.instanceNumber = instanceNumber;
        deviceId = String.format("%d-%d", deviceNumber, instanceNumber);
    }

    public RazberryDeviceAddress(String url, String deviceId, boolean convertPercentFactor) {
        this.url = url;
        this.deviceId = deviceId;
        this.convertPercentFactor = convertPercentFactor;
    }

    public String getUrl() {
        return url;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public boolean isConvertPercentFactor() {
        return convertPercentFactor;
    }

    public Integer getDeviceNumber() {
        return deviceNumber;
    }

    public Integer getInstanceNumber() {
        return instanceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RazberryDeviceAddress that = (RazberryDeviceAddress) o;

        if (convertPercentFactor != that.convertPercentFactor) return false;
        if (!url.equals(that.url)) return false;
        if (!deviceId.equals(that.deviceId)) return false;
        if (deviceNumber != null ? !deviceNumber.equals(that.deviceNumber) : that.deviceNumber != null) return false;
        return !(instanceNumber != null ? !instanceNumber.equals(that.instanceNumber) : that.instanceNumber != null);

    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + deviceId.hashCode();
        result = 31 * result + (convertPercentFactor ? 1 : 0);
        result = 31 * result + (deviceNumber != null ? deviceNumber.hashCode() : 0);
        result = 31 * result + (instanceNumber != null ? instanceNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RazberryDeviceAddress{" +
                "url='" + url + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", convertPercentFactor=" + convertPercentFactor +
                ", deviceNumber=" + deviceNumber +
                ", instanceNumber=" + instanceNumber +
                '}';
    }
}
