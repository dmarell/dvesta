/*
 * Created by Daniel Marell 14-12-23 15:39
 */
package se.marell.dvesta.tickengine;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public abstract class AbstractTickEngine implements TickEngine {
    @Override
    public Collection<Integer> getTickFrequencies() {
        return null;
    }

    @Override
    public int findFrequency(int lowFreq, int hiFreq, int preferredFreq) {
        return preferredFreq;
    }

    @Override
    public void addTickConsumer(int tickFrequency, @NotNull NamedTickConsumer consumer) {

    }

    @Override
    public int addTickConsumer(int lowFreq, int hiFreq, int preferedFreq, @NotNull NamedTickConsumer consumer) {
        return 0;
    }

    @Override
    public void addPreTickConsumer(int tickFrequency, @NotNull NamedTickConsumer consumer) {

    }

    @Override
    public void addPostTickConsumer(int tickFrequency, @NotNull NamedTickConsumer consumer) {

    }

    @Override
    public int removeTickConsumer(@NotNull NamedTickConsumer consumer) {
        return 0;
    }

    @Override
    public Collection<TickFrequencyStats> getTickFrequencyStats() {
        return null;
    }
}
