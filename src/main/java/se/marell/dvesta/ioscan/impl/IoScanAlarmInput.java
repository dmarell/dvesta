/*
 * Created by Daniel Marell 15-06-21 09:00
 */
package se.marell.dvesta.ioscan.impl;

import se.marell.dvesta.ioscan.AlarmInput;
import se.marell.dvesta.ioscan.IoType;

public class IoScanAlarmInput extends AbstractIoDevice implements AlarmInput {
    private static final long serialVersionUID = 1;
    private int alarmValue;
    private String alarmString;
    private boolean overrideEnable;
    private boolean overrideStatus;

    public IoScanAlarmInput(String name) {
        super(IoType.ALARM_INPUT, name, "");
    }

    @Override
    public int getAlarm() {
        return alarmValue;
    }

    @Override
    public String getAlarmString() {
        return alarmString;
    }

    @Override
    public String getValueAsString() {
        return String.format("%s (%d)", alarmString, alarmValue);
    }

    @Override
    public boolean isOverrideEnable() {
        return overrideEnable;
    }

    @Override
    public void setOverrideEnable(boolean overrideEnable) {
        this.overrideEnable = overrideEnable;
    }

    @Override
    public void triggerAlarm(int alarmValue, String alarmString, long timestamp) {
        this.alarmValue = alarmValue;
        this.alarmString = alarmString;
        this.setIoStatus(timestamp, true);
        //TODO handle override
    }

    @Override
    public void resetAlarm(long timestamp) {
        this.alarmValue = 0;
        this.alarmString = null;
        this.setIoStatus(timestamp, true);
    }
}
