/*
 * Created by Daniel Marell Mar 2, 2002 22:01:43 PM
 */
package se.marell.dvesta.tickengine.impl;

import java.util.ArrayList;
import java.util.Collection;

public class TickFrequencyController {
    private Collection<TickFrequency> tickFrequencies;

    public TickFrequencyController() {
        this.tickFrequencies = new ArrayList<TickFrequency>();
    }

    public void addTickFreq(TickFrequency freq) {
        tickFrequencies.add(freq);
    }

    public void removeTickFreq(TickFrequency freq) {
        tickFrequencies.remove(freq);
    }

    public Collection<TickFrequency> getTickFreqs() {
        return tickFrequencies;
    }

    public void shutdown() {
        for (TickFrequency ef : tickFrequencies) {
            ef.shutdown();
        }
    }

    public void spawn() {
        // Spawn all frequencies
        int prio = Thread.MAX_PRIORITY - 1;
        for (TickFrequency ef : tickFrequencies) {
            ef.spawn(prio--);
        }
    }
}
