/*
 * Created by Daniel Marell Jan 22, 2010 11:03:46 AM
 */
package se.marell.dvesta.ioscan;

public interface BitOutput extends BitInput {
    /**
     * Set status of output.
     *
     * @param status new status
     */
    void setOutputStatus(boolean status);

    /**
     * Check if output status has been changed since clearTouched has been called
     *
     * @return true if status has changed
     */
    boolean isTouched();

    /**
     * Clears the touched status flag
     */
    void clearTouched();
}
