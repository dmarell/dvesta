/*
 * Created by Daniel Marell 15-06-21 01:00
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.data;

class AlarmKey {
    private int deviceNumber;
    private int instanceNumber;
    private int dataNumber;
    private String type;

    public AlarmKey(int deviceNumber, int instanceNumber, int dataNumber, String type) {
        this.deviceNumber = deviceNumber;
        this.instanceNumber = instanceNumber;
        this.dataNumber = dataNumber;
        this.type = type;
    }

    public AlarmKey(String fieldName) {
        //TODO parse
    }

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public int getDataNumber() {
        return dataNumber;
    }

    public int getInstanceNumber() {
        return instanceNumber;
    }

    public String getType() {
        return type;
    }
}
