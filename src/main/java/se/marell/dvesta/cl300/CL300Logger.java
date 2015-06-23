/*
 * Created by daniel Mar 17, 2002 4:11:12 PM
 */
package se.marell.dvesta.cl300;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.marell.dvesta.datalogger.DataItemDescriptor;
import se.marell.dvesta.datalogger.DataLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Uses the CL300Fetcher to read parameters and input them to the DataLogger
 * <p/>
 * Convert value to degrees celsius: (value - 1962) / 18.31
 */
public class CL300Logger {
    private static final double TEMP_OFFSET = 1962;
    private static final double TEMP_FACTOR = 18.31;

    enum Parameter {
        hotWaterTemp(0, "Varmvattemtemperatur", true),
        returnTemp(1, "Returtemperatur", true),
        hotGasTemp(2, "Hetgastemperatur", true),
        outdoorTemp(3, "Utetemperatur", true),
        roomTemp(4, "Rumstemperatur", true),
        setValueHotWater(5, "Inställt värde varmvatten", false),
        setValueParallellTemp(6, "Parallelltemperatur (vred)", false),
        setValueRoomHysteresis(7, "Rumshysteres (pot)", false),
        setValueHysteresisCurve(8, "Hystereskurva (pot)", false),
        setPoint(9, "SP (Set Point)", false),
        lowerLimit(10, "LL (Lower Limit)", false),
        lowerLimitAuxEnergy(11, "LL Tillskott", false),
        upperLimitTemp(12, "UL (Upper Limit) temperatur", false),
        upperLimitAuxEnergy(13, "UL Tillskott", false),
        refValue(14, "Referensvärde - beror pÂ mode 1-3", false),
        restartDelay(15, "Omstart fördröjning", false),
        setValueAuxEnergyTicker(16, "Tillskottstimer (pot)", false),
        auxEnergyActiveTicker(17, "Timer nedräkning (ticker) under tillskott", false),
        alarmTicker(18, "Larmticker - tillskott vid fel", false),
        tickerCheckRadiatorOrHotWater(19, "Ticker (t.ex. 60s) innan mätning övergång radiator-varmvatten", false),
        mode(20, "Mode 1,2 eller 3", false),
        auxEnergyCheckTimer(21, "Tillskott checktimer", false),
        tipHeatTicker(22, "Spetsvärme, ticker", false);

        private int id;
        private String description;
        private boolean isTemperature;

        private Parameter(int id, String description, boolean isTemperature) {
            this.id = id;
            this.description = description;
            this.isTemperature = isTemperature;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public boolean isTemperature() {
            return isTemperature;
        }
    }

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private CL300Fetcher pump;
    private Thread thread;
    private DataLogger logger;
    private boolean stopRequest;
    private Map<Integer, DataItemDescriptor> descriptorMap = new HashMap<Integer, DataItemDescriptor>();
    private String portName;

    public CL300Logger(DataLogger logger, String portName) {
        this.logger = logger;
        this.portName = portName;
        init();
    }

    private void init() {
        log.info("Initializing CL300Fetcher");
        try {
            this.pump = new CL300Fetcher(portName);
        } catch (IOException e) {
            log.error("Initialization failed, portName={}:{}", portName, e.getMessage());
        }

        for (Parameter p : Parameter.values()) {
            DataItemDescriptor d;
            if (p.isTemperature()) {
                d = logger.registerDoubleDataItem(p.name(), p.getDescription(), 1);
            } else {
                d = logger.registerIntegerDataItem(p.name(), p.getDescription());
            }
            descriptorMap.put(d.getDataItemId(), d);
        }
    }

    public void startThread() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                runLogging();
            }
        });
        thread.start();
    }

    public synchronized void requestThreadStop() {
        stopRequest = true;
        notify();
    }

    /**
     * Thread runs here
     */
    private void runLogging() {
        while (true) {
            CL300Fetcher.Record r;
            if (pump == null) {
                init();
            }

            if (pump != null) {
                log.trace("Requesting parameters from CL300");
                pump.requestParameters();

                do {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ignore) {
                    }
                    if (stopRequest) {
                        return;
                    }
                } while ((r = pump.getParameters()) == null);

                log.trace("Got parameters from CL300:" + r.params.length);

                for (Parameter p : Parameter.values()) {
                    if (p.getId() >= 0 && p.getId() < r.params.length) {
                        DataItemDescriptor d = descriptorMap.get(p.getId());
                        if (d != null) {
                            if (p.isTemperature()) {
                                int value = r.params[p.getId()];
                                double tempCelsius = (value - TEMP_OFFSET) / TEMP_FACTOR;
                                logger.logDoubleValue(d, tempCelsius);
                            } else {
                                int value = r.params[p.getId()];
                                logger.logIntegerValue(d, value);
                            }
                        }
                    }
                }
            } else {
                try {
                    Thread.sleep(10 * 60000);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }
}