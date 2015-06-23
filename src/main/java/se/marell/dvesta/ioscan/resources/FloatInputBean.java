/*
 * Created by Daniel Marell 14-11-28 23:04
 */
package se.marell.dvesta.ioscan.resources;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.marell.dvesta.ioscan.FloatInput;
import se.marell.dvesta.ioscan.IoType;

public class FloatInputBean implements FloatInput {
    private float value;
    private int numDecimals;
    private float overrideValue;
    private float min;
    private float max;
    private IoType type;
    private String name;
    private String unit;
    private long timestamp;
    private boolean connected;
    private String deviceAddress;
    private String deviceAddressDescription;
    private boolean overrideEnable;

    public FloatInputBean(FloatInput f) {
        setValue(f.getValue());
        setNumDecimals(f.getNumDecimals());
        setOverrideValue(f.getOverrideValue());
        setMin(f.getMin());
        setMax(f.getMax());
        setType(f.getType());
        setName(f.getName());
        setUnit(f.getUnit());
        setTimestamp(f.getTimestamp());
        setConnected(f.isConnected());
        setDeviceAddress(f.getDeviceAddress());
        setDeviceAddressDescription(f.getDeviceAddressDescription());
        setOverrideEnable(f.isOverrideEnable());
    }

    @Override
    public String getValueAsString() {
        return Float.toString(value);
    }

    @Override
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public int getNumDecimals() {
        return numDecimals;
    }

    public void setNumDecimals(int numDecimals) {
        this.numDecimals = numDecimals;
    }

    @Override
    public void setStatus(long timestamp, float value) {
    }

    @Override
    public float getOverrideValue() {
        return overrideValue;
    }

    @Override
    public void setOverrideValue(float overrideValue) {
        this.overrideValue = overrideValue;
    }

    @Override
    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    @Override
    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    @NotNull
    @Override
    public IoType getType() {
        return type;
    }

    public void setType(IoType type) {
        this.type = type;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public void setDisconnected() {
    }

    @Nullable
    @Override
    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    @NotNull
    @Override
    public String getDeviceAddressDescription() {
        return deviceAddressDescription;
    }

    public void setDeviceAddressDescription(String deviceAddressDescription) {
        this.deviceAddressDescription = deviceAddressDescription;
    }

    @Override
    public void setIoStatus(long timestamp, boolean isConnected) {
    }

    @Override
    public boolean isOverrideEnable() {
        return overrideEnable;
    }

    @Override
    public void setOverrideEnable(boolean overrideEnable) {
        this.overrideEnable = overrideEnable;
    }

    @Override
    public void mapDevice(@NotNull String deviceAddress, @NotNull String deviceAddressDescription) {
    }

    @Override
    public void unmapDevice() {
    }
}
