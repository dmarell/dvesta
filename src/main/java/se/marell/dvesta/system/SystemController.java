/*
 * Created by Daniel Marell 14-03-02 12:02
 */
package se.marell.dvesta.system;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SystemController {
    @Autowired
    private Environment environment;

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public String getVersion() {
        return BuildInfo.getAppVersion();
    }

    @RequestMapping(value = "/environment", method = RequestMethod.GET)
    public String getRunEnvironment() {
        return RunEnvironment.getCurrentEnvironment(environment).toString();
    }

    @RequestMapping(value = "/log-level/{loggerName}", method = RequestMethod.PUT)
    public String setLogLevel(@PathVariable String loggerName, @RequestParam String logLevel) {
        Logger logger = LoggerFactory.getLogger(loggerName);
        if (logger != null) {
            ch.qos.logback.classic.Logger classicLogger = (ch.qos.logback.classic.Logger) logger;
            String oldLevel = classicLogger.getLevel().toString();
            setLogLevel(classicLogger, logLevel);
            String newLevel = classicLogger.getLevel().toString();
            logger.info("Set log level for logger {} from {} to {}", loggerName, oldLevel, newLevel);
        }
        return getLogLevel(loggerName);
    }

    @RequestMapping(value = "/log-level/{loggerName}", method = RequestMethod.GET)
    public String setLogLevel(@PathVariable String loggerName) {
        return getLogLevel(loggerName);
    }

    private void setLogLevel(ch.qos.logback.classic.Logger logger, String level) {
        if (level.equalsIgnoreCase("trace")) {
            logger.setLevel(Level.TRACE);
        } else if (level.equalsIgnoreCase("debug")) {
            logger.setLevel(Level.DEBUG);
        } else if (level.equalsIgnoreCase("info")) {
            logger.setLevel(Level.INFO);
        } else if (level.equalsIgnoreCase("error")) {
            logger.setLevel(Level.ERROR);
        }
    }

    public String getLogLevel(String loggerName) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerName);
        if (logger != null) {
            return logger.getLevel().toString();
        }
        return null;
    }
}