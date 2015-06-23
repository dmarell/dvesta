/*
 * Created by Daniel Marell 2009-apr-19 02:11:24
 */
package se.marell.dvesta.tickengine;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Bean holding tick execution statistics for a tick execution frequency.
 * The length of the array is expected to be large enough to cover time between subsequent updates but at the
 * same time small enough to limit redundant overlaps in subsequent messages.
 */
public class TickFrequencyStats implements Serializable {
    private static final long serialVersionUID = 1;
    private int tickFrequency;
    private int accumulatedMinTickDuration = Integer.MAX_VALUE;
    private int accumulatedMaxTickDuration = 0;
    private int[] tickDurationHistory;
    private int nextTickDurationHistoryIndex;
    private long tickOverrunCount;
    private long tickCount;
    private Collection<TickConsumerStats> stats;

    public TickFrequencyStats(int tickFrequency, int numHistoryTicks) {
        this.tickFrequency = tickFrequency;
        tickDurationHistory = new int[numHistoryTicks];
    }

    public void addTickStats(int duration) {
        tickDurationHistory[nextTickDurationHistoryIndex] = duration;
        if (++nextTickDurationHistoryIndex >= tickDurationHistory.length) {
            nextTickDurationHistoryIndex = 0;
        }
        if (duration > accumulatedMaxTickDuration) {
            accumulatedMaxTickDuration = duration;
        }
        if (duration < accumulatedMinTickDuration) {
            accumulatedMinTickDuration = duration;
        }
        if (duration > 1000 / tickFrequency) {
            ++tickOverrunCount;
        }
        ++tickCount;
    }

    public void setTickConsumerStats(List<TickConsumerStats> stats) {
        this.stats = stats;
    }

    public Collection<TickConsumerStats> getTickConsumerStats() {
        return stats;
    }

    public int getTickFrequency() {
        return tickFrequency;
    }

    public int getAccumulatedMinTickDuration() {
        return accumulatedMinTickDuration;
    }

    public int getAccumulatedMaxTickDuration() {
        return accumulatedMaxTickDuration;
    }

    public int[] getTickDurationHistory() {
        return tickDurationHistory;
    }

    public long getTickOverrunCount() {
        return tickOverrunCount;
    }

    public long getTickCount() {
        return tickCount;
    }
}
