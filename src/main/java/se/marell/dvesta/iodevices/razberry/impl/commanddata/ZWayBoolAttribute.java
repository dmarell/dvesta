/*
 * Created by Daniel Marell 15-06-20 16:43
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata;

public class ZWayBoolAttribute {
    private long invalidateTime;
    private long updateTime;
    private String type;
    private boolean value;

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

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RazberryBoolReading {" +
                "invalidateTime: " + invalidateTime +
                ", updateTime: " + updateTime +
                ", type: '" + type + '\'' +
                ", value: " + value +
                '}';
    }
}
