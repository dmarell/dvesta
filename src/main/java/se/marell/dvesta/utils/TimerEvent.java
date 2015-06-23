/*
 * Created by Daniel Marell 14-12-14 11:02
 */
package se.marell.dvesta.utils;

import se.marell.dcommons.time.PassiveTimer;
import se.marell.dcommons.time.TimeSource;

public class TimerEvent {
    private boolean prevInputStatus;
    private PassiveTimer timer;
    private boolean prevIsTimerRunning;
    private boolean startTimerOnPositiveFlank;
    private TimerEvent.Listener listener;

    public TimerEvent(int delayMsec, boolean startTimerOnPositiveFlank, TimerEvent.Listener listener) {
        this.timer = new PassiveTimer(delayMsec);
        this.startTimerOnPositiveFlank = startTimerOnPositiveFlank;
        this.listener = listener;
        timer.forceExpire();
    }

    public TimerEvent(int delayMsec, boolean startTimerOnPositiveFlank) {
        this.timer = new PassiveTimer(delayMsec);
        this.startTimerOnPositiveFlank = startTimerOnPositiveFlank;
        timer.forceExpire();
    }

    public TimerEvent(int delayMsec, TimeSource timesource, boolean startTimerOnPositiveFlank, TimerEvent.Listener listener) {
        this.timer = new PassiveTimer(delayMsec, timesource);
        this.startTimerOnPositiveFlank = startTimerOnPositiveFlank;
        this.listener = listener;
        timer.forceExpire();
    }

    public interface Listener {
        void timerStarted();

        void timerStopped();
    }

    public boolean dispatch(boolean inputStatus) {
        if (inputStatus != prevInputStatus) {
            if (inputStatus == startTimerOnPositiveFlank) {
                timer.restart();
                if (listener != null) {
                    listener.timerStarted();
                }
            }
            prevInputStatus = inputStatus;
        }
        boolean isTimerRunning = timer.isRunning();
        if (!isTimerRunning && prevIsTimerRunning) {
            if (listener != null) {
                listener.timerStopped();
            }
        }
        prevIsTimerRunning = isTimerRunning;
        return isTimerRunning;
    }
}
