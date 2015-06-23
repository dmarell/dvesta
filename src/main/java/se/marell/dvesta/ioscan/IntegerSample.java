/*
 * Created daniel 2010-feb-22 11:20:12
 */
package se.marell.dvesta.ioscan;

public class IntegerSample extends AbstractSample {
    private static final long serialVersionUID = 1;
    public int value;

    public IntegerSample() {
    }

    public IntegerSample(long timestamp, int value) {
        super(timestamp);
        this.value = value;
    }
}
