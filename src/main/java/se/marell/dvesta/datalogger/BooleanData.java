/*
 * Created by daniel Mar 3, 2002 10:31:32 PM
 */
package se.marell.dvesta.datalogger;

public class BooleanData extends AbstractData {
    private boolean value;

    public BooleanData(int dataItemId, String name, String description) {
        super(dataItemId, name, description);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        if (value != this.value) {
            this.value = value;
            isModified = true;
        }
    }

    @Override
    public String toString() {
        return value ? "1" : "0";
    }

    @Override
    public void setValue(String s) {
        value = s.equalsIgnoreCase("true") || s.equals("1");
    }
}
