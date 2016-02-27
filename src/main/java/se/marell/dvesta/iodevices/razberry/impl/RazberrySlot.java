/*
 * Created by Daniel Marell 27/02/16.
 */
package se.marell.dvesta.iodevices.razberry.impl;

import org.springframework.http.ResponseEntity;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.data.ZWayDataReply;
import se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation.ZAutomationDevicesReply;

import java.util.concurrent.Future;

final class RazberrySlot {
    private String razberryUri;

    private Future<ResponseEntity<ZAutomationDevicesReply>> devicesResponse;
    private long updateTimeLastDevicesReply;

    private Future<ResponseEntity<ZWayDataReply>> dataResponse;
    private long updateTimeLastDataReply;

    private Integer lastResponseStatusCode;
    private String lastException;
    private long lastStatusChange;

    public String getRazberryUri() {
        return razberryUri;
    }

    public void setDevicesResponse(Future<ResponseEntity<ZAutomationDevicesReply>> devicesResponse) {
        this.devicesResponse = devicesResponse;
    }

    public Future<ResponseEntity<ZAutomationDevicesReply>> getDevicesResponse() {
        return devicesResponse;
    }

    public long getUpdateTimeLastDevicesReply() {
        return updateTimeLastDevicesReply;
    }

    public void setUpdateTimeLastDevicesReply(long updateTimeLastDevicesReply) {
        this.updateTimeLastDevicesReply = updateTimeLastDevicesReply;
    }

    public void setDataResponse(Future<ResponseEntity<ZWayDataReply>> dataResponse) {
        this.dataResponse = dataResponse;
    }

    public Future<ResponseEntity<ZWayDataReply>> getDataResponse() {
        return dataResponse;
    }

    public long getUpdateTimeLastDataReply() {
        return updateTimeLastDataReply;
    }

    public void setUpdateTimeLastDataReply(long updateTimeLastDataReply) {
        this.updateTimeLastDataReply = updateTimeLastDataReply;
    }

    public synchronized Integer getLastResponseStatusCode() {
        return lastResponseStatusCode;
    }

    public synchronized String getLastException() {
        return lastException;
    }

    public synchronized long getLastStatusChange() {
        return lastStatusChange;
    }

    public synchronized void setValues(Integer lastResponseStatusCode, String lastException) {
        if (getErrorStatus(this.lastResponseStatusCode, this.lastException) != getErrorStatus(lastResponseStatusCode, lastException)) {
            // Update lastStatusChange in transitions between error status and ok status
            this.lastStatusChange = System.currentTimeMillis();
        }

        this.lastResponseStatusCode = lastResponseStatusCode;
        this.lastException = lastException;

//        if ((this.lastResponseStatusCode == null) != (lastResponseStatusCode == null)) {
//            this.lastStatusChange = now;
//        }
//        if (this.lastResponseStatusCode != null && lastResponseStatusCode != null && !this.lastResponseStatusCode.equals(lastResponseStatusCode)) {
//            this.lastStatusChange = now;
//        }
//        this.lastResponseStatusCode = lastResponseStatusCode;

//        if ((this.lastException == null) != (lastException == null)) {
//            this.lastStatusChange = now;
//        }
//        if (this.lastException != null && lastException != null && !this.lastException.equals(lastException)) {
//            this.lastStatusChange = now;
//        }
//        this.lastException = lastException;
    }

    private boolean getErrorStatus(Integer lastResponseStatusCode, String lastException) {
        boolean responseStatusError = lastResponseStatusCode != null && lastResponseStatusCode != 200;
        boolean exceptionError = lastException != null;
        return responseStatusError || exceptionError;
    }

    public RazberrySlot(String razberryUri) {
        this.razberryUri = razberryUri;
    }
}
