/*
 * Created by Daniel Marell 14-12-16 20:50
 */
package se.marell.dvesta.iodevices.razberry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import se.marell.dcommons.time.TimeSource;
import se.marell.dvesta.iodevices.razberry.impl.RazberryClient;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.data.ZWayAlarm;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.data.ZWayDataReply;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation.ZAutomationDevice;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation.ZAutomationDeviceMetrics;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation.ZAutomationDevicesData;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation.ZAutomationDevicesReply;
import se.marell.dvesta.ioscan.IoScanSpringConfig;
import se.marell.dvesta.tickengine.AbstractTickEngine;
import se.marell.dvesta.tickengine.TickConsumer;
import se.marell.dvesta.tickengine.TickEngine;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import static junit.framework.TestCase.fail;

@Import({
        IoScanSpringConfig.class,
})
@Configuration
@Component
public class TestConfig {
    public static final String Di1_DEVICE_ID = "bit-input-1";
    public static final String Li1_DEVICE_ID = "alarm-input-1";
    public static final int Li1_DEVICE_NUMBER = 6;
    public static final int Li1_INSTANCE_NUMBER = 7;
    public static final int Li1_DATA_NUMBER = 8;
    public static final int CODE = 42;
    public static final int ALARM_VALUE = 43;
    public static final String ALARM_STRING = "Alarm string 43";

    @Autowired
    AutowireCapableBeanFactory beanFactory;

    private TickConsumer preTickConsumer;
    private TickConsumer postTickConsumer;
    private long time;

    private int alarmValue;
    private String alarmString;

    private ZAutomationDevicesReply zAutomationDevicesReply;
    private ZWayDataReply zWayDataReply;

    private void initRazberryClientReplyData() {
        zAutomationDevicesReply = createZAutomationDevicesReply();
        zWayDataReply = createZWayDataReply();
    }

    public static String convertLogicalIdToRazberry(String id) {
        return id + "-rz";
    }

    @Bean
    RazberryClient razberryClient() {
        return new RazberryClient() {
            @Override
            public Future<ResponseEntity<ZAutomationDevicesReply>> getDevices(String uri, long since) {
                return new AsyncResult<>(new ResponseEntity<>(zAutomationDevicesReply, HttpStatus.OK));
            }

            @Override
            public Future<ResponseEntity<ZWayDataReply>> getDataSince(String uri, long since) {
                return new AsyncResult<>(new ResponseEntity<>(zWayDataReply, HttpStatus.OK));
            }
        };
    }

    @NotNull
    private ZAutomationDevicesReply createZAutomationDevicesReply() {
        ZAutomationDevicesReply reply = new ZAutomationDevicesReply();
        reply.setCode(CODE);
        ZAutomationDevicesData devices = new ZAutomationDevicesData(
                true, getTimeInSeconds(),
                new ZAutomationDevice[]{
                        new ZAutomationDevice(
                                convertLogicalIdToRazberry(Di1_DEVICE_ID),
                                new ZAutomationDeviceMetrics("on", "switch", "Test switch 1"),
                                Collections.emptyList(), "", "", getTimeInSeconds())
                });
        reply.setData(devices);
        return reply;
    }

    @NotNull
    private ZWayDataReply createZWayDataReply() {
        long t = getTimeInSeconds();
        ZWayDataReply reply = new ZWayDataReply();
        Map<Integer, ZWayAlarm> dataMap = new HashMap<>();
        dataMap.put(Li1_DATA_NUMBER, new ZWayAlarm(alarmValue, alarmString, Collections.emptyList(), 0, t));
        Map<Integer, Map<Integer, ZWayAlarm>> alarmMap = new HashMap<>();
        alarmMap.put(Li1_DEVICE_NUMBER, dataMap);
        reply.setAlarmMap(alarmMap);
        return reply;
    }

    @Bean
    TickEngine tickEngine() {
        return new AbstractTickEngine() {
            @Override
            public void addTickConsumer(int tickFrequency, @NotNull TickConsumer consumer) {
                fail("Unexpected");
            }

            @Override
            public void addPreTickConsumer(int tickFrequency, @NotNull TickConsumer consumer) {
                preTickConsumer = consumer;
            }

            @Override
            public void addPostTickConsumer(int tickFrequency, @NotNull TickConsumer consumer) {
                postTickConsumer = consumer;
            }
        };
    }

    @Bean
    TimeSource timeSource() {
        return new TimeSource() {
            @Override
            public long currentTimeMillis() {
                return time;
            }

            @Override
            public long nanoTime() {
                return time * 1000000;
            }
        };
    }

    public void setTime(String dateTime) {
        setTime(LocalDateTime.parse(dateTime));
    }

    public void setTime(LocalDateTime dateTime) {
        this.time = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        initRazberryClientReplyData();
    }

    public long getTime() {
        return time;
    }

    public long getTimeInSeconds() {
        return time / 1000;
    }

    public void executeTick() {
        preTickConsumer.executeTick();
        postTickConsumer.executeTick();
    }

    public void executeTick(int count) {
        for (int i = 0; i < count; ++i) {
            executeTick();
        }
    }

    public void setAlarmStatus(int alarmValue, String alarmString) {
        this.alarmValue = alarmValue;
        this.alarmString = alarmString;
        initRazberryClientReplyData();
    }
}
