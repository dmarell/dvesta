/*
 * Created by Daniel Marell 14-11-29 12:58
 */
package se.marell.dvesta.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.marell.dvesta.ioscan.*;
import se.marell.dvesta.tickengine.NamedTickConsumer;
import se.marell.dvesta.tickengine.TickEngine;
import se.marell.dvesta.utils.TimerEvent;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class DemoControl implements ServletContextListener {
    private static final String MODULE_NAME = DemoControl.class.getSimpleName();
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TickEngine tickEngine;

    @Autowired
    private IoMapper ioMapper;

    private FloatInput daylight;
    private BitInput irHall;
    private BitOutput terraceLights;
    private TimerEvent terraceTimerEvent = new TimerEvent(10000, true, new TimerEvent.Listener() {
        @Override
        public void timerStarted() {
            log.info("Tänder terassbelysningen");
            terraceLights.setOutputStatus(true);
        }

        @Override
        public void timerStopped() {
            log.info("Släcker terassbelysningen");
            terraceLights.setOutputStatus(false);
        }
    });
    private NamedTickConsumer tickConsumer;
    private NamedTickConsumer fastTickConsumer;

    private String getName() {
        return "democontrol";
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            daylight = ioMapper.getFloatInput("daylight", "", 0f, 2, 0, 10);
            irHall = ioMapper.getBitInput("irHall", false);
            terraceLights = ioMapper.getBitOutput("terraceLights", false);
        } catch (IoMappingException e) {
            log.error("I/O mapping failed:" + e.getMessage());
        }

        tickConsumer = new NamedTickConsumer(MODULE_NAME, this::tick, tickEngine, 1, 10, 1);
        fastTickConsumer = new NamedTickConsumer(MODULE_NAME + ".fast", this::fastTick, tickEngine, 1, 100, 20);

        log.info("Started " + getName());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("deactivating " + getName());
        tickEngine.removeTickConsumer(tickConsumer);
        tickEngine.removeTickConsumer(fastTickConsumer);
        log.info("deactivated " + getName());
    }

    private void tick() {
//        System.out.printf("daylight: %.2f\n", daylight.getValue());
//        log.info(String.format("daylight: %.2f", daylight.getValue()));
        terraceTimerEvent.dispatch(irHall.getInputStatus());
    }

    private void fastTick() {
        //mainDoorFilterAction.tick();
        //doorBellEntranceFilterAction.tick();
    }
}
