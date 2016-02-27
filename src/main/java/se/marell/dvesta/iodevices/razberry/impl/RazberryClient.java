/*
 * Created by Daniel Marell 14-11-23 17:12
 */
package se.marell.dvesta.iodevices.razberry.impl;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.alarmsensor.ZWayAlarmSensorReply;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.data.ZWayDataReply;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation.ZAutomationDevicesReply;

import javax.annotation.PostConstruct;
import java.util.concurrent.Future;

@Service
public class RazberryClient {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment environment;

    private RestTemplate restTemplate = new RestTemplate();

    private String username;
    private String password;

    @PostConstruct
    private void activate() {
        username = environment.getProperty("razberry.username");
        password = environment.getProperty("razberry.password");
    }

    @Async
    public void setSwitch(String uri, String deviceId, boolean on) {
        String requestUri = uri + "/ZAutomation/api/v1/devices/{deviceId}/command/{status}";
        restTemplate.exchange(requestUri, HttpMethod.GET, createHttpRequest(), Void.class, deviceId, on ? "on" : "off");
    }

    @Async
    public void setFloatOutput(String uri, String deviceId, float level) {
        String requestUri = uri + "/ZAutomation/api/v1/devices/{deviceId}/command/exact?level={level}";
        restTemplate.exchange(requestUri, HttpMethod.GET, createHttpRequest(), Void.class, deviceId, level);
    }

    @Async
    public Future<ResponseEntity<ZAutomationDevicesReply>> getDevices(String uri, long since) {
        String requestUri = uri + "/ZAutomation/api/v1/devices?since={since}";
        log.debug("request: " + requestUri + ",since: " + since);
        ResponseEntity<ZAutomationDevicesReply> response = restTemplate.exchange(
                requestUri, HttpMethod.GET, createHttpRequest(), ZAutomationDevicesReply.class, since);
        return new AsyncResult<>(response);
    }

    @Async
    public Future<ResponseEntity<ZWayAlarmSensorReply>> getAlarmSensor(String uri, int deviceNumber) {
        String requestUri = uri + "/ZWaveAPI/Run/devices[{deviceNumber}].instances[0].commandClasses[156]";
        log.debug("request: " + requestUri + ",deviceNumber: " + deviceNumber);
        ResponseEntity<ZWayAlarmSensorReply> response = restTemplate.exchange(requestUri, HttpMethod.GET, createHttpRequest(), ZWayAlarmSensorReply.class, deviceNumber);
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
        ResponseEntity<ZWayDataReply> response = restTemplate.exchange(
                requestUri, HttpMethod.GET, createHttpRequest(), ZWayDataReply.class, since);
        return new AsyncResult<>(response);
    }

    private String getCreds() {
        if (username != null) {
            String plainCreds = username + ":" + (password != null ? password : "");
            return new String(Base64.encodeBase64(plainCreds.getBytes()));
        }
        return null;
    }

    private HttpEntity<String> createHttpRequest() {
        HttpHeaders headers = new HttpHeaders();
        String base64Creds = getCreds();
        if (base64Creds != null) {
            headers.add("Authorization", "Basic " + base64Creds);
        }
        return new HttpEntity<>(headers);
    }
}
