/**
 * Created by Daniel Marell Dec 16, 2002 10:00:10 PM
 */
package se.marell.dvesta.iodevices.adu208.config;

import se.marell.dvesta.ioscan.DeviceAddress;

public class DigitalInputAdu208Address extends DeviceAddress {
    private static final long serialVersionUID = 1;
    private int deviceNumber;
    private int bitno;
    private String str;

    public DigitalInputAdu208Address(int deviceNumber, int bitno) {
        this.deviceNumber = deviceNumber;
        this.bitno = bitno;
        this.str = "ADU208:" + deviceNumber + ":i" + bitno;
    }

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public int getBitno() {
        return bitno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DigitalInputAdu208Address that = (DigitalInputAdu208Address) o;

        if (bitno != that.bitno) return false;
        if (deviceNumber != that.deviceNumber) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = deviceNumber;
        result = 31 * result + bitno;
        return result;
    }

    @Override
    public String getGlobalDeviceIdentifier() {
        return str;
    }

    @Override
    public String toString() {
        return str;
    }
}
