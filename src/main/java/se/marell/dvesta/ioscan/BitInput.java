/*
 * Created by Daniel Marell Jan 21, 2010 11:44:21 PM
 */
package se.marell.dvesta.ioscan;

public interface BitInput extends IoDevice {
    /**
     * @return value of input
     */
    boolean getInputStatus();

    /**
     * Used by producer to set status of the input, probably from the physical unit.
     * By calling this method the device is considered connected to its device/source.
     *
     * @param timestamp Timestamp when read from physical unit
     * @param status    status of input from physical unit
     */
    void setInputStatus(long timestamp, boolean status);

    /**
     * @return true if override mode is on
     */
    boolean getOverrideStatus();

    /**
     * Set override status. In effect when getOverrideStatus() is true. Overrides input status.
     *
     * @param overrideStatus The override status
     */
    void setOverrideStatus(boolean overrideStatus);
}
