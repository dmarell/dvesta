/*
 * Created by Daniel Marell Feb 10, 2010 9:28:03 PM
 */
package se.marell.dvesta.ioscan.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.marell.dvesta.ioscan.IoDevice;
import se.marell.dvesta.ioscan.IoType;

public abstract class AbstractIoDevice implements IoDevice {
    private static final long serialVersionUID = 1;
    private IoType type;
    private String name;
    private String unit;
    private long timestamp;
    private boolean isConnected;
    private String deviceAddress;
    private String deviceAddressDescription = "";

    public AbstractIoDevice(IoType type, String name, String unit) {
        this.type = type;
        this.name = name;
        this.unit = unit;
    }

    @Override
    @NotNull
    public IoType getType() {
        return type;
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }

    @Override
    @NotNull
    public String getUnit() {
        return unit;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    @Nullable
    public String getDeviceAddress() {
        return deviceAddress;
    }

    @Override
    @NotNull
    public String getDeviceAddressDescription() {
        return deviceAddressDescription;
    }

    @Override
    public void setIoStatus(long timestamp, boolean isConnected) {
        this.timestamp = timestamp;
        this.isConnected = isConnected;
    }

    @Override
    public void setDisconnected() {
        this.isConnected = false;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void mapDevice(@NotNull String deviceAddress, @NotNull String deviceAddressDescription) {
        this.deviceAddress = deviceAddress;
        this.deviceAddressDescription = deviceAddressDescription;
    }

    @Override
    public void unmapDevice() {
        this.deviceAddress = null;
        this.deviceAddressDescription = "";
    }

    @Override
    public String toString() {
        return "{" +
                "class: " + this.getClass().getSimpleName() +
                ",ioType: " + type +
                ",name: " + name + ",addr: " + deviceAddress +
                "}";
    }

    protected int getSamplesBufferSize(String name) {
        return 10000; // Todo implement possibility to override default for all and individually depending on name
    }
}
