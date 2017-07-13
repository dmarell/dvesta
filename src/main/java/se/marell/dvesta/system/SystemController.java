/*
 * Created by Daniel Marell 14-03-02 12:02
 */
package se.marell.dvesta.system;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
class VersionReply {
    private String version;
    private Date deployed;

    public VersionReply(String version, Date deployed) {
        this.version = version;
        this.deployed = deployed;
    }

    public String getVersion() {
        return version;
    }

    public Date getDeployed() {
        return deployed;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class EnvironmentReply {
    private String environment;

    public EnvironmentReply(String environment) {
        this.environment = environment;
    }

    public String getEnvironment() {
        return environment;
    }
}


@JsonInclude(JsonInclude.Include.NON_NULL)
class LogLevelReply {
    private String level;

    public LogLevelReply() {
    }

    public LogLevelReply(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}

@RestController
public class SystemController {
    @Autowired
    private Environment environment;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void getLivenessProbe() {
    }

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public VersionReply getVersion() {
        return new VersionReply(BuildInfo.getAppVersion(), null);
    }

    @RequestMapping(value = "/environment", method = RequestMethod.GET)
    public EnvironmentReply getRunEnvironment() {
        return new EnvironmentReply(RunEnvironment.getCurrentEnvironment(environment).toString());
    }

    @RequestMapping(value = "/log-level/{loggerName}", method = RequestMethod.PUT)
    public LogLevelReply setLogLevel(@PathVariable String loggerName, @RequestParam String logLevel) {
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
    public LogLevelReply setLogLevel(@PathVariable String loggerName) {
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

    public LogLevelReply getLogLevel(String loggerName) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerName);
        if (logger != null && logger.getLevel() != null) {
            return new LogLevelReply(logger.getLevel().toString());
        }
        return new LogLevelReply();
    }
}