/*
 * Created by Daniel Marell 15-01-01 20:35
 */
package se.marell.dvesta.tickengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Convenience class for simple executable modules with one tick frequency.
 */
@Component
public abstract class ExecutableModule implements ServletContextListener {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TickEngine tickEngine;

    private TickConsumer tickConsumer;

    private int minFrequency;
    private int maxFrequency;
    private int preferredFrequency;

    protected ExecutableModule(int minFrequency, int maxFrequency, int preferredFrequency) {
        this.minFrequency = minFrequency;
        this.maxFrequency = maxFrequency;
        this.preferredFrequency = preferredFrequency;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        initialize(servletContextEvent);

        int tickFrequency = tickEngine.findFrequency(minFrequency, maxFrequency, preferredFrequency);
        if (tickFrequency == 0) {
            throw new IllegalStateException("Failed to find a suitable tick frequency");
        }
        tickConsumer = new TickConsumer() {
            @Override
            public String getName() {
                return ExecutableModule.this.getName();
            }

            @Override
            public void executeTick() {
                ExecutableModule.this.executeTick();
            }
        };
        tickEngine.addTickConsumer(tickFrequency, tickConsumer);

        log.info("Started " + getClass().getSimpleName() + ",tickFrequency=" + tickFrequency);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        tickEngine.removeTickConsumer(tickConsumer);
        shutdown(servletContextEvent);
        log.info("deactivated " + getName());
    }

    protected void initialize(ServletContextEvent servletContextEvent) {
    }

    protected void shutdown(ServletContextEvent servletContextEvent) {
    }

    protected abstract void executeTick();

    public String getName() {
        return getClass().getSimpleName();
    }
}
