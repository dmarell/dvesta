/*
 * Created by Daniel Marell Mar 2, 2002 22:05:22 PM
 */
package se.marell.dvesta.tickengine.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.marell.dvesta.tickengine.TickConsumer;
import se.marell.dvesta.tickengine.TickConsumerStats;
import se.marell.dvesta.tickengine.TickFrequencyStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class TickFrequency {
    private static class ListBucket {
        TickConsumer consumer;
        TickConsumerStats stats;
        int frequency;

        private ListBucket(TickConsumer consumer, int frequency) {
            this.consumer = consumer;
            stats = new TickConsumerStats(consumer.getName(), frequency * 1000);
        }
    }

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private int tickFrequency;
    private List<ListBucket> consumers;
    private List<ListBucket> preTickConsumers;
    private List<ListBucket> postTickConsumers;
    private TickFrequencyStats freqStats;
    private boolean shutdown;
    private Thread thread;

    private static class TickSupervisorInfo {
        private long startTime;
        private String moduleName;

        private TickSupervisorInfo(long startTime, String moduleName) {
            this.startTime = startTime;
            this.moduleName = moduleName;
        }

        public long getStartTime() {
            return startTime;
        }

        public String getModuleName() {
            return moduleName;
        }
    }

    private Map<Integer, TickSupervisorInfo> tickSupervisorMap = new HashMap<Integer, TickSupervisorInfo>();
    private Thread tickSupervisorThread;

    public TickFrequency(int tickFrequency) {
        this.tickFrequency = tickFrequency;
        this.consumers = new CopyOnWriteArrayList<ListBucket>();
        this.preTickConsumers = new CopyOnWriteArrayList<ListBucket>();
        this.postTickConsumers = new CopyOnWriteArrayList<ListBucket>();
        freqStats = new TickFrequencyStats(tickFrequency, tickFrequency);
    }

    public void spawn(int threadPriority) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("tick frequency " + tickFrequency + " started");
                runFrequency();
                log.info("tick frequency " + tickFrequency + " stopped");
            }
        });
        thread.setPriority(threadPriority);
        thread.start();

        tickSupervisorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runTickSupervisorThread();
            }
        });
        tickSupervisorThread.start();
    }

    public void shutdown() {
        shutdown = true;
    }

    public boolean isAlive() {
        return thread.isAlive();
    }

    public synchronized void addConsumer(TickConsumer consumer) {
        List<ListBucket> newList = new ArrayList<ListBucket>(consumers);
        newList.add(new ListBucket(consumer, tickFrequency));
        consumers = newList;
    }

    public synchronized void addPreTickConsumer(TickConsumer consumer) {
        List<ListBucket> newList = new ArrayList<ListBucket>(preTickConsumers);
        newList.add(new ListBucket(consumer, tickFrequency));
        preTickConsumers = newList;
    }

    public synchronized void addPostTickConsumer(TickConsumer consumer) {
        List<ListBucket> newList = new ArrayList<ListBucket>(postTickConsumers);
        newList.add(new ListBucket(consumer, tickFrequency));
        postTickConsumers = newList;
    }

    public synchronized int removeConsumer(TickConsumer consumer) {
        int n = consumers.size() + preTickConsumers.size() + postTickConsumers.size();
        consumers = removeConsumers(consumers, consumer);
        preTickConsumers = removeConsumers(preTickConsumers, consumer);
        postTickConsumers = removeConsumers(postTickConsumers, consumer);
        return n - (consumers.size() + preTickConsumers.size() + postTickConsumers.size());
    }

    private List<ListBucket> removeConsumers(List<ListBucket> localConsumers, TickConsumer consumer) {
        List<ListBucket> newList = new ArrayList<ListBucket>();
        for (ListBucket b : localConsumers) {
            if (b.consumer != consumer) {
                newList.add(b);
            }
        }
        return newList;
    }

    public synchronized TickFrequencyStats getStats() {
        List<TickConsumerStats> stats = new ArrayList<TickConsumerStats>();
        for (ListBucket b : consumers) {
            stats.add(b.stats);
        }
        freqStats.setTickConsumerStats(stats);
        return freqStats;
    }

    public int getTickFrequency() {
        return tickFrequency;
    }

    public synchronized List<TickConsumer> getConsumers() {
        List<TickConsumer> c = new ArrayList<TickConsumer>();
        for (ListBucket b : consumers) {
            c.add(b.consumer);
        }
        return c;
    }

    public void runFrequency() {
        long timestamp = System.currentTimeMillis();
        final long cycle = 1000 / tickFrequency; // Duration of a single cycle

        while (!shutdown) {
            // Execute all consumers
            executeTickConsumer(preTickConsumers);
            executeTickConsumer(consumers);
            executeTickConsumer(postTickConsumers);

            long now = System.currentTimeMillis();
            freqStats.addTickStats((int) (now - timestamp));

            // Calculate time to sleep until start of next cycle
            long sleepTime = cycle - (now - timestamp);
            if (sleepTime < 0) {
                sleepTime = 1; // Always sleep at least 1ms in order to not lockup completely
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignore) {
            }
            timestamp += cycle;
        }
    }

    private void executeTickConsumer(List<ListBucket> c) {
        for (ListBucket b : c) {
            long t = System.currentTimeMillis();
            boolean error = false;
            try {
                synchronized (this) {
                    tickSupervisorMap.put(
                            b.frequency,
                            new TickSupervisorInfo(System.currentTimeMillis(), b.consumer.getName()));
                }
                b.consumer.executeTick();
                t = System.currentTimeMillis() - t;
            } catch (RuntimeException e) {
                log.error("Freq=" + tickFrequency + " caught exception in consumer " + b.consumer.getName(), e);
                error = true;
            }
            b.stats.addTickStats((int) t, error);
        }
    }

    private void runTickSupervisorThread() {
        while (true) {
            synchronized (this) {
                for (Map.Entry<Integer, TickSupervisorInfo> e : tickSupervisorMap.entrySet()) {
                    long execTime = System.currentTimeMillis() - e.getValue().startTime;
                    int frequency = e.getKey();
                    double availableTimeForAllModules = 1.0 / frequency * 1000.0;
                    if (execTime > availableTimeForAllModules * 2) {
                        log.warn("Execution time exceeded slot time, module " + e.getValue().getModuleName() + " execTime=" + execTime);
                    }
                }
            }
            if (shutdown) {
                return;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignore) {
            }
        }
    }
}
