/*
 * Created by Daniel Marell 2011-09-11 14:08
 */
package se.marell.dvesta.tickengine;

public abstract class AbstractTickConsumer implements TickConsumer {
    private String name;

    public AbstractTickConsumer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
