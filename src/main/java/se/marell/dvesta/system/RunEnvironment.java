/*
 * Created by Daniel Marell 14-09-20 00:39
 */
package se.marell.dvesta.system;

import org.springframework.core.env.Environment;

/**
 * Represents a runtime environment, like development, test, staging, production.
 * The environment is set using Spring profiles.
 * Possible values are:
 * local: Local development (default if missing)
 * autosmall: Small/fast automatic tests
 * autolarge: Large/slow automatic tests
 * man: Manual test environment
 * prod: Production environment
 */
public enum RunEnvironment {
    LOCAL("local"),
    AUTOSMALL("autosmall"),
    AUTOLARGE("autolarge"),
    MAN("man"),
    PROD("prod");

    private String name;

    RunEnvironment(String name) {
        this.name = name;
    }

    public static RunEnvironment resolve(String envString) {
        for (RunEnvironment e : RunEnvironment.values()) {
            if (e.toString().toLowerCase().equals(envString.toLowerCase())) {
                return e;
            }
        }
        return null;
    }

    public static RunEnvironment getCurrentEnvironment(Environment environment) {
        for (String profile : environment.getActiveProfiles()) {
            RunEnvironment e = resolve(profile);
            if (e != null) {
                return e;
            }
        }
        return LOCAL;
    }

    @Override
    public String toString() {
        return name;
    }
}