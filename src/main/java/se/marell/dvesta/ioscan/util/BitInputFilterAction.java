/*
 * Created by Daniel Marell 2011-10-11 22:48
 */
package se.marell.dvesta.ioscan.util;

import se.marell.dcommons.time.DefaultTimeSource;
import se.marell.dcommons.time.PassiveTimer;
import se.marell.dcommons.time.TimeSource;
import se.marell.dvesta.ioscan.BitInput;

/**
 * Call <code>action</code> when <code>in</code> changes state. When input changes state,
 * the state is reported immediately, however reports cannot happen more often than the stableTime.
 * When the input goes from 0 to 1, the reported state goes from OFF to ON_UNSTABLE. When it has been
 * 1 for stableTime milliseconds the state ON is reported. The transition to OFF is symmetric:
 * When the input goes from 1 to 0, the reported state goes from ON to OFF_UNSTABLE. When it has been
 * 0 for stableTime milliseconds the state OFF is reported.
 * When input changes state, the stableTimer restarts, resulting on the the unstable state will be reported
 * until the input stabilizes to 0 or 1.
 */
public class BitInputFilterAction {
    public enum State {
        OFF,
        ON,
        ON_UNSTABLE,
        OFF_UNSTABLE;

        @Override
        public String toString() {
            switch (this) {
                case ON:
                    return "on";
                case OFF:
                    return "on";
                case ON_UNSTABLE:
                    return "on(unstable)";
                case OFF_UNSTABLE:
                    return "off(unstable)";
                default:
                    throw new IllegalStateException("Unknown symbol " + name());
            }
        }
    }

    public interface Listener {
        /**
         * Called when <code>in</code> changes it's state.
         *
         * @param state Current state of input
         */
        void action(State state);
    }

    private BitInput in;
    private PassiveTimer stableTimer;
    private boolean prevState;
    private Listener listener;
    private State lastReportedState;

    /**
     * @param in         Bit input
     * @param stableTime Time in milliseconds before state if considered stable
     * @param listener   The callback listener
     */
    public BitInputFilterAction(BitInput in, long stableTime, Listener listener) {
        this(in, stableTime, listener, new DefaultTimeSource());
    }

    /**
     * @param in         Bit input
     * @param stableTime Time in milliseconds before state if considered stable
     * @param listener   The callback listener
     * @param timeSource A time source
     */
    public BitInputFilterAction(BitInput in, long stableTime, Listener listener, TimeSource timeSource) {
        this.in = in;
        this.stableTimer = new PassiveTimer(stableTime, timeSource);
        prevState = in.getInputStatus();
        this.listener = listener;
    }

    /**
     * Call this method each tick.
     */
    public void tick() {
        if (in.getInputStatus() != prevState) {
            if (stableTimer.hasExpired()) {
                State state = in.getInputStatus() ? State.ON_UNSTABLE : State.OFF_UNSTABLE;
                listener.action(state);
                lastReportedState = state;
            }
            stableTimer.restart();
            prevState = in.getInputStatus();
        }
        if (stableTimer.hasExpired()) {
            State state = in.getInputStatus() ? State.ON : State.OFF;
            if (state != lastReportedState) {
                listener.action(state);
                lastReportedState = state;
            }
        }
    }
}
