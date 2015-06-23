/*
 * Created by Daniel Marell 2008-okt-08 23:08:38
 */
package se.marell.dvesta.tickengine;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public interface TickEngine {
    /**
     * @return Existing tick frequencies
     */
    @NotNull
    Collection<Integer> getTickFrequencies();

    /**
     * Find the best tick frequency given the constraints.
     *
     * @param lowFreq      Lowest permitted frequency
     * @param hiFreq       Highest permitted frequency
     * @param preferredFreq Preferred frequency
     * @return frequency or 0 if no frequency was found
     */
    int findFrequency(int lowFreq, int hiFreq, int preferredFreq);

    /**
     * Add a tick consumer called immediately before every execution tick.
     *
     * @param tickFrequency Tick frequency. Must be an existing tick frequency according to #getFrequencies()
     * @param consumer      Tick consumer object
     */
    void addTickConsumer(int tickFrequency, @NotNull TickConsumer consumer);

    /**
     * Add a tick consumer called immediately before every execution tick.
     *
     * @param lowFreq      Lowest permitted frequency
     * @param hiFreq       Highest permitted frequency
     * @param preferedFreq Preferred frequency
     * @param consumer     Tick consumer object
     * @return frequency or 0 if no frequency was found
     */
    int addTickConsumer(int lowFreq, int hiFreq, int preferedFreq, @NotNull TickConsumer consumer);

    /**
     * Add a tick consumer called immediately before every execution tick.
     *
     * @param tickFrequency Tick frequency. Must be an existing tick frequency according to #getFrequencies()
     * @param consumer      Tick consumer object
     */
    void addPreTickConsumer(int tickFrequency, @NotNull TickConsumer consumer);

    /**
     * Add a tick consumer called immediately after every execution tick.
     *
     * @param tickFrequency Tick frequency. Must be an existing tick frequency according to #getFrequencies()
     * @param consumer      Tick consumer object
     */
    void addPostTickConsumer(int tickFrequency, @NotNull TickConsumer consumer);

    /**
     * Remove all references to a tick consumer from everywhere in the TickEngine.
     *
     * @param consumer Tick consumer to remove
     * @return Number of places this tick consumer were removed from
     */
    int removeTickConsumer(@NotNull TickConsumer consumer);

    /**
     * Get statistics for all tick frequencies.
     *
     * @return Statistics for all tick frequencies
     */
    Collection<TickFrequencyStats> getTickFrequencyStats();
}
