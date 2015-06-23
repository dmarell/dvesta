/*
 * Created by Daniel Marell 15-06-20 23:14
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

@JsonDeserialize(using = ZWayDataReplyDeserializer.class)
public class ZWayDataReply {
    private Map<Integer/*deviceNumber*/, Map<Integer/*dataNumber*/, ZWayAlarm>> alarmMap;
    private long updateTime;

    public Map<Integer, Map<Integer, ZWayAlarm>> getAlarmMap() {
        return alarmMap;
    }

    public void setAlarmMap(Map<Integer, Map<Integer, ZWayAlarm>> alarmMap) {
        this.alarmMap = alarmMap;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ZWayDataReply{" +
                "alarmMap=" + toString(alarmMap) +
                ", updateTime=" + updateTime +
                '}';
    }

    private String toString(Map<Integer, Map<Integer, ZWayAlarm>> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Map<Integer, ZWayAlarm>> e : map.entrySet()) {
            sb.append("key=" + e.getKey() + ",value=" + e.getValue());
        }
        return sb.toString();
    }

}
