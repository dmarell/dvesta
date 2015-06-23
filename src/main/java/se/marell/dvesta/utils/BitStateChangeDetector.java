/*
 * Created by Daniel Marell 14-12-14 11:03
 */
package se.marell.dvesta.utils;

public class BitStateChangeDetector {
    private boolean prevStatus;
    private boolean alwaysReportFirstTime = true;
    private boolean firstCall = true;

    public BitStateChangeDetector() {
    }

    public BitStateChangeDetector(boolean alwaysReportFirstTime) {
        this.alwaysReportFirstTime = alwaysReportFirstTime;
    }

    public boolean hasChanged(boolean status) {
        if (firstCall) {
            if (alwaysReportFirstTime) {
                prevStatus = !status;
            } else {
                prevStatus = status;
            }
            firstCall = false;
        }

        if (status != prevStatus) {
            prevStatus = status;
            return true;
        }
        return false;
    }
}
