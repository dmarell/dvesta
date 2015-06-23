/*
 * Created by Daniel Marell Mar 3, 2002 9:35:23 PM
 */
package se.marell.dvesta.batterybackdata;

public class ShortData extends BatteryBackupData {
    private short value;

    public ShortData() {
    }

    public ShortData(BatteryBackupRepository batteryBackupRepository, String name) {
        super(name);
        batteryBackupRepository.createObject(name, this);
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
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
        value = Short.parseShort(s);
    }
}
