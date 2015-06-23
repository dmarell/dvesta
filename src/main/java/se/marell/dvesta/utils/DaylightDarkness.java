/*
 * Created by Daniel Marell 14-12-14 11:06
 */
package se.marell.dvesta.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class DaylightDarkness {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private List<DaylightSensorService> daylightSensorServices = new ArrayList<>();

    @PostConstruct
    public void init() {
        for (DaylightSensorService ds : daylightSensorServices) {
            log.info("Found daylight service " + ds.getClass().getSimpleName());
        }
    }

    public String getDaylightServiceName() {
        DaylightSensorService ds = getDaylightService();
        if (ds != null) {
            return ds.getClass().getSimpleName();
        }
        return "undefined";
    }

    public boolean isDark() {
        DaylightSensorService bestDs = getDaylightService();
        if (bestDs != null) {
            return bestDs.isDarkOutdoor();
        }

        // Fallback
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        return hour >= 18 || hour < 9;
    }

    public DaylightSensorService getDaylightService() {
        double maxPrecision = 0;
        DaylightSensorService bestDs = null;
        for (DaylightSensorService ds : daylightSensorServices) {
            if (ds.getPrecision() > maxPrecision) {
                bestDs = ds;
                maxPrecision = ds.getPrecision();
            }
        }
        return bestDs;
    }
}
