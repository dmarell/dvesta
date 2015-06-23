/*
 * Created by Daniel Marell 14-01-21 21:29
 */
package se.marell.dvesta.iodevices.owfs.config;

import java.util.List;

public class OwfsConfiguration {
//    public static interface BitIO {
//      OwfsDeviceAddress getAddress();
////        String getLogicalName();
////
////        String getDescription();
////
////        String getDeviceAddress();
//
//        boolean readValue() throws IOException;
//
//        void setValue(boolean value) throws IOException;
//    }
//
//    public static interface AnalogInput {
//      OwfsDeviceAddress getAddress();
////        String getLogicalName();
////
////        String getDescription();
////
////        String getDeviceAddress();
////
////        String getUnit();
////
////        int getNumDecimals();
////
////        float getMinValue();
////
////        float getMaxValue();
//
//        double readValue() throws IOException;
//    }


    private String mountPoint;
    private List<DS18B20DeviceAddress> ds18b20Devices;
    private List<DS2450DeviceAddress> ds2450Devices;
    private List<DS2405DeviceAddress> ds2405Devices;

    public OwfsConfiguration(String mountPoint, List<DS18B20DeviceAddress> ds18b20Devices,
                             List<DS2450DeviceAddress> ds2450Devices, List<DS2405DeviceAddress> ds2405Devices) {
        this.mountPoint = mountPoint;
        this.ds18b20Devices = ds18b20Devices;
        this.ds2450Devices = ds2450Devices;
        this.ds2405Devices = ds2405Devices;
    }

    public String getMountPoint() {
        return mountPoint;
    }

    public List<DS18B20DeviceAddress> getDs18b20Devices() {
        return ds18b20Devices;
    }

    public List<DS2450DeviceAddress> getDs2450Devices() {
        return ds2450Devices;
    }

    public List<DS2405DeviceAddress> getDs2405Devices() {
        return ds2405Devices;
    }

//  private Map<String, AnalogInput> analogInputMap = new HashMap<String, AnalogInput>();
//  private Map<String, BitIO> bitIOMap = new HashMap<String, BitIO>();

//  public OwfsConfiguration(String mountPoint, List<AnalogInput> analogInputs, List<BitIO> bitInputInputs) {
//    this.mountPoint = mountPoint;
//    this.analogInputs = analogInputs;
//    this.bitInputInputs = bitInputInputs;
//    for (AnalogInput ai : analogInputs) {
//      analogInputMap.put(ai.getAddress().getLogicalName(), ai);
//    }
//    for (BitIO bi : bitInputInputs) {
//      bitIOMap.put(bi.getAddress().getLogicalName(), bi);
//    }
//  }
//
//  public String getMountPoint() {
//    return mountPoint;
//  }
//
//  public List<AnalogInput> getAnalogInputs() {
//    return analogInputs;
//  }
//
//  public List<BitIO> getBitInputInputs() {
//    return bitInputInputs;
//  }
}
