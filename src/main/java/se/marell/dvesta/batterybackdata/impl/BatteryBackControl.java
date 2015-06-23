/*
 * Created by Daniel Marell 2011-09-14 21:53
 */
package se.marell.dvesta.batterybackdata.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.marell.dcommons.time.PassiveTimer;
import se.marell.dvesta.tickengine.TickConsumer;
import se.marell.dvesta.tickengine.TickEngine;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class BatteryBackControl implements ServletContextListener {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TickEngine tickEngine;

    private PassiveTimer autoSaveTimer = new PassiveTimer(60 * 1000);

    @Autowired
    private FileDataRepository repository;

    private TickConsumer tickConsumer = new TickConsumer() {
        @Override
        public String getName() {
            return BatteryBackControl.this.getName();
        }

        @Override
        public void executeTick() {
            tick();
        }
    };

    private String getName() {
        return "batterybackcontrol";
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            repository.restore();
            log.info("Restored state");
        } catch (ParameterPersistenceException e) {
            log.info("Failed to restore state: " + e.getMessage());
        }

        int tickFrequency = tickEngine.findFrequency(1, 10, 1);
        if (tickFrequency == 0) {
            log.info("Failed to find a suitable tick frequency: " + getName());
            return;
        }
        tickEngine.addTickConsumer(tickFrequency, tickConsumer);

        log.info("Started " + getName());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("deactivating " + getName());
        tickEngine.removeTickConsumer(tickConsumer);
        repository.save();
        log.info("Saved state");
        log.info("deactivated " + getName());
    }

    private void tick() {
        if (autoSaveTimer.hasExpired()) {
            autoSaveTimer.restart();
            repository.save();
        }
    }
}
