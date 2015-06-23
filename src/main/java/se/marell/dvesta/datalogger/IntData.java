/**
 * Created by daniel 2004-jan-06 23:07:00
 */
package se.marell.dvesta.datalogger;

public class IntData extends AbstractData {
    private int value;

    public IntData(int dataItemId, String name, String description) {
        super(dataItemId, name, description);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value != this.value) {
            this.value = value;
            isModified = true;
        }
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public void setValue(String s) {
        value = Integer.parseInt(s);
    }
}
