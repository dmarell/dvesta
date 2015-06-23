/*
 * Created by Daniel Marell 14-12-07 12:30
 */
package se.marell.dvesta.ioscan.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import se.marell.dvesta.ioscan.IoDevice;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class IoDeviceSerializer {
    public static void serializeFields(IoDevice value, JsonGenerator jgen) throws IOException {
        jgen.writeStringField("type", value.getType().name());
        jgen.writeStringField("name", value.getName());
        jgen.writeStringField("unit", value.getUnit());
        if (value.getTimestamp() > 0) {
            Instant instant = Instant.ofEpochMilli(value.getTimestamp());
            String str = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toString();
            jgen.writeStringField("timestamp", str);
        }
        jgen.writeBooleanField("connected", value.isConnected());
        jgen.writeStringField("deviceAddress", value.getDeviceAddress());
        jgen.writeStringField("deviceAddressDescription", value.getDeviceAddressDescription());
        jgen.writeBooleanField("overrideEnable", value.isOverrideEnable());
    }
}
