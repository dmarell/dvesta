/*
 * Created by Daniel Marell 14-11-23 17:32
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ZAutomationDevicesReply {

    private ZAutomationDevicesData data;
    private int code;
    private String message;
    private Object error;

    public ZAutomationDevicesReply() {
    }

    public ZAutomationDevicesReply(ZAutomationDevicesData data, int code, String message, Object error) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.error = error;
    }

    public ZAutomationDevicesData getData() {
        return data;
    }

    public void setData(ZAutomationDevicesData data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "{" +
                "class: RazberryDevicesReply" +
                ", data: " + data +
                ", code: " + code +
                ", message: '" + message + '\'' +
                ", error: " + error +
                '}';
    }
}
