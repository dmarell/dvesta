package se.marell.dvesta.iodevices.k8055.slackmonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.marell.dcommons.time.PassiveTimer;
import se.marell.dvesta.iodevices.k8055.impl.K8055Status;
import se.marell.dvesta.iodevices.k8055.monitor.K8055Monitor;
import se.marell.dvesta.slack.SlackConnection;
import se.marell.dvesta.tickengine.NamedTickConsumer;
import se.marell.dvesta.tickengine.TickEngine;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;

/**
 * Report errors for K8055 units on Slack and log.
 * Suppress errors a few seconds in order to avoid to report short errors that immediately heals.
 */
@Component
public class K8055MonitorControl implements ServletContextListener {
    private static final String MODULE_NAME = K8055MonitorControl.class.getSimpleName();
    private static final long INITIAL_TIME_SUPPRESSING_ERRORS = 10000;
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private K8055Monitor monitor;
    private long statusTimestamp;
    private boolean hasError;

    @Autowired
    private SlackConnection slackConnection;

    @Autowired
    private TickEngine tickEngine;

    private NamedTickConsumer tickConsumer;

    private String getName() {
        return this.getClass().getSimpleName();
    }

    private PassiveTimer errorTimer = new PassiveTimer(INITIAL_TIME_SUPPRESSING_ERRORS);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        tickConsumer = new NamedTickConsumer(MODULE_NAME, this::tick, tickEngine, 1, 1, 1);
        log.info("Started " + getName());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("deactivating " + getName());
        tickEngine.removeTickConsumer(tickConsumer);
        log.info("deactivated " + getName());
    }

    private void tick() {
        K8055Status status = monitor.getStatus(statusTimestamp);
        Map<String, String> errorMap = status.getLastError();
        if (!status.hasError() && hasError) {
            String msg = monitor.getNumUnits() == 1 ? "K8055 ok" : "All K8055 units ok";
            slackConnection.sendMessage(msg);
            log.info(msg);
        }
        if (!status.hasError()) {
            // errorTimer starts running when there is one error or more
            errorTimer.restart();
        }
        hasError = status.hasError();

        if (errorTimer.hasExpired()) {
            for (Map.Entry<String, String> e : errorMap.entrySet()) {
                String msg = String.format("Error status for K8055: %s: Reason: %s", e.getKey(), e.getValue());
                slackConnection.sendMessage(msg);
                log.info(msg);
            }
            statusTimestamp = status.getTimestamp();
        }
    }
}
