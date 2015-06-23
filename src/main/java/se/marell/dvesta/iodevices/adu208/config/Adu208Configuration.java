/*
 * Created by Daniel Marell 2011-09-18 19:10
 */
package se.marell.dvesta.iodevices.adu208.config;

import java.util.List;

public class Adu208Configuration {
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

        @Override
        public String toString() {
            return "BitIO{" +
                    "logicalName='" + logicalName + '\'' +
                    ", deviceNumber=" + deviceNumber +
                    ", bitno=" + bitno +
                    ", invert=" + invert +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    private int deviceNumber;
    private List<BitIO> inputs;
    private List<BitIO> outputs;

    public Adu208Configuration(int deviceNumber, List<BitIO> inputs, List<BitIO> outputs) {
        this.deviceNumber = deviceNumber;
        this.inputs = inputs;
        this.outputs = outputs;
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

    @Override
    public String toString() {
        return "Adu208Configuration{" +
                "deviceNumber=" + deviceNumber +
                ", inputs=" + inputs +
                ", outputs=" + outputs +
                '}';
    }
}
