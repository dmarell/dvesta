/*
 * Created by Daniel Marell 2011-09-11 08:43
 */
package se.marell.dvesta.tickengine.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.marell.dvesta.tickengine.TickConsumer;
import se.marell.dvesta.tickengine.TickEngine;
import se.marell.dvesta.tickengine.TickFrequencyStats;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class SequentialTickEngine implements TickEngine {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private Collection<TickFrequency> tickFrequencies;
    private TickFrequencyController execController;

    @PostConstruct
    private void activate() {
        log.info("activate tick engine");
        tickFrequencies = new ArrayList<TickFrequency>();
        execController = new TickFrequencyController();

        int[] frequencies = new int[]{1, 20, 100};
        for (int f : frequencies) {
            TickFrequency tf = new TickFrequency(f);
            tickFrequencies.add(tf);
            execController.addTickFreq(tf);
        }
        execController.spawn();
        log.info("activate tick engine");
    }

    @PreDestroy
    private void deactivate() {
        log.info("deactivate tick engine");
        execController.shutdown();
    }

    @NotNull
    @Override
    public synchronized Collection<Integer> getTickFrequencies() {
        Collection<Integer> frequencies = new ArrayList<Integer>();
        for (TickFrequency tf : tickFrequencies) {
            frequencies.add(tf.getTickFrequency());
        }
        return frequencies;
    }

    @Override
    public synchronized int findFrequency(int lowFreq, int hiFreq, int preferredFreq) {
        TickFrequency f = selectFrequency(lowFreq, hiFreq, preferredFreq);
        if (f != null) {
            return f.getTickFrequency();
        }
        return 0;
    }

    @Override
    public synchronized void addTickConsumer(int tickFrequency, @NotNull TickConsumer consumer) {
        TickFrequency f = selectFrequency(tickFrequency, tickFrequency, tickFrequency);
        if (f == null) {
            throw new RuntimeException("Tried to add TickConsumer (" + consumer.toString() +
                    ") to a non-existing frequency " + tickFrequency);
        }
        f.addConsumer(consumer);
    }

    @Override
    public synchronized int addTickConsumer(int lowFreq, int hiFreq, int preferedFreq, @NotNull TickConsumer consumer) {
        TickFrequency f = selectFrequency(lowFreq, hiFreq, preferedFreq);
        if (f != null) {
            f.addConsumer(consumer);
            return f.getTickFrequency();
        }
        return 0;
    }

    @Override
    public synchronized void addPreTickConsumer(int tickFrequency, @NotNull TickConsumer consumer) {
        TickFrequency f = selectFrequency(tickFrequency, tickFrequency, tickFrequency);
        if (f == null) {
            throw new RuntimeException("Tried to add PreTickConsumer (" + consumer.toString() +
                    ") to a non-existing frequency " + tickFrequency);
        }
        f.addPreTickConsumer(consumer);
    }

    @Override
    public synchronized void addPostTickConsumer(int tickFrequency, @NotNull TickConsumer consumer) {
        TickFrequency f = selectFrequency(tickFrequency, tickFrequency, tickFrequency);
        if (f == null) {
            throw new RuntimeException("Tried to add PostTickConsumer (" + consumer.toString() +
                    ") to a non-existing frequency " + tickFrequency);
        }
        f.addPostTickConsumer(consumer);
    }

    private synchronized TickFrequency selectFrequency(int lowFreq, int hiFreq, int preferredFreq) {
        int minDiff = Integer.MAX_VALUE;
        TickFrequency bestTf = null;
        for (TickFrequency tf : searchFrequencies(lowFreq, hiFreq, preferredFreq)) {
            int diff = Math.abs(tf.getTickFrequency() - preferredFreq);
            if (diff < minDiff) {
                minDiff = diff;
                bestTf = tf;
            }
        }
        return bestTf;
    }

    private synchronized Collection<TickFrequency> searchFrequencies(int lowFreq, int hiFreq, int preferredFreq) {
        Collection<TickFrequency> freqs = new ArrayList<TickFrequency>();
        for (TickFrequency tf : tickFrequencies) {
            if (tf.getTickFrequency() >= lowFreq && tf.getTickFrequency() <= hiFreq) {
                freqs.add(tf);
            }
        }
        return freqs;
    }

    @Override
    public synchronized int removeTickConsumer(@NotNull TickConsumer consumer) {
        int n = 0;
        for (TickFrequency tf : tickFrequencies) {
            n += tf.removeConsumer(consumer);
        }
        return n;
    }

    @Override
    public synchronized Collection<TickFrequencyStats> getTickFrequencyStats() {
        Collection<TickFrequencyStats> stats = new ArrayList<TickFrequencyStats>();
        for (TickFrequency tf : tickFrequencies) {
            stats.add(tf.getStats());
        }
        return stats;
    }
}
