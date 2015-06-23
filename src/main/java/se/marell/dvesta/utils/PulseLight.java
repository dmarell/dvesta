/*
 * Created by Daniel Marell 2011-10-10 00:01
 */
package se.marell.dvesta.utils;

import se.marell.dvesta.ioscan.IntegerOutput;

public class PulseLight {
    private IntegerOutput outValue;
    private int ledStepCount;
    private int cycleCount;
    private int cycles;
    private int ledStepDirection;
    private int tickFrequency;

    public PulseLight(IntegerOutput outValue, int tickFrequency, int cycles) {
        this.tickFrequency = tickFrequency;
        this.outValue = outValue;
        start(cycles);
    }

    public void tick() {
        if (cycleCount < cycles) {
            outValue.setValue(ledStepCount * 255 / tickFrequency);
            ledStepCount += ledStepDirection;
            if (ledStepCount >= tickFrequency || ledStepCount <= 0) {
                ledStepDirection = -ledStepDirection;
            }
            if (ledStepCount <= 0) {
                ++cycleCount;
            }
        }
    }

    public void start(int cycles) {
        this.ledStepCount = 0;
        this.ledStepDirection = 1;
        this.cycleCount = 0;
        this.cycles = cycles;
    }
}

