package se.marell.dvesta.iodevices.k8055.slackmonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.marell.dvesta.iodevices.k8055.monitor.K8055Monitor;
import se.marell.dvesta.iodevices.k8055.impl.K8055Status;
import se.marell.dvesta.slack.SlackConnection;
import se.marell.dvesta.tickengine.TickConsumer;
import se.marell.dvesta.tickengine.TickEngine;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;

@Component
public class K8055MonitorControl implements ServletContextListener {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private K8055Monitor monitor;
    private long statusTimestamp;
    private boolean hasError;

    @Autowired
    private SlackConnection slackConnection;

    @Autowired
    private TickEngine tickEngine;

    private TickConsumer tickConsumer = new TickConsumer() {
        @Override
        public String getName() {
            return K8055MonitorControl.this.getName();
        }

        @Override
        public void executeTick() {
            tick();
        }
    };

    private String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        int tickFrequency = tickEngine.findFrequency(1, 1, 1);
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
        hasError = status.hasError();
        for (Map.Entry<String, String> e : errorMap.entrySet()) {
            String msg = String.format("Error status for K8055: %s: Reason: %s", e.getKey(), e.getValue());
            slackConnection.sendMessage(msg);
            log.info(msg);
        }
        statusTimestamp = status.getTimestamp();
    }
}
