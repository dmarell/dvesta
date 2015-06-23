/*
 * Created by daniel Mar 3, 2002 10:31:32 PM
 */
package se.marell.dvesta.batterybackdata;

public class BitData extends BatteryBackupData {
    private boolean value;

    public BitData() {
    }

    public BitData(BatteryBackupRepository batteryBackupRepository, String name) {
        super(name);
        batteryBackupRepository.createObject(name, this);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        if (value != this.value) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return value ? "1" : "0";
    }

    @Override
    public void setValue(String s) {
        value = s.equalsIgnoreCase("true") || s.equals("1");
    }
}
