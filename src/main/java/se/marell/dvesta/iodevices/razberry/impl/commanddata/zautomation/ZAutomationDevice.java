/*
 * Created by Daniel Marell 14-11-23 17:15
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation;

import java.util.List;

public class ZAutomationDevice {
    private String id;
    private ZAutomationDeviceMetrics metrics;
    private List<String> tags;
    private String location;
    private String deviceType;
    private long updateTime;

    public ZAutomationDevice() {
    }

    public ZAutomationDevice(String id, ZAutomationDeviceMetrics metrics, List<String> tags, String location, String deviceType, long updateTime) {
        this.id = id;
        this.metrics = metrics;
        this.tags = tags;
        this.location = location;
        this.deviceType = deviceType;
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZAutomationDeviceMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(ZAutomationDeviceMetrics metrics) {
        this.metrics = metrics;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "{" +
                "class: RazberryDevice" +
                ", id: " + id +
                ", metrics: " + metrics +
                ", tags: " + tags +
                ", location: " + location +
                ", deviceType: " + deviceType +
                ", updateTime: " + updateTime +
                '}';
    }
}

