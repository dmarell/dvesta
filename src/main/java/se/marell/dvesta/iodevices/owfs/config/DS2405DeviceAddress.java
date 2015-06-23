/*
 * Created by Daniel Marell 14-01-27 20:13
 */
package se.marell.dvesta.iodevices.owfs.config;

public class DS2405DeviceAddress extends OwfsDeviceAddress {
    private boolean invert;
    private boolean pureInput;

    public DS2405DeviceAddress(String logicalName, String description, String deviceAddress,
                               boolean invert, boolean pureInput) {
        super(logicalName, description, deviceAddress);
        this.invert = invert;
        this.pureInput = pureInput;
    }

    @Override
    public String getDeviceType() {
        return "DS2405";
    }

    public boolean isInvert() {
        return invert;
    }

    public boolean isPureInput() {
        return pureInput;
    }
}
