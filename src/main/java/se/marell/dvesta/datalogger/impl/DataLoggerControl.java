/*
 * Created by Daniel Marell 14-01-19 13:56
 */
package se.marell.dvesta.datalogger.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import se.marell.dvesta.datalogger.*;
import se.marell.dvesta.tickengine.NamedTickConsumer;
import se.marell.dvesta.tickengine.TickEngine;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataLoggerControl implements DataLogger, ServletContextListener {
    private static final String MODULE_NAME = DataLoggerControl.class.getSimpleName();
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private NamedTickConsumer tickConsumer;

    @Autowired
    private Environment environment;

    @Autowired
    private TickEngine tickEngine;

    private Map<Integer, DoubleData> doubleVariables = new HashMap<Integer, DoubleData>();
    private Map<Integer, IntData> intVariables = new HashMap<Integer, IntData>();
    private Map<Integer, BooleanData> booleanVariables = new HashMap<Integer, BooleanData>();
    private int nextDataItemId = 0;
    private boolean variableAdded;

    private LogFileWriter writer;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("Starting " + MODULE_NAME);
        String outputDirectory = environment.getProperty("dvesta.dataloggerOutputDirectory");
        writer = new LogFileWriter(new File(outputDirectory));
        tickConsumer = new NamedTickConsumer(MODULE_NAME, this::executeTick, tickEngine, 1, 10, 1);
        log.info("Started " + MODULE_NAME);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("deactivating " + MODULE_NAME);
        tickEngine.removeTickConsumer(tickConsumer);
        log.info("deactivated " + MODULE_NAME);
    }

    @Override
    public synchronized DataItemDescriptor registerDoubleDataItem(String name, String description, int decimalPlaces) {
        variableAdded = true;
        int id = nextDataItemId++;
        doubleVariables.put(id, new DoubleData(id, name, description, decimalPlaces));
        return new DataItemDescriptor(id, name, description);
    }

    @Override
    public synchronized DataItemDescriptor registerIntegerDataItem(String name, String description) {
        variableAdded = true;
        int id = nextDataItemId++;
        intVariables.put(id, new IntData(id, name, description));
        return new DataItemDescriptor(id, name, description);
    }

    @Override
    public synchronized DataItemDescriptor registerBooleanDataItem(String name, String description) {
        variableAdded = true;
        int id = nextDataItemId++;
        booleanVariables.put(id, new BooleanData(id, name, description));
        return new DataItemDescriptor(id, name, description);
    }

    @Override
    public synchronized void logDoubleValue(DataItemDescriptor descriptor, double value) {
        DoubleData variable = doubleVariables.get(descriptor.getDataItemId());
        if (variable == null) {
            throw new IllegalArgumentException("Unknown descriptor " + descriptor);
        }
        variable.setValue(value);
    }

    @Override
    public synchronized void logIntegerValue(DataItemDescriptor descriptor, int value) {
        IntData variable = intVariables.get(descriptor.getDataItemId());
        if (variable == null) {
            throw new IllegalArgumentException("Unknown descriptor " + descriptor);
        }
        variable.setValue(value);
    }

    @Override
    public synchronized void logBooleanValue(DataItemDescriptor descriptor, boolean value) {
        BooleanData variable = booleanVariables.get(descriptor.getDataItemId());
        if (variable == null) {
            throw new IllegalArgumentException("Unknown descriptor " + descriptor);
        }
        variable.setValue(value);
    }

    /**
     * Get modified AbstractData objects. Clear the modification flag.
     *
     * @return Modified AbstractData objects
     */
    @NotNull
    public synchronized List<AbstractData> getModifiedVariablesAndClearFlag() {
        List<AbstractData> modifiedVariables = new ArrayList<AbstractData>();
        List<AbstractData> allVariables = new ArrayList<AbstractData>();
        allVariables.addAll(doubleVariables.values());
        allVariables.addAll(intVariables.values());
        allVariables.addAll(booleanVariables.values());
        for (AbstractData var : allVariables) {
            if (var.isModified()) {
                var.setModified(false);
                modifiedVariables.add(var);
            }
        }
        return modifiedVariables;
    }

    public synchronized void executeTick() {
        List<AbstractData> modifiedVariables = getModifiedVariablesAndClearFlag();
        if (modifiedVariables.size() > 0) {
            log.trace("Found modified variables:" + modifiedVariables.size());
            if (variableAdded) {
                List<AbstractData> allVariables = new ArrayList<AbstractData>();
                allVariables.addAll(doubleVariables.values());
                allVariables.addAll(intVariables.values());
                allVariables.addAll(booleanVariables.values());
                writer.setAllVariables(allVariables);
                variableAdded = false;
            }
            writer.writeToFile(modifiedVariables);
        }
    }
}
