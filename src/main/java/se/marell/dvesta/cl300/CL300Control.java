/*
 * Created by Daniel Marell 2011-09-14 21:53
 */
package se.marell.dvesta.cl300;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import se.marell.dvesta.datalogger.DataLogger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class CL300Control implements ServletContextListener {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment environment;

    @Autowired
    private DataLogger dataLogger;

    private CL300Logger cl300Logger;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String portName = environment.getProperty("dvesta.cl300SerialPort");
        cl300Logger = new CL300Logger(dataLogger, portName);
        cl300Logger.startThread();
        log.info("Started " + getName());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("deactivating " + getName());
        if (cl300Logger != null) {
            cl300Logger.requestThreadStop();
        }
        log.info("deactivated " + getName());
    }

    public String getName() {
        return "cl300control";
    }
}
