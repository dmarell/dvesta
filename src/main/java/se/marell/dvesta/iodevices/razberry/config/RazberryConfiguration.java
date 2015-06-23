/*
 * Created by Daniel Marell 2011-09-18 22:57
 */
package se.marell.dvesta.iodevices.razberry.config;

import java.util.List;

public class RazberryConfiguration {
    public static class BitIO {
        private String logicalName;
        private String controllerUrl;
        private String deviceId;
        private boolean invert;
        private String description;

        public BitIO(String logicalName, String controllerUrl, String deviceId, boolean invert, String description) {
            this.logicalName = logicalName;
            this.controllerUrl = controllerUrl;
            this.deviceId = deviceId;
            this.invert = invert;
            this.description = description;
        }

        public String getLogicalName() {
            return logicalName;
        }

        public String getControllerUrl() {
            return controllerUrl;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public boolean isInvert() {
            return invert;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class AnalogIO {
        private String logicalName;
        private String controllerUrl;
        private String deviceId;
        private String description;
        private String unit;
        private int numDecimals;
        private float min;
        private float max;
        private boolean convertPercentFactor;

        public AnalogIO(String logicalName, String controllerUrl, String deviceId, String description,
                        String unit, int numDecimals, float min, float max) {
            this.logicalName = logicalName;
            this.controllerUrl = controllerUrl;
            this.deviceId = deviceId;
            this.description = description;
            this.unit = unit;
            this.numDecimals = numDecimals;
            this.min = min;
            this.max = max;
        }

        public AnalogIO(String logicalName, String controllerUrl, String deviceId, String description,
                        String unit, int numDecimals, float min, float max, boolean convertPercentFactor) {
            this.logicalName = logicalName;
            this.controllerUrl = controllerUrl;
            this.deviceId = deviceId;
            this.description = description;
            this.unit = unit;
            this.numDecimals = numDecimals;
            this.min = min;
            this.max = max;
            this.convertPercentFactor = convertPercentFactor;
        }

        public String getLogicalName() {
            return logicalName;
        }

        public String getControllerUrl() {
            return controllerUrl;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public String getDescription() {
            return description;
        }

        public String getUnit() {
            return unit;
        }

        public int getNumDecimals() {
            return numDecimals;
        }

        public float getMin() {
            return min;
        }

        public float getMax() {
            return max;
        }

        public boolean isConvertPercentFactor() {
            return convertPercentFactor;
        }
    }

    public static class AlarmInput {
        private String logicalName;
        private String controllerUrl;
        private String deviceId;
        private int deviceNumber;
        private int instanceNumber;
        private String description;

        public AlarmInput(String logicalName, String controllerUrl, String deviceId, int deviceNumber, int instanceNumber, String description) {
            this.logicalName = logicalName;
            this.controllerUrl = controllerUrl;
            this.deviceId = deviceId;
            this.deviceNumber = deviceNumber;
            this.instanceNumber = instanceNumber;
            this.description = description;
        }

        public String getLogicalName() {
            return logicalName;
        }

        public String getControllerUrl() {
            return controllerUrl;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public int getDeviceNumber() {
            return deviceNumber;
        }

        public int getInstanceNumber() {
            return instanceNumber;
        }

        public String getDescription() {
            return description;
        }
    }


    private String controllerUrl;
    private List<BitIO> inputs;
    private List<BitIO> outputs;
    private List<AnalogIO> analogInputs;
    private List<AnalogIO> analogOutputs;
    private List<AlarmInput> alarmInputs;

    public RazberryConfiguration(String controllerUrl, List<BitIO> inputs, List<BitIO> outputs,
                                 List<AnalogIO> analogInputs, List<AnalogIO> analogOutputs, List<AlarmInput> alarmInputs) {
        this.controllerUrl = controllerUrl;
        this.inputs = inputs;
        this.outputs = outputs;
        this.analogInputs = analogInputs;
        this.analogOutputs = analogOutputs;
        this.alarmInputs = alarmInputs;
    }

    public String getControllerUrl() {
        return controllerUrl;
    }

    public List<BitIO> getInputs() {
        return inputs;
    }

    public List<BitIO> getOutputs() {
        return outputs;
    }

    public List<AnalogIO> getAnalogInputs() {
        return analogInputs;
    }

    public List<AnalogIO> getAnalogOutputs() {
        return analogOutputs;
    }

    public List<AlarmInput> getAlarmInputs() {
        return alarmInputs;
    }
}

