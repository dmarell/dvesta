/*
 * Created by daniel Jan 22, 2010 11:04:32 AM
 */
package se.marell.dvesta.ioscan;

public interface FloatOutput extends FloatInput {
    /**
     * Set value of output
     *
     * @param value new status
     */
    void setValue(float value);

    /**
     * Check if output has been changed since clearTouched has been called
     *
     * @return true if value has changed
     */
    boolean isTouched();

    /**
     * Clears the touched status flag
     */
    void clearTouched();
}

