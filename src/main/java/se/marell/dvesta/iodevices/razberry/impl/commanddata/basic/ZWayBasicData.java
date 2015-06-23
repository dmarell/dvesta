/*
 * Created by Daniel Marell 15-06-20 19:04
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.basic;

import se.marell.dvesta.iodevices.razberry.impl.commanddata.ZWayBoolAttribute;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.ZWayIntAttribute;

public class ZWayBasicData {
    private long invalidateTime;
    private long updateTime;
    private String type;
    private int value;
    private ZWayBoolAttribute supported;
    private ZWayIntAttribute version;
    private ZWayBoolAttribute security;
    private ZWayBoolAttribute interviewDone;
    private ZWayIntAttribute interviewCounter;
    private ZWayIntAttribute level;

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

    public ZWayBoolAttribute getSupported() {
        return supported;
    }

    public void setSupported(ZWayBoolAttribute supported) {
        this.supported = supported;
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

    public ZWayIntAttribute getLevel() {
        return level;
    }

    public void setLevel(ZWayIntAttribute level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "RazberryBasicData{" +
                "invalidateTime=" + invalidateTime +
                ", updateTime=" + updateTime +
                ", type='" + type + '\'' +
                ", value=" + value +
                ", supported=" + supported +
                ", version=" + version +
                ", security=" + security +
                ", interviewDone=" + interviewDone +
                ", interviewCounter=" + interviewCounter +
                ", level=" + level +
                '}';
    }
}
