/*
 * Created by Daniel Marell 15-06-20 16:18
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.alarmsensor;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ZWayAlarmSensorReply {
    private String name;
    private ZWayAlarmSensorData data;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZWayAlarmSensorData getData() {
        return data;
    }

    public void setData(ZWayAlarmSensorData data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RazberryAlarmSensorReply {" +
                "name: '" + name + '\'' +
                ", data: " + data +
                ", id: " + id +
                '}';
    }
}
