/*
 * Created daniel 2009-dec-31 16:47:49
 */
package se.marell.dvesta.ioscan;

public enum IoType {
    DIGITAL_INPUT("Di"),
    DIGITAL_OUTPUT("Do"),
    ANALOG_INPUT("Ai"),
    ANALOG_OUTPUT("Ao"),
    INTEGER_INPUT("Ii"),
    INTEGER_OUTPUT("Ii"),
    ALARM_INPUT("Li");

    private String name;

    private IoType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static IoType resolve(String s) {
        for (IoType t : values()) {
            if (t.getName().equals(s)) {
                return t;
            }
        }
        return null;
    }
}
