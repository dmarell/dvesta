/*
 * Created by Daniel Marell 2011-09-14 21:53
 */
package se.marell.dvesta.batterybackdata.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.marell.dcommons.time.PassiveTimer;
import se.marell.dvesta.tickengine.NamedTickConsumer;
import se.marell.dvesta.tickengine.TickEngine;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class BatteryBackControl implements ServletContextListener {
    private static final String MODULE_NAME = BatteryBackControl.class.getSimpleName();
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TickEngine tickEngine;

    private PassiveTimer autoSaveTimer = new PassiveTimer(60 * 1000);

    @Autowired
    private FileDataRepository repository;

    private NamedTickConsumer tickConsumer;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            repository.restore();
            log.info("Restored state");
        } catch (ParameterPersistenceException e) {
            log.info("Failed to restore state: " + e.getMessage());
        }
        tickConsumer = new NamedTickConsumer(MODULE_NAME, this::tick, tickEngine, 1, 10, 1);
        log.info("Started " + MODULE_NAME);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("deactivating " + MODULE_NAME);
        tickEngine.removeTickConsumer(tickConsumer);
        repository.save();
        log.info("Saved state");
        log.info("deactivated " + MODULE_NAME);
    }

    private void tick() {
        if (autoSaveTimer.hasExpired()) {
            autoSaveTimer.restart();
            repository.save();
        }
    }
}
