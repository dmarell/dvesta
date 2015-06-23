/*
 * Created daniel 2010-feb-21 12:38:16
 */
package se.marell.dvesta.ioscan;

public class BitSample extends AbstractSample {
    private static final long serialVersionUID = 1;
    public boolean status;

    public BitSample() {
    }

    public BitSample(long timestamp, boolean status) {
        super(timestamp);
        this.status = status;
    }
}
