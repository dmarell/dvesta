/*
 * Created by daniel Mar 3, 2002 9:22:59 PM
 */
package se.marell.dvesta.datalogger;

public abstract class AbstractData {
    private int dataItemId;
    private String name;
    private String description;
    protected boolean isModified = true;

    public AbstractData(int dataItemId, String name, String description) {
        this.name = name;
        this.dataItemId = dataItemId;
        this.description = description;
    }

    public int getDataItemId() {
        return dataItemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public abstract void setValue(String s);
}
