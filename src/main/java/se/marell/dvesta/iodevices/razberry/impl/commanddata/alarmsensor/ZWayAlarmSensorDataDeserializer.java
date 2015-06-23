/*
 * Created by Daniel Marell 15-06-20 17:42
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.alarmsensor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
This Deserializer is needed because we want to store the each data item where the json object name is the key
in the map and value in map is the object.

data": {
  "0": {
    "invalidateTime": 1434797140,
    "updateTime": 1434797141,
    "type": "empty",
    "value": null,
    ...
  },
  "1": {
    "invalidateTime": 1434797140,
    ...

*/
public class ZWayAlarmSensorDataDeserializer extends StdDeserializer<ZWayAlarmSensorData> {
    private ObjectMapper mapper = new ObjectMapper();

    public ZWayAlarmSensorDataDeserializer() {
        super(ZWayAlarmSensorData.class);
    }

    @Override
    public ZWayAlarmSensorData deserialize(JsonParser jsonParser, DeserializationContext dc) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        ZWayAlarmSensorData deserializedData = new ZWayAlarmSensorData();
        Map<Integer, ZWayAlarmSensorDataItem> dataMap = new HashMap<>();
        Iterator<String> iter = node.fieldNames();
        while (iter.hasNext()) {
            String fieldName = iter.next();
            if (isNumber(fieldName) && node.get(fieldName).isObject()) {
                JsonNode jsonNode = node.get(fieldName);
                ZWayAlarmSensorDataItem dataItem = mapper.convertValue(jsonNode, ZWayAlarmSensorDataItem.class);
                dataMap.put(Integer.parseInt(fieldName), dataItem);
            }
        }
        deserializedData.setDataMap(dataMap);
        return deserializedData;
    }

    private boolean isNumber(String fieldName) {
        try {
            Integer.parseInt(fieldName);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
