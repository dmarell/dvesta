/*
 * Created by Daniel Marell 15-06-20 17:42
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.*;

/*
    "devices.6.instances.0.commandClasses.113.data.1.event": {
      "value": 2,
      "type": "int",
      "invalidateTime": 1434705167,
      "updateTime": 1434829759
    },

    "devices.6.instances.0.commandClasses.113.data.1.eventString": {
      "value": "Smoke detected, location unknown",
      "type": "string",
      "invalidateTime": 1434705167,
      "updateTime": 1434829759
    },

    "devices.6.instances.0.commandClasses.113.data.1.eventParameters": {
      "value": [ ],
      "type": "binary",
      "invalidateTime": 1434705167,
      "updateTime": 1434829759
    },
*/
public class ZWayDataReplyDeserializer extends StdDeserializer<ZWayDataReply> {
    public ZWayDataReplyDeserializer() {
        super(ZWayDataReply.class);
    }

    @Override
    public ZWayDataReply deserialize(JsonParser jsonParser, DeserializationContext dc) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        ZWayDataReply deserializedData = new ZWayDataReply();
        Map<Integer, Map<Integer, ZWayAlarm>> alarmMap = new HashMap<>();
        Iterator<String> iter = node.fieldNames();
        while (iter.hasNext()) {
            String fieldName = iter.next(); // For example "devices.6.instances.0.commandClasses.113.data.1.eventString"
            if (fieldName.equals("updateTime")) {
                deserializedData.setUpdateTime(node.asLong());
            } else {
                AlarmKey key = parseAlarmKey(fieldName);
                if (key != null) {
                    JsonNode jsonNode = node.get(fieldName);
                    Map<Integer, ZWayAlarm> dataMap = alarmMap.get(key.getDeviceNumber());
                    if (dataMap == null) {
                        dataMap = new HashMap<>();
                        alarmMap.put(key.getDeviceNumber(), dataMap);
                    }
                    ZWayAlarm alarm = dataMap.get(key.getDataNumber());
                    dataMap.put(key.getDataNumber(), parseAlarmEntry(key.getType(), jsonNode, alarm));
                }
            }
        }
        deserializedData.setAlarmMap(alarmMap);
        return deserializedData;
    }

    private ZWayAlarm parseAlarmEntry(String type, JsonNode node, ZWayAlarm result) {
        if (result == null) {
            result = new ZWayAlarm();
        }
        JsonNode typeNode = node.get("type");
        if (typeNode == null) {
            return null;
        }
        JsonNode valueNode = node.get("value");
        if (valueNode == null) {
            return null;
        }
        if (type.equals("event")) {
            if (!typeNode.asText().equals("int")) {
                return null;
            }
            result.setEventValue(valueNode.asInt());
        } else if (type.equals("eventString")) {
            if (!typeNode.asText().equals("string")) {
                return null;
            }
            result.setEventString(valueNode.asText());
        } else if (type.equals("eventParameters")) {
            List<Integer> eventParameters = new ArrayList<>();
            Iterator<JsonNode> iter = valueNode.iterator();
            while (iter.hasNext()) {
                JsonNode n = iter.next();
                if (n.isInt()) {
                    eventParameters.add(n.asInt());
                }
            }
            result.setEventParameters(eventParameters);
        }
        JsonNode invalidateTimeNode = node.get("invalidateTime");
        if (invalidateTimeNode == null) {
            return null;
        }
        result.setInvalidateTime(invalidateTimeNode.asLong());

        JsonNode updateTimeNode = node.get("updateTime");
        if (updateTimeNode == null) {
            return null;
        }
        result.setUpdateTime(updateTimeNode.asLong());

        return result;
    }

    // Example: "devices.6.instances.0.commandClasses.113.data.1.event"
    // index     0       1 2         3 4              5   6    7 8
    private AlarmKey parseAlarmKey(String fieldName) {
        String[] tokens = fieldName.split("\\.");
        if (tokens.length != 9) {
            return null;
        }
        if (!tokens[0].equals("devices")) {
            return null;
        }
        int deviceNumber = Integer.parseInt(tokens[1]);
        if (!tokens[2].equals("instances")) {
            return null;
        }
        int instanceNumber = Integer.parseInt(tokens[3]);
        if (!tokens[4].equals("commandClasses")) {
            return null;
        }
        if (!tokens[5].equals("113")) {
            return null;
        }
        if (!tokens[6].equals("data")) {
            return null;
        }
        int dataNumberNumber = Integer.parseInt(tokens[7]);
        String type = tokens[8];
        return new AlarmKey(deviceNumber, instanceNumber, dataNumberNumber, type);
    }
}

