/*
 * Created by Daniel Marell 2011-09-18 22:57
 */
package se.marell.dvesta.iodevices.k8055.config;

import java.util.List;

public class K8055Configuration {
    public static class BitIO {
        private String logicalName;
        private int deviceNumber;
        private int bitno;
        private boolean invert;
        private String description;

        public BitIO(String logicalName, int deviceNumber, int bitno, boolean invert, String description) {
            this.logicalName = logicalName;
            this.deviceNumber = deviceNumber;
            this.bitno = bitno;
            this.invert = invert;
            this.description = description;
        }

        public String getLogicalName() {
            return logicalName;
        }

        public int getDeviceNumber() {
            return deviceNumber;
        }

        public int getBitno() {
            return bitno;
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
        private int deviceNumber;
        private int portno;
        private String description;

        public AnalogIO(String logicalName, int deviceNumber, int portno, String description) {
            this.logicalName = logicalName;
            this.deviceNumber = deviceNumber;
            this.portno = portno;
            this.description = description;
        }

        public String getLogicalName() {
            return logicalName;
        }

        public int getDeviceNumber() {
            return deviceNumber;
        }

        public int getPortno() {
            return portno;
        }

        public String getDescription() {
            return description;
        }
    }

    private int deviceNumber;
    private List<BitIO> inputs;
    private List<BitIO> outputs;
    private List<AnalogIO> analogInputs;
    private List<AnalogIO> analogOutputs;

    public K8055Configuration(int deviceNumber, List<BitIO> inputs, List<BitIO> outputs,
                              List<AnalogIO> analogInputs, List<AnalogIO> analogOutputs) {
        this.deviceNumber = deviceNumber;
        this.inputs = inputs;
        this.outputs = outputs;
        this.analogInputs = analogInputs;
        this.analogOutputs = analogOutputs;
    }

    public int getDeviceNumber() {
        return deviceNumber;
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
}

