/*
 * Created by Daniel Marell 14-11-29 12:58
 */
package se.marell.dvesta.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.marell.dvesta.ioscan.*;
import se.marell.dvesta.tickengine.AbstractTickConsumer;
import se.marell.dvesta.tickengine.TickConsumer;
import se.marell.dvesta.tickengine.TickEngine;
import se.marell.dvesta.utils.TimerEvent;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class DemoControl implements ServletContextListener {
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

    private String getName() {
        return "democontrol";
    }

    private TickConsumer tickConsumer = new TickConsumer() {
        @Override
        public String getName() {
            return DemoControl.this.getName();
        }

        @Override
        public void executeTick() {
            tick();
        }
    };

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            daylight = ioMapper.getFloatInput("daylight", "", 0f, 2, 0, 10);
            irHall = ioMapper.getBitInput("irHall", false);
            terraceLights = ioMapper.getBitOutput("terraceLights", false);
        } catch (IoMappingException e) {
            log.error("I/O mapping failed:" + e.getMessage());
        }

        int tickFrequency = tickEngine.findFrequency(1, 10, 1);
        if (tickFrequency == 0) {
            log.info("Failed to find a suitable tick frequency: " + getName());
            return;
        }
        tickEngine.addTickConsumer(tickFrequency, tickConsumer);

        int fastTickFrequency = tickEngine.findFrequency(1, 100, 20);
        if (fastTickFrequency == 0) {
            log.info("Failed to find a suitable fast tick frequency: " + getName());
            return;
        }
        tickEngine.addTickConsumer(fastTickFrequency, new AbstractTickConsumer("DemoControl.fast") {
            @Override
            public void executeTick() {
                fastTick();
            }
        });

        log.info("Started " + getName());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("deactivating " + getName());
        tickEngine.removeTickConsumer(tickConsumer);
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
