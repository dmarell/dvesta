/*
 * Created by Daniel Marell 04/04/16.
 */
package se.marell.dvesta.tickengine;

/**
 * Represents a named tick consumer.
 */
public class NamedTickConsumer {
    private String name;
    private TickConsumer tickConsumer;

    public NamedTickConsumer(String name, TickConsumer tickConsumer) throws IllegalArgumentException {
        this.name = name;
        this.tickConsumer = tickConsumer;
    }

    public NamedTickConsumer(String name, TickConsumer tickConsumer, TickEngine tickEngine, int lowFreq, int hiFreq, int preferredFreq) {
        this(name, tickConsumer);
        addToTickFrequency(tickEngine, lowFreq, hiFreq, preferredFreq);
    }

    public void addToTickFrequency(TickEngine tickEngine, int lowFreq, int hiFreq, int preferredFreq) {
        int tickFrequency = tickEngine.findFrequency(lowFreq, hiFreq, preferredFreq);
        if (tickFrequency == 0) {
            throw new IllegalArgumentException("Failed to find a suitable tick frequency: " + name);
        }
        tickEngine.addTickConsumer(tickFrequency, this);
    }

    /**
     * @return Name of consumer. It is highly practical if it is unique among a module's tick consumers.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Get the tick consumer
     */
    public TickConsumer getTickConsumer() {
        return tickConsumer;
    }
}
