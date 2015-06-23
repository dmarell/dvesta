/*
 * Created by Daniel Marell 15-06-21 08:41
 */
package se.marell.dvesta.ioscan;

public interface AlarmInput extends IoDevice {
    /**
     * @return Alarm signal, 0=no alarm
     */
    int getAlarm();

    /**
     * @return Alarm string, null=no alarm
     */
    String getAlarmString();

    /**
     * Trigger an alarm.
     *
     * @param alarmValue  The value
     * @param alarmString The string. Null if alarm is inactive
     * @param timestamp   The alarm timestamp
     */
    void triggerAlarm(int alarmValue, String alarmString, long timestamp);

    /**
     * Reset alarm state.
     *
     * @param timestamp The alarm timestamp
     */
    void resetAlarm(long timestamp);
}
