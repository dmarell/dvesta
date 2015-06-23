/*
 * Created by Daniel Marell 15-06-20 19:03
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.basic;

public class ZWayBasicReply {
    private String name;
    private ZWayBasicData data;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZWayBasicData getData() {
        return data;
    }

    public void setData(ZWayBasicData data) {
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
        return "RazberryBasicReply{" +
                "name='" + name + '\'' +
                ", data=" + data +
                ", id=" + id +
                '}';
    }
}
