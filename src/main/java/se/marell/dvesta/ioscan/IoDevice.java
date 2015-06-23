/*
 * Created by daniel Jan 21, 2010 11:48:36 PM
 */
package se.marell.dvesta.ioscan;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Base class for an object holding status and meta data for an I/O device.
 */
public interface IoDevice extends Serializable {
    /**
     * Get value formatted in a string.
     * @return Value as string
     */
    String getValueAsString();

    /**
     * Get IO type.
     *
     * @return IO type
     */
    @NotNull
    IoType getType();

    /**
     * @return Logical name of device (for example "tempHdd1")
     */
    @NotNull
    String getName();

    /**
     * @return Unit or empty string if unit is undefined or not applicable
     */
    @NotNull
    String getUnit();

    /**
     * @return Timestamp when device was last updated from its physical device (input) or time when last set by
     * controlling software (output). 0 if device has never been mapped or controlled by software.
     */
    long getTimestamp();

    /**
     * @return true if device mapped to its physical device and the physical device is working
     */
    boolean isConnected();

    /**
     * Indicate that the physical device is not accessible.
     */
    void setDisconnected();

    /**
     * @return Physical device deviceAddress or null if unmapped
     */
    //@Nullable
    String getDeviceAddress();

    /**
     * @return Human readable description of the mapped physical deviceAddress (for example "Temperature of HDD #1").
     * Empty string if device is unmapped. Never null.
     */
    @NotNull
    String getDeviceAddressDescription();

    /**
     * Set I/O status from physical device.
     *
     * @param timestamp   Timestamp in msec when physical device was last updated from H/W
     * @param isConnected true if physical device is considered connected to its H/W
     */
    void setIoStatus(long timestamp, boolean isConnected);

    /**
     * Check if override I/O status is in effect.
     *
     * @return True if override I/O status is in effect
     */
    boolean isOverrideEnable();

    /**
     * @param overrideEnable True if override I/O status should be in effect
     */
    void setOverrideEnable(boolean overrideEnable);

    /**
     * Map this device to a physical device deviceAddress.
     *
     * @param deviceAddress            Physical deviceAddress of device
     * @param deviceAddressDescription Human readable description of the physical deviceAddress
     */
    void mapDevice(@NotNull String deviceAddress, @NotNull String deviceAddressDescription);

    /**
     * Remove mapping to a physical device.
     */
    void unmapDevice();
}
