/*
 * Created by Daniel Marell 14-12-29 08:12
 */
package se.marell.dvesta.iodevices;

import se.marell.dvesta.ioscan.DeviceAddress;
import se.marell.dvesta.ioscan.IoDevice;

import java.util.Collection;

public abstract class AbstractIoController {
    protected <E extends IoDevice> E findIoDevice(DeviceAddress address, Collection<E> devices) {
        for (E device : devices) {
            String deviceAddress = device.getDeviceAddress();
            if (deviceAddress != null && deviceAddress.equals(address.toString())) {
                return device;
            }
        }
        return null;
    }
}
