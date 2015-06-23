/**
 * Created by daniel 2004-jan-06 23:07:00
 */
package se.marell.dvesta.batterybackdata;

public class IntData extends BatteryBackupData {
    private int value;

    public IntData() {
    }

    public IntData(BatteryBackupRepository batteryBackupRepository, String name) {
        super(name);
        batteryBackupRepository.createObject(name, this);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value != this.value) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public void setValue(String s) {
        value = Integer.parseInt(s);
    }
}
