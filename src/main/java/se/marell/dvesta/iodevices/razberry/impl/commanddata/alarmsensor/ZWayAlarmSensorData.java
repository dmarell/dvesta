/*
 * Created by Daniel Marell 15-06-20 16:21
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.alarmsensor;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.ZWayBoolAttribute;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.ZWayIntAttribute;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.ZWayStringAttribute;

import java.util.Map;

@JsonDeserialize(using = ZWayAlarmSensorDataDeserializer.class)
public class ZWayAlarmSensorData {
    private Map<Integer, ZWayAlarmSensorDataItem> dataMap;
    private long invalidateTime;
    private long updateTime;
    private String type;
    private int value;
    private ZWayIntAttribute version;
    private ZWayBoolAttribute security;
    private ZWayBoolAttribute interviewDone;
    private ZWayIntAttribute interviewCounter;
    private ZWayIntAttribute alarmMap;
    private ZWayStringAttribute alarms;

    public Map<Integer, ZWayAlarmSensorDataItem> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<Integer, ZWayAlarmSensorDataItem> dataMap) {
        this.dataMap = dataMap;
    }

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

    public ZWayIntAttribute getVersion() {
        return version;
    }

    public void setVersion(ZWayIntAttribute version) {
        this.version = version;
    }

    public ZWayBoolAttribute getSecurity() {
        return security;
    }

    public void setSecurity(ZWayBoolAttribute security) {
        this.security = security;
    }

    public ZWayBoolAttribute getInterviewDone() {
        return interviewDone;
    }

    public void setInterviewDone(ZWayBoolAttribute interviewDone) {
        this.interviewDone = interviewDone;
    }

    public ZWayIntAttribute getInterviewCounter() {
        return interviewCounter;
    }

    public void setInterviewCounter(ZWayIntAttribute interviewCounter) {
        this.interviewCounter = interviewCounter;
    }

    public ZWayIntAttribute getAlarmMap() {
        return alarmMap;
    }

    public void setAlarmMap(ZWayIntAttribute alarmMap) {
        this.alarmMap = alarmMap;
    }

    public ZWayStringAttribute getAlarms() {
        return alarms;
    }

    public void setAlarms(ZWayStringAttribute alarms) {
        this.alarms = alarms;
    }

    @Override
    public String toString() {
        return "RazberryAlarmSensorData {" +
                "dataMap: " + dataMap +
                ", invalidateTime: " + invalidateTime +
                ", updateTime: " + updateTime +
                ", type: '" + type + '\'' +
                ", value: " + value +
                ", version: " + version +
                ", security: " + security +
                ", interviewDone: " + interviewDone +
                ", interviewCounter: " + interviewCounter +
                ", alarmMap: " + alarmMap +
                ", alarms: " + alarms +
                '}';
    }
}
