package se.marell.dvesta.iodevices.razberry.slackmonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.marell.dvesta.iodevices.razberry.impl.RazberryStatus;
import se.marell.dvesta.iodevices.razberry.monitor.RazberryMonitor;
import se.marell.dvesta.slack.SlackConnection;
import se.marell.dvesta.tickengine.NamedTickConsumer;
import se.marell.dvesta.tickengine.TickEngine;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;

@Component
public class RazberryMonitorControl implements ServletContextListener {
    private static final String MODULE_NAME = RazberryMonitorControl.class.getSimpleName();

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RazberryMonitor monitor;
    private long statusTimestamp;
    private boolean hasError;

    @Autowired
    private SlackConnection slackConnection;

    @Autowired
    private TickEngine tickEngine;

    private NamedTickConsumer tickConsumer;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        tickConsumer = new NamedTickConsumer(MODULE_NAME, this::tick, tickEngine, 1, 1, 1);
        log.info("Started " + MODULE_NAME);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("deactivating " + MODULE_NAME);
        tickEngine.removeTickConsumer(tickConsumer);
        log.info("deactivated " + MODULE_NAME);
    }

    private void tick() {
        RazberryStatus status = monitor.getStatus(statusTimestamp);
        Map<String, String> errorMap = status.getLastError();
        if (!status.hasError() && hasError) {
            String msg = monitor.getNumUnits() == 1 ? "Razberry ok" : "All Razberry units ok";
            slackConnection.sendMessage(msg);
            log.info(msg);
        }
        hasError = status.hasError();
        for (Map.Entry<String, String> e : errorMap.entrySet()) {
            String msg = String.format("Error status for Razberry: %s: Reason: %s", e.getKey(), e.getValue());
            slackConnection.sendMessage(msg);
            log.info(msg);
        }
        statusTimestamp = status.getTimestamp();
    }
}
