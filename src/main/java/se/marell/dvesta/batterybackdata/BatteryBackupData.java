/*
 * Created by daniel Mar 3, 2002 9:22:59 PM
 */
package se.marell.dvesta.batterybackdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BatteryBackupData {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private String name;

    public BatteryBackupData() {
    }

    public BatteryBackupData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void setValue(String s);
}
