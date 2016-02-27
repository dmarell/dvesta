/*
 * Created by Daniel Marell 26/02/16.
 */
package se.marell.dvesta.iodevices.razberry;

import java.util.Map;

public class RazberryStatus {
    private Map<String, String> lastError;
    private long timestamp;
    private boolean hasError;

    public RazberryStatus(Map<String, String> lastError, long timestamp, boolean hasError) {
        this.lastError = lastError;
        this.timestamp = timestamp;
        this.hasError = hasError;
    }

    public Map<String, String> getLastError() {
        return lastError;
    }

    public boolean hasError() {
        return hasError;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
