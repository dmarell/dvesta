/*
 * Created by Daniel Marell 2008-sep-11 09:16:08
 */
package se.marell.dvesta.tickengine;

/**
 * Implemented by classes that consume execution ticks, i.e., be a part of a specific tick execution frequency.
 */
public interface TickConsumer {
    /**
     * @return Name of consumer. Must be unique among tick consumers.
     */
    String getName();

    /**
     * This method is called by the execution framework in the requested frequency. The tick consumer is expected
     * to perform its work/logic here.
     */
    void executeTick();
}
