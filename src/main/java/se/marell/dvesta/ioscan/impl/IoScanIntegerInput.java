/*
 * Created by Daniel Marell Feb 10, 2010 9:39:25 PM
 */
package se.marell.dvesta.ioscan.impl;

import org.jetbrains.annotations.NotNull;
import se.marell.dcommons.util.MutableElementQueue;
import se.marell.dvesta.ioscan.IntegerInput;
import se.marell.dvesta.ioscan.IntegerSample;
import se.marell.dvesta.ioscan.IoType;

public class IoScanIntegerInput extends AbstractIoDevice implements IntegerInput {
    private static final long serialVersionUID = 1;
    protected int value;
    protected int min;
    protected int max;
    protected boolean overrideEnable;
    protected int overrideValue;
    protected transient MutableElementQueue<IntegerSample> samples;

    public IoScanIntegerInput(@NotNull String name, @NotNull String unit, int unmappedValue, int min, int max) {
        this(IoType.ANALOG_INPUT, name, unit, unmappedValue, min, max);
    }

    protected IoScanIntegerInput(IoType type, @NotNull String name, @NotNull String unit, int unmappedValue, int min, int max) {
        super(type, name, unit);
        this.value = unmappedValue;
        this.min = min;
        this.max = max;
        samples = new MutableElementQueue<IntegerSample>(
                new IntegerSample[getSamplesBufferSize(name)],
                new MutableElementQueue.Initializer<IntegerSample>() {
                    @Override
                    public IntegerSample create() {
                        return new IntegerSample();
                    }
                });
    }

    @Override
    public String getValueAsString() {
        return Integer.toString(value);
    }

    @Override
    public int getValue() {
        return overrideEnable ? overrideValue : value;
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public synchronized void setStatus(long timestamp, boolean isConnected, int value) {
        super.setIoStatus(timestamp, isConnected);
        this.value = value;
        addSample();
    }

    protected void addSample() {
        if (samples.isFull()) {
            samples.getFirst();
        }
        IntegerSample s = samples.putLast();
        s.timestamp = getTimestamp();
        s.value = value;
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
    public int getOverrideValue() {
        return overrideValue;
    }

    @Override
    public void setOverrideValue(int overrideValue) {
        this.overrideValue = overrideValue;
    }

    public MutableElementQueue<IntegerSample> getSamples() {
        return samples;
    }
}