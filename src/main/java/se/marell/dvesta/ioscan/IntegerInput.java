/*
 * Created by daniel Jan 22, 2010 11:06:01 AM
 */
package se.marell.dvesta.ioscan;

public interface IntegerInput extends IoDevice {
    /**
     * @return value of input
     */
    int getValue();

    /**
     * Used by producer to set status of the input, probably from the physical unit.
     *
     * @param timestamp   Timestamp when read from physical unit
     * @param isConnected true if physical unit currently is accessible/online
     * @param value       value of input from physical unit
     */
    void setStatus(long timestamp, boolean isConnected, int value);

    /**
     * @return true if override mode is on
     */
    int getOverrideValue();

    /**
     * Set override value. In effect when getOverrideValue() is true. Replaces the value
     *
     * @param overrideValue The override value
     */
    void setOverrideValue(int overrideValue);

    /**
     * Note that the value could be outside nominal min and max limits.
     *
     * @return Nominal min value
     */
    int getMin();

    /**
     * Note that the value could be outside nominal min and max limits.
     *
     * @return Nominal max value
     */
    int getMax();
}
