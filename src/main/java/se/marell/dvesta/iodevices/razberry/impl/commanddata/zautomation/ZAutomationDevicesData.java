/*
 * Created by Daniel Marell 14-11-23 17:33
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation;

import java.util.Arrays;

public class ZAutomationDevicesData {
    private boolean structureChanged;
    private long updateTime;
    private ZAutomationDevice[] devices;

    public ZAutomationDevicesData() {
    }

    public ZAutomationDevicesData(boolean structureChanged, long updateTime, ZAutomationDevice[] devices) {
        this.structureChanged = structureChanged;
        this.updateTime = updateTime;
        this.devices = devices;
    }

    public boolean isStructureChanged() {
        return structureChanged;
    }

    public void setStructureChanged(boolean structureChanged) {
        this.structureChanged = structureChanged;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public ZAutomationDevice[] getDevices() {
        return devices;
    }

    public void setDevices(ZAutomationDevice[] devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "{" +
                "class: RazberryDevicesData" +
                ", structureChanged: " + structureChanged +
                ", updateTime: " + updateTime +
                ", devices: " + Arrays.toString(devices) +
                '}';
    }
}
