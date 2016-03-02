/*
 * Created by Daniel Marell 14-11-23 18:04
 */
package se.marell.dvesta.iodevices.razberry;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import se.marell.dvesta.iodevices.razberry.impl.RazberryClient;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.alarmsensor.ZWayAlarmSensorReply;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.basic.ZWayBasicReply;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.data.ZWayDataReply;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation.ZAutomationDevicesReply;

import java.util.concurrent.Future;

public class RazberryTest {
    public static void main(String[] args) throws Exception {
//        testDevices();
//        testAlarmSensor();
//        testAlarmSensor2();
        testData();
    }

    private static void testDevices() throws InterruptedException, java.util.concurrent.ExecutionException {
        RazberryClient r = new RazberryClient();
        String uri = "http://rpi3:8083";

        System.out.println("Sending getDevices");
        Future<ResponseEntity<ZAutomationDevicesReply>> response = r.getDevices(uri);
        System.out.println("reply=" + response.get().getBody());

        System.out.println("Sending setSwitch on");
        String deviceId = "ZWayVDev_7:0:37";
        r.setSwitch(uri, deviceId, "off");
        Thread.sleep(1000);
        System.out.println("Sending setSwitch off");
        r.setSwitch(uri, deviceId, "on");
    }

    private static void testAlarmSensor() throws InterruptedException, java.util.concurrent.ExecutionException {
        RazberryClient r = new RazberryClient();
        String uri = "http://rpi2:8083";
        final int deviceNumber = 6;
        System.out.println("Sending getAlarmSensor1");
        Future<ResponseEntity<ZWayAlarmSensorReply>> response = r.getAlarmSensor(uri, deviceNumber);
        ZWayAlarmSensorReply reply = response.get().getBody();
        System.out.println("response: " + reply);
        System.out.println("sensorState, deviceNumber: " + deviceNumber + ": " +
                reply.getData().getDataMap().get(0).getSensorState().getValue());
    }

    private static void testAlarmSensor2() throws InterruptedException, java.util.concurrent.ExecutionException {
        String uri = "http://rpi2:8083";
        final int deviceNumber = 6;
        {
            System.out.println("Sending getAlarmSensor1");
            ResponseEntity<ZWayAlarmSensorReply> response = getAlarmSensor1(uri, deviceNumber);
            ZWayAlarmSensorReply reply = response.getBody();
            System.out.println("response: " + reply);
            System.out.println("sensorState, deviceNumber: " + deviceNumber + ": " +
                    reply.getData().getDataMap().get(0).getSensorState().getValue());
        }
        {
            System.out.println("Sending getBasicLevel deviceNumber: " + deviceNumber);
            ResponseEntity<ZWayBasicReply> response = getBasicLevel(uri, deviceNumber);
            ZWayBasicReply reply = response.getBody();
            System.out.println("response: " + reply);
            System.out.println("value: " + reply.getData().getValue());
            System.out.println("level.value: " + reply.getData().getLevel().getValue());
        }
    }

    public static ResponseEntity<ZWayAlarmSensorReply> getAlarmSensor1(String uri, int deviceNumber) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUri = uri + "/ZWaveAPI/Run/devices[{deviceNumber}].instances[0].commandClasses[156]";
        return restTemplate.getForEntity(requestUri, ZWayAlarmSensorReply.class, deviceNumber);
    }

    public static ResponseEntity<ZWayBasicReply> getBasicLevel(String uri, int deviceNumber) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUri = uri + "/ZWaveAPI/Run/devices[{deviceNumber}].instances[0].commandClasses[32]";
        return restTemplate.getForEntity(requestUri, ZWayBasicReply.class, deviceNumber);
    }

    private static void testData() throws InterruptedException, java.util.concurrent.ExecutionException {
        String uri = "http://rpi2:8083";
        final int since = 1434825385;
        System.out.println("Sending getDataSince, since: " + since);
        ResponseEntity<ZWayDataReply> response = getDataSince(uri, since);
        ZWayDataReply reply = response.getBody();
        System.out.println("response: " + reply);
    }

    public static ResponseEntity<ZWayDataReply> getDataSince(String uri, long since) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUri = uri + "/ZWaveAPI/Data/{since}";
        return restTemplate.getForEntity(requestUri, ZWayDataReply.class, since);
    }
}
