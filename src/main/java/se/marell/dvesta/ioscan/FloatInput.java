/*
 * Created by daniel Jan 21, 2010 11:57:01 PM
 */
package se.marell.dvesta.ioscan;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import se.marell.dvesta.ioscan.resources.FloatInputSerializer;

@JsonSerialize(using = FloatInputSerializer.class)
public interface FloatInput extends IoDevice {
    /**
     * @return value of input
     */
    float getValue();

    /**
     * @return Number of decimal places in presentation
     */
    int getNumDecimals();

    /**
     * Used by producer to set status of the input, probably from the physical unit.
     * By calling this method the device is considered connected to its device/source.
     *
     * @param timestamp Timestamp when read from physical unit
     * @param value     value of input from physical unit
     */
    void setStatus(long timestamp, float value);

    /**
     * @return true if override mode is on
     */
    float getOverrideValue();

    /**
     * Set override value. In effect when getOverrideValue() is true. Replaces the value
     *
     * @param overrideValue The override value
     */
    void setOverrideValue(float overrideValue);

    /**
     * Note that the value could be outside nominal min and max limits.
     *
     * @return Nominal min value
     */
    float getMin();

    /**
     * Note that the value could be outside nominal min and max limits.
     *
     * @return Nominal max value
     */
    float getMax();
}
