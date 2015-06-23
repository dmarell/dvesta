/*
 * Created by Daniel Marell 14-01-27 19:42
 */
package se.marell.dvesta.iodevices.owfs.config;

public class DS2450DeviceAddress extends OwfsDeviceAddress {
    private int channel;
    private double offset;
    private double factor;
    private String unit;
    private int selectedRange;
    private int selectedResolution;
    private boolean isPowered;
    private int numDecimals;
    private float minValue;
    private float maxValue;

    public DS2450DeviceAddress(String logicalName, String description, String deviceAddress, int channel,
                               double offset, double factor, String unit, int selectedRange,
                               int selectedResolution, boolean isPowered, int numDecimals,
                               float minValue, float maxValue) {
        super(logicalName, description, deviceAddress);
        this.channel = channel;
        this.offset = offset;
        this.factor = factor;
        this.unit = unit;
        this.selectedRange = selectedRange;
        this.selectedResolution = selectedResolution;
        this.isPowered = isPowered;
        this.numDecimals = numDecimals;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public String getDeviceType() {
        return "DS2450";
    }

    public int getChannel() {
        return channel;
    }

    public double getOffset() {
        return offset;
    }

    public double getFactor() {
        return factor;
    }

    public String getUnit() {
        return unit;
    }

    public int getSelectedRange() {
        return selectedRange;
    }

    public int getSelectedResolution() {
        return selectedResolution;
    }

    public boolean isPowered() {
        return isPowered;
    }

    public int getNumDecimals() {
        return numDecimals;
    }

    public float getMinValue() {
        return minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }
}
