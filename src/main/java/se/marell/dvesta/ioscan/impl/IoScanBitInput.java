/*
 * Created by Daniel Marell Feb 10, 2010 9:32:39 PM
 */
package se.marell.dvesta.ioscan.impl;

import org.jetbrains.annotations.NotNull;
import se.marell.dcommons.util.MutableElementQueue;
import se.marell.dvesta.ioscan.BitInput;
import se.marell.dvesta.ioscan.BitSample;
import se.marell.dvesta.ioscan.IoType;

public class IoScanBitInput extends AbstractIoDevice implements BitInput {
    private static final long serialVersionUID = 1;
    protected boolean inputStatus;
    protected boolean overrideEnable;
    protected boolean overrideStatus;
    protected transient MutableElementQueue<BitSample> samples;

    public IoScanBitInput(@NotNull String name, @NotNull String unit, boolean unmappedStatus) {
        this(IoType.DIGITAL_INPUT, name, unit, unmappedStatus);
    }

    protected IoScanBitInput(IoType type, @NotNull String name, @NotNull String unit, boolean unmappedStatus) {
        super(type, name, unit);
        this.inputStatus = unmappedStatus;
        samples = new MutableElementQueue<BitSample>(
                new BitSample[getSamplesBufferSize(name)],
                new MutableElementQueue.Initializer<BitSample>() {
                    @Override
                    public BitSample create() {
                        return new BitSample();
                    }
                });
    }

    @Override
    public String getValueAsString() {
        return inputStatus ? "on" : "off";
    }

    @Override
    public boolean getInputStatus() {
        return overrideEnable ? overrideStatus : inputStatus;
    }

    @Override
    public synchronized void setInputStatus(long timestamp, boolean inputStatus) {
        super.setIoStatus(timestamp, true);
        this.inputStatus = inputStatus;
        addSample();
    }

    protected void addSample() {
        if (samples.isFull()) {
            samples.getFirst();
        }
        BitSample s = samples.putLast();
        s.timestamp = getTimestamp();
        s.status = inputStatus;
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
    public boolean getOverrideStatus() {
        return overrideStatus;
    }

    @Override
    public void setOverrideStatus(boolean overrideStatus) {
        this.overrideStatus = overrideStatus;
    }

    public MutableElementQueue<BitSample> getSamples() {
        return samples;
    }
}
