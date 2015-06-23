/*
 * Created by daniel Mar 3, 2002 9:30:08 PM
 */
package se.marell.dvesta.batterybackdata;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FloatData extends BatteryBackupData {
    private float value;
    private int decimalPlaces;
    private float resolution;
    private DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();

    public FloatData() {
    }

    public FloatData(BatteryBackupRepository batteryBackupRepository, String name, int decimalPlaces) {
        super(name);
        batteryBackupRepository.createObject(name, this);
        this.decimalPlaces = decimalPlaces;
        this.resolution = getResolution();
        this.df.setMaximumFractionDigits(decimalPlaces);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        if (Math.abs(value - this.value) > resolution) {
            this.value = value;
        }
    }

    public void addValue(float value) {
        setValue(getValue() + value);
    }

    private float getResolution() {
        switch (decimalPlaces) {
            case 0:
                return 1;
            case 1:
                return 0.1f;
            case 2:
                return 0.01f;
            case 3:
                return 0.001f;
            case 4:
                return 0.0001f;
            case 5:
                return 0.00001f;
            default:
                return 0.000001f;
        }
    }

    @Override
    public String toString() {
        return df.format(value);
    }

    @Override
    public void setValue(String s) {
        value = Float.parseFloat(s);
    }
}
