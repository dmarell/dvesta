/*
 * Created by Daniel Marell 2010-feb-28 19:16:19
 */
package se.marell.dvesta.ioscan;

import java.io.Serializable;

public abstract class AbstractSample implements Serializable {
    private static final long serialVersionUID = 1;
    public long timestamp;

    protected AbstractSample() {
    }

    protected AbstractSample(long timestamp) {
        this.timestamp = timestamp;
    }
}