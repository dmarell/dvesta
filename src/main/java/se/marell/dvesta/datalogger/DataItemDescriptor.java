/*
 * Created by Daniel Marell 14-01-19 16:35
 */
package se.marell.dvesta.datalogger;

public class DataItemDescriptor {
    private int dataItemId;
    private String name;
    private String description;

    public DataItemDescriptor(int dataItemId, String name, String description) {
        this.dataItemId = dataItemId;
        this.name = name;
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
}
