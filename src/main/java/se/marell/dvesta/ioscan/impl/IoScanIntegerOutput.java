/*
 * Created by Daniel Marell Feb 10, 2010 9:41:59 PM
 */
package se.marell.dvesta.ioscan.impl;

import org.jetbrains.annotations.NotNull;
import se.marell.dvesta.ioscan.IntegerOutput;
import se.marell.dvesta.ioscan.IoType;

public class IoScanIntegerOutput extends IoScanIntegerInput implements IntegerOutput {
    private static final long serialVersionUID = 1;
    private boolean isTouched;

    public IoScanIntegerOutput(@NotNull String name, @NotNull String unit, int value, int min, int max) {
        super(IoType.INTEGER_OUTPUT, name, unit, value, min, max);
    }

    public void setValue(int value) {
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