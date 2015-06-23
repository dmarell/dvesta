/*
 * Created by daniel Jan 22, 2010 11:06:35 AM
 */
package se.marell.dvesta.ioscan;

public interface IntegerOutput extends IntegerInput {
    /**
     * Set value of output
     *
     * @param value new status
     */
    void setValue(int value);

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