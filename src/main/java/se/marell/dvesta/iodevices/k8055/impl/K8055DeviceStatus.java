/*
 * Created by Daniel Marell 28/02/16.
 */
package se.marell.dvesta.iodevices.k8055.impl;

public class K8055DeviceStatus {
    private boolean pollOk;
    private long timestamp;

    public K8055DeviceStatus(boolean pollOk, long timestamp) {
        this.pollOk = pollOk;
        this.timestamp = timestamp;
    }

    public boolean isPollOk() {
        return pollOk;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
