/*
 * Created by Daniel Marell 14-12-15 09:21
 */
package se.marell.dvesta.utils;

import org.junit.Test;
import se.marell.dcommons.time.TimeSource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TimerEventTest {
    long timeInTest = 100;
    int startedCount;
    int stoppedCount;

    @Test
    public void test() throws Exception {
        TimeSource ts = new TimeSource() {
            @Override
            public long currentTimeMillis() {
                return timeInTest;
            }

            @Override
            public long nanoTime() {
                return timeInTest * 1000000;
            }
        };

        TimerEvent e = new TimerEvent(1000, ts, true, new TimerEvent.Listener() {
            @Override
            public void timerStarted() {
                startedCount++;
            }

            @Override
            public void timerStopped() {
                stoppedCount++;
            }
        });

        assertThat(startedCount, is(0));
        assertThat(stoppedCount, is(0));
        assertThat(e.dispatch(false), is(false));
        assertThat(e.dispatch(true), is(true));
        assertThat(startedCount, is(1));
        assertThat(stoppedCount, is(0));

        timeInTest += 700;

        assertThat(e.dispatch(true), is(true));
        assertThat(e.dispatch(false), is(true));
        assertThat(startedCount, is(1));
        assertThat(stoppedCount, is(0));

        timeInTest += 700;

        assertThat(e.dispatch(false), is(false));
        assertThat(startedCount, is(1));
        assertThat(stoppedCount, is(1));
    }
}
