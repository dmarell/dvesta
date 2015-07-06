package se.marell.dvesta.cl300;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Retrieves all parameters from the CL300 using the serial port in one operation
 */
public class CL300Fetcher {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private CL300 cl300;
    private static final int nParams = 23;
    private Record buffer;

    public static class Record implements Cloneable {
        public long timestamp;
        public short[] params;

        @Override
        protected Object clone() {
            Record r = new Record();
            r.timestamp = timestamp;
            r.params = new short[params.length];
            for (int i = 0; i < params.length; i++) {
                r.params[i] = params[i];
            }
            return r;
        }
    }

    /**
     * @param portName Serial port name, e.g., /dev/ttyS0
     * @throws IOException if port cannot be opened because is does not exist or no permission or too many listeners error
     */
    public CL300Fetcher(String portName) throws IOException {
        cl300 = new CL300(portName);
    }

    public void requestParameters() {
        Record rec = new Record();

        rec.timestamp = System.currentTimeMillis();

        // Create parameter array and initialize to "no value"
        rec.params = new short[nParams];
        for (int i = 0; i < nParams; ++i) {
            rec.params[i] = -1; // -1 means "no value"
        }

        boolean recError = false;
        int errCnt = 0;
        do {
            for (int i = 0; i < nParams; ++i) {
                try {
                    rec.params[i] = cl300.getParameter(i + 1, 3000); // Timeout 3 sec
                    recError = false;
                    errCnt = 0;
                } catch (IOException e) {
                    recError = true;
                    if (++errCnt > 10 && (errCnt % 10) == 0) {
                        log.info("Repeated error getting param " + (i + 1) + ". Retry " + errCnt + ",error: " + e.getMessage());
                    }
                    log.trace(" Error:" + e.getMessage());
                    break;
                }
            }

            // Let the computer rest a while if that perhaps is a cause to the error
            if (recError) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                }
            }
        } while (recError);

        log.trace("Got record from CL300");

        synchronized (this) {
            buffer = rec;
        }
    }

    /**
     * @return Latest record from CL300 or null if requestParameters() wasn't called
     */
    public Record getParameters() {
        synchronized (this) {
            if (buffer == null) {
                return null;
            }
            Record r = (Record) buffer.clone();
            buffer = null;
            return r;
        }
    }
}
