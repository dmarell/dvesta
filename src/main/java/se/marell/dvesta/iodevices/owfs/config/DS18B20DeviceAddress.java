/*
 * Created by Daniel Marell 14-01-27 19:50
 */
package se.marell.dvesta.iodevices.owfs.config;

public class DS18B20DeviceAddress extends OwfsDeviceAddress {
    private int selectedResolution;
    private int numDecimals;
    private float minValue;
    private float maxValue;

    public DS18B20DeviceAddress(String logicalName, String description, String deviceAddress,
                                int selectedResolution, int numDecimals, float minValue, float maxValue) {
        super(logicalName, description, deviceAddress);
        this.selectedResolution = selectedResolution;
        this.numDecimals = numDecimals;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public String getDeviceType() {
        return "DS18B20";
    }

    public int getSelectedResolution() {
        return selectedResolution;
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
