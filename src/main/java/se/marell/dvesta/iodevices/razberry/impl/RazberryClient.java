/*
 * Created by Daniel Marell 14-11-23 17:12
 */
package se.marell.dvesta.iodevices.razberry.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.alarmsensor.ZWayAlarmSensorReply;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.data.ZWayDataReply;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation.ZAutomationDevicesReply;

import java.util.concurrent.Future;

@Service
public class RazberryClient {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private RestTemplate restTemplate = new RestTemplate();

    @Async
    public void setSwitch(String uri, String deviceId, boolean on) {
        String requestUri = uri + "/ZAutomation/api/v1/devices/{deviceId}/command/{status}";
        restTemplate.getForEntity(requestUri, null, deviceId, on ? "on" : "off");
    }

    @Async
    public void setFloatOutput(String uri, String deviceId, float level) {
        String requestUri = uri + "/ZAutomation/api/v1/devices/{deviceId}/command/exact?level={level}";
        restTemplate.getForEntity(requestUri, null, deviceId, level);
    }

    @Async
    public Future<ResponseEntity<ZAutomationDevicesReply>> getDevices(String uri, long since) {
        String requestUri = uri + "/ZAutomation/api/v1/devices?since={since}";
        log.debug("request: " + requestUri + ",since: " + since);
        ResponseEntity<ZAutomationDevicesReply> response = restTemplate.getForEntity(requestUri, ZAutomationDevicesReply.class, since);
        return new AsyncResult<>(response);
    }

    @Async
    public Future<ResponseEntity<ZWayAlarmSensorReply>> getAlarmSensor(String uri, int deviceNumber) {
        String requestUri = uri + "/ZWaveAPI/Run/devices[{deviceNumber}].instances[0].commandClasses[156]";
        log.debug("request: " + requestUri + ",deviceNumber: " + deviceNumber);
        ResponseEntity<ZWayAlarmSensorReply> response = restTemplate.getForEntity(requestUri, ZWayAlarmSensorReply.class, deviceNumber);
        return new AsyncResult<>(response);
    }

    @Async
    public Future<ResponseEntity<ZAutomationDevicesReply>> getDevices(String uri) {
        return getDevices(uri, 0);
    }

    @Async
    public Future<ResponseEntity<ZWayDataReply>> getDataSince(String uri, long since) {
        String requestUri = uri + "/ZWaveAPI/Data/{since}";
        log.debug("request: " + requestUri + ",since: " + since);
        ResponseEntity<ZWayDataReply> response = restTemplate.getForEntity(requestUri, ZWayDataReply.class, since);
        return new AsyncResult<>(response);
    }

}
