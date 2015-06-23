/*
 * Created by Daniel Marell 15-06-21 13:31
 */
package se.marell.dvesta.iodevices.razberry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import se.marell.dvesta.iodevices.razberry.config.RazberryConfiguration;
import se.marell.dvesta.iodevices.razberry.impl.RazberryIoController;
import se.marell.dvesta.ioscan.AlarmInput;
import se.marell.dvesta.ioscan.BitInput;
import se.marell.dvesta.ioscan.BitOutput;
import se.marell.dvesta.ioscan.IoMapper;
import se.marell.dvesta.tickengine.TickEngine;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfig.class)
@WebAppConfiguration
public class RazberryIoControllerTest {
    @Autowired
    AutowireCapableBeanFactory beanFactory;

    @Autowired
    IoMapper ioMapper;

    @Autowired
    TickEngine tickEngine;

    @Autowired
    TestConfig testConfig;

    BitInput bitInput;
    AlarmInput alarmInput;

    @Before
    public void resetIoMapper() {
        for (BitInput b : ioMapper.getBitInputs()) {
            b.setInputStatus(0, false);
            b.setDisconnected();
        }
        for (BitOutput b : ioMapper.getBitOutputs()) {
            b.setInputStatus(0, false);
            b.setDisconnected();
        }
    }

    public void initRazberryIoController(String dateTime) {
        testConfig.setTime(dateTime);
        RazberryIoController c = beanFactory.createBean(RazberryIoController.class);
        c.setConfiguration(new ArrayList<RazberryConfiguration>() {{
            add(createRazberryConfig());
        }});
        bitInput = ioMapper.findBitInput(TestConfig.Di1_DEVICE_ID);
        alarmInput = ioMapper.findAlarmInput(TestConfig.Li1_DEVICE_ID);
    }

    private RazberryConfiguration createRazberryConfig() {
        final String rpiUrl = "foo://rpitest";
        final List<RazberryConfiguration.BitIO> bitInputs = new ArrayList<RazberryConfiguration.BitIO>() {{
            add(new RazberryConfiguration.BitIO(
                    TestConfig.Di1_DEVICE_ID,
                    rpiUrl,
                    TestConfig.convertLogicalIdToRazberry(TestConfig.Di1_DEVICE_ID),
                    false, "Test bitinput 1"));
        }};
        final List<RazberryConfiguration.BitIO> bitOutputs = new ArrayList<RazberryConfiguration.BitIO>() {{
        }};
        final List<RazberryConfiguration.AnalogIO> floatInputs = new ArrayList<RazberryConfiguration.AnalogIO>() {{
        }};
        final List<RazberryConfiguration.AnalogIO> floatOutputs = new ArrayList<RazberryConfiguration.AnalogIO>() {{
        }};
        final List<RazberryConfiguration.AlarmInput> alarmInputs = new ArrayList<RazberryConfiguration.AlarmInput>() {{
            add(new RazberryConfiguration.AlarmInput(
                    TestConfig.Li1_DEVICE_ID,
                    rpiUrl,
                    TestConfig.convertLogicalIdToRazberry(TestConfig.Di1_DEVICE_ID),
                    TestConfig.Li1_DEVICE_NUMBER,
                    TestConfig.Li1_INSTANCE_NUMBER,
                    "Test alarm input 1"));
        }};
        return new RazberryConfiguration(rpiUrl, bitInputs, bitOutputs, floatInputs, floatOutputs, alarmInputs);
    }

    @Test
    public void shouldReadBitInput() throws Exception {
        initRazberryIoController("2015-06-21T15:32:00");
        verifyBitIO(false, false, bitInput);
        testConfig.executeTick(2);
        verifyBitIO(true, true, bitInput);
    }

    private void verifyBitIO(boolean status, boolean connected, BitInput... bitinputs) {
        for (BitInput in : bitinputs) {
            assertThat(in.getName(), in.getInputStatus(), is(status));
            assertThat(in.isConnected(), is(connected));
            if (in.isConnected()) {
                assertThat(in.getTimestamp(), is(testConfig.getTimeInSeconds()));
            }
        }
    }

    @Test
    public void shouldReadAlarmInput() throws Exception {
        initRazberryIoController("2015-06-21T15:32:00");
        verifyAlarmInput(0, null, false, alarmInput);
        testConfig.setAlarmStatus(TestConfig.ALARM_VALUE, TestConfig.ALARM_STRING);
        testConfig.executeTick(2);
        verifyAlarmInput(TestConfig.ALARM_VALUE, TestConfig.ALARM_STRING, true, alarmInput);
        testConfig.setTime("2015-06-21T15:33:00");
        testConfig.setAlarmStatus(0, null);
        testConfig.executeTick(2);
        verifyAlarmInput(0, null, true, alarmInput);
    }

    private void verifyAlarmInput(int expectedAlarmValue, String expectedAlarmString, boolean connected, AlarmInput... inputs) {
        for (AlarmInput in : inputs) {
            assertThat(in.getName(), in.getAlarm(), is(expectedAlarmValue));
            assertThat(in.getName(), in.getAlarmString(), is(expectedAlarmString));
            assertThat(in.isConnected(), is(connected));
            if (in.isConnected()) {
                assertThat(in.getTimestamp(), is(testConfig.getTime()));
            }
        }
    }
}
