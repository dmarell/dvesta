/*
 * Created by Daniel Marell Feb 10, 2010 9:45:30 PM
 */
package se.marell.dvesta.ioscan.impl;

import org.jetbrains.annotations.NotNull;
import se.marell.dvesta.ioscan.FloatOutput;
import se.marell.dvesta.ioscan.IoType;

public class IoScanFloatOutput extends IoScanFloatInput implements FloatOutput {
    private static final long serialVersionUID = 1;
    private boolean isTouched;

    public IoScanFloatOutput(@NotNull String name, @NotNull String unit, float value, int numDecimals, float min, float max) {
        super(IoType.ANALOG_OUTPUT, name, unit, value, numDecimals, min, max);
    }

    @Override
    public void setValue(float value) {
        this.value = value;
        isTouched = true;
        setTimestamp(System.currentTimeMillis());
        addSample();
    }

    @Override
    public boolean isTouched() {
        return isTouched;
    }

    @Override
    public void clearTouched() {
        isTouched = false;
    }
}
