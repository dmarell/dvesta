/*
 * Created by Daniel Marell 15-06-20 16:42
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.alarmsensor;

import se.marell.dvesta.iodevices.razberry.impl.commanddata.ZWayIntAttribute;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.ZWayStringAttribute;

public class ZWayAlarmSensorDataItem {
    private long invalidateTime;
    private long updateTime;
    private String type;
    private int value;
    private ZWayIntAttribute srcId;
    private ZWayIntAttribute sensorState;
    private ZWayIntAttribute sensorTime;
    private ZWayStringAttribute typeString;

    public long getInvalidateTime() {
        return invalidateTime;
    }

    public void setInvalidateTime(long invalidateTime) {
        this.invalidateTime = invalidateTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ZWayIntAttribute getSrcId() {
        return srcId;
    }

    public void setSrcId(ZWayIntAttribute srcId) {
        this.srcId = srcId;
    }

    public ZWayIntAttribute getSensorState() {
        return sensorState;
    }

    public void setSensorState(ZWayIntAttribute sensorState) {
        this.sensorState = sensorState;
    }

    public ZWayIntAttribute getSensorTime() {
        return sensorTime;
    }

    public void setSensorTime(ZWayIntAttribute sensorTime) {
        this.sensorTime = sensorTime;
    }

    public ZWayStringAttribute getTypeString() {
        return typeString;
    }

    public void setTypeString(ZWayStringAttribute typeString) {
        this.typeString = typeString;
    }

    @Override
    public String toString() {
        return "RazberryAlarmSensorDataItem {" +
                "invalidateTime: " + invalidateTime +
                ", updateTime: " + updateTime +
                ", type: '" + type + '\'' +
                ", value: " + value +
                ", srcId: " + srcId +
                ", sensorState: " + sensorState +
                ", sensorTime: " + sensorTime +
                ", typeString: " + typeString +
                '}';
    }
}
