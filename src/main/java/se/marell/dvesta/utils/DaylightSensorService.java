/*
 * Created by Daniel Marell 14-11-30 23:09
 */
package se.marell.dvesta.utils;

public interface DaylightSensorService {
    /**
     * Get precision factor of this implementation. 1.0=fully updated and full precision, 0.0=disconnected/random
     *
     * @return Value between 0 and 1
     */
    double getPrecision();

    /**
     * Check if it is dark outdoor.
     *
     * @return true if dark
     */
    boolean isDarkOutdoor();

    /**
     * Check daylight indoor. More daylight is needed in order to reach indoor.
     *
     * @return true if dark indoor
     */
    boolean isDarkIndoor();

    /**
     * Get value for daylight. 1.0 is full daylight and 0 is complete darkness.
     *
     * @return Value between 0 and 1
     */
    double getDaylight();
}
