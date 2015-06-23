/*
 * Created by daniel 2003-sep-03 23:00:29
 */
package se.marell.dvesta.datalogger.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.marell.dvesta.datalogger.AbstractData;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Reads buffered logger data and write to files. One file for each date on the form
 * yyyyddmm.log.
 */
public class LogFileWriter {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private PrintWriter out;
    private String filename;
    private File logFileDirectory;
    private List<AbstractData> allVariables = new ArrayList<AbstractData>();
    private boolean variblesAdded;

    public LogFileWriter(File logFileDirectory) {
        this.logFileDirectory = logFileDirectory;
    }

    public void writeToFile(List<AbstractData> variables) {
        long timestamp = System.currentTimeMillis();
        if (openLogFile(timestamp)) {
            out.print(timestamp);
            out.print("\t");
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < variables.size(); j++) {
                if (j > 0) {
                    sb.append("\t");
                }
                AbstractData dv = variables.get(j);
                sb.append(dv.getDataItemId());
                sb.append(":");
                sb.append(dv.toString());
            }
            out.println(sb.toString());
            out.flush();
            log.trace("wrote " + variables.size() + " variables");
        }
    }

    private String getFilename(long timestamp) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(timestamp);
        return String.format("%02d", c.get(Calendar.YEAR)) +
                String.format("%02d", (c.get(Calendar.MONTH) + 1)) +
                String.format("%02d", (c.get(Calendar.DAY_OF_MONTH))) +
                ".log";
    }

    private boolean openLogFile(long timestamp) {
        String newFilename = getFilename(timestamp);
        if (newFilename.equals(filename)) {
            if (variblesAdded) {
                writeMapping();
                variblesAdded = false;
            }
        } else {
            if (out != null) {
                out.close();
                out = null;
            }
            // Open/create file
            try {
                if (!logFileDirectory.exists()) {
                    logFileDirectory.mkdirs();
                    log.info("Created logFileDirectory " + logFileDirectory);
                }
                out = new PrintWriter(new BufferedWriter(new FileWriter(new File(logFileDirectory, newFilename), true)));
                filename = newFilename;
                log.debug("opened log file " + newFilename);
                writeMapping();
            } catch (IOException e) {
                log.error("Can't open/create log file:" + e.getMessage());
            }
        }
        return out != null;
    }

    private void writeMapping() {
        assert out != null;
        out.println();
        for (AbstractData nv : allVariables) {
            out.println("mapping\t" + nv.getDataItemId() + ":" + nv.getName() + ":" + nv.getDescription());
        }
        out.println();
        out.flush();
        log.trace("wrote mapping");
    }

    public void setAllVariables(List<AbstractData> allVariables) {
        this.allVariables = allVariables;
        variblesAdded = true;
    }
}
