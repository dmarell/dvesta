/*
 * Created by Daniel Marell 2009-apr-20 23:45:02
 */
package se.marell.dvesta.tickengine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Bean holding tick execution statistics for a control plugin instance in a certain tick execution frequency.
 */
public class TickConsumerStats implements Serializable {
    private static final long serialVersionUID = 1;

    public static class TickBucket implements Serializable {
        private static final long serialVersionUID = 1;

        public int tickDuration;
        public boolean tickError;

        /**
         * @param tickDuration Execution time in ms for this tick
         * @param tickError    Flag if a RuntimeException was caught in this tick
         */
        public TickBucket(int tickDuration, boolean tickError) {
            this.tickDuration = tickDuration;
            this.tickError = tickError;
        }
    }

    private String controlPluginName;
    private long timestamp;
    private int accumulatedMinTickDuration = Integer.MAX_VALUE;
    private int accumulatedMaxTickDuration = 0;
    private TickBucket[] tickHistory;
    private int nextTickHistoryIndex;

    public TickConsumerStats(String controlPluginName, int numHistoryTicks) {
        this.controlPluginName = controlPluginName;
        tickHistory = new TickBucket[numHistoryTicks];
        for (int i = 0; i < numHistoryTicks; ++i) {
            tickHistory[i] = new TickBucket(0, false);
        }
    }

    public TickConsumerStats(String controlPluginName, long timestamp,
                             int accumulatedMinTickDuration, int accumulatedMaxTickDuration,
                             Collection<TickBucket> tickHistory) {
        this.controlPluginName = controlPluginName;
        this.timestamp = timestamp;
        this.accumulatedMinTickDuration = accumulatedMinTickDuration;
        this.accumulatedMaxTickDuration = accumulatedMaxTickDuration;
        this.tickHistory = (TickBucket[]) tickHistory.toArray();
    }

    public String getControlPluginName() {
        return controlPluginName;
    }

    public void addTickStats(int tickDuration, boolean tickError) {
        TickBucket b = tickHistory[nextTickHistoryIndex];
        b.tickDuration = tickDuration;
        b.tickError = tickError;
        if (++nextTickHistoryIndex >= tickHistory.length) {
            nextTickHistoryIndex = 0;
        }
        if (tickDuration > accumulatedMaxTickDuration) {
            accumulatedMaxTickDuration = tickDuration;
        }
        if (tickDuration < accumulatedMinTickDuration) {
            accumulatedMinTickDuration = tickDuration;
        }
        timestamp = System.currentTimeMillis();
    }

    /**
     * @return execution information per tick for latest ticks in this frequency. Index 0 = newest
     */
    public Collection<TickBucket> getHistory() {
        Collection<TickBucket> a = new ArrayList<TickBucket>(tickHistory.length);
        int i = nextTickHistoryIndex;
        do {
            a.add(tickHistory[i]);
            if (++i == tickHistory.length) {
                i = 0;
            }
        } while (i != nextTickHistoryIndex);
        return a;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getAccumulatedMinTickDuration() {
        return accumulatedMinTickDuration;
    }

    public int getAccumulatedMaxTickDuration() {
        return accumulatedMaxTickDuration;
    }
}
