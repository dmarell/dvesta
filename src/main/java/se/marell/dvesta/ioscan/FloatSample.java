/*
 * Created daniel 2010-feb-22 11:19:14
 */
package se.marell.dvesta.ioscan;

public class FloatSample extends AbstractSample {
    private static final long serialVersionUID = 1;
    public float value;

    public FloatSample() {
    }

    public FloatSample(long timestamp, float value) {
        super(timestamp);
        this.value = value;
    }
}
