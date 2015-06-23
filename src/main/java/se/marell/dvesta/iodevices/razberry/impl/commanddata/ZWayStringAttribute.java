/*
 * Created by Daniel Marell 15-06-20 16:43
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata;

public class ZWayStringAttribute {
    private long invalidateTime;
    private long updateTime;
    private String type;
    private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RazberryStringReading {" +
                "invalidateTime: " + invalidateTime +
                ", updateTime: " + updateTime +
                ", type: '" + type + '\'' +
                ", value: '" + value + '\'' +
                '}';
    }
}

