/*
 * Created by daniel Mar 3, 2002 9:30:08 PM
 */
package se.marell.dvesta.datalogger;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DoubleData extends AbstractData {
    private double value;
    private int decimalPlaces;
    private double resolution;
    private DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();

    public DoubleData(int dataItemId, String name, String description, int decimalPlaces) {
        super(dataItemId, name, description);
        this.decimalPlaces = decimalPlaces;
        this.resolution = getResolution();
        this.df.setMaximumFractionDigits(decimalPlaces);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if (Math.abs(value - this.value) > resolution) {
            this.value = value;
            isModified = true;
        }
    }

    private double getResolution() {
        switch (decimalPlaces) {
            case 0:
                return 1.0;
            case 1:
                return 0.1;
            case 2:
                return 0.01;
            case 3:
                return 0.001;
            case 4:
                return 0.0001;
            case 5:
                return 0.00001;
            default:
                return 0.000001;
        }
    }

    @Override
    public String toString() {
        return df.format(value);
    }

    @Override
    public void setValue(String s) {
        value = Double.parseDouble(s);
    }
}
