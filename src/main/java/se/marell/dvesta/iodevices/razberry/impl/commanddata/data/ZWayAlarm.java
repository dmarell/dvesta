/*
 * Created by Daniel Marell 15-06-20 23:42
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.data;

import java.util.List;

/*
    "devices.6.instances.0.commandClasses.113.data.1.event": {
      "value": 2,
      "type": "int",
      "invalidateTime": 1434705167,
      "updateTime": 1434829759
    },

    "devices.6.instances.0.commandClasses.113.data.1.eventString": {
      "value": "Smoke detected, location unknown",
      "type": "string",
      "invalidateTime": 1434705167,
      "updateTime": 1434829759
    },

    "devices.6.instances.0.commandClasses.113.data.1.eventParameters": {
      "value": [ ],
      "type": "binary",
      "invalidateTime": 1434705167,
      "updateTime": 1434829759
    },
*/
public class ZWayAlarm {
    private Integer eventValue;
    private String eventString;
    private List<Integer> eventParameters;
    private long invalidateTime;
    private long updateTime;

    public ZWayAlarm() {
    }

    public ZWayAlarm(Integer eventValue, String eventString, List<Integer> eventParameters, long invalidateTime, long updateTime) {
        this.eventValue = eventValue;
        this.eventString = eventString;
        this.eventParameters = eventParameters;
        this.invalidateTime = invalidateTime;
        this.updateTime = updateTime;
    }

    public Integer getEventValue() {
        return eventValue;
    }

    public void setEventValue(Integer eventValue) {
        this.eventValue = eventValue;
    }

    public String getEventString() {
        return eventString;
    }

    public void setEventString(String eventString) {
        this.eventString = eventString;
    }

    public List<Integer> getEventParameters() {
        return eventParameters;
    }

    public void setEventParameters(List<Integer> eventParameters) {
        this.eventParameters = eventParameters;
    }

    public long getInvalidateTime() {
        return invalidateTime;
    }

    public void setInvalidateTime(long invalidateTime) {
        this.invalidateTime = invalidateTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ZWayAlarm{" +
                "eventValue=" + eventValue +
                ", eventString='" + eventString + '\'' +
                ", eventParameters=" + eventParameters +
                ", invalidateTime=" + invalidateTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
