/*
 * Created by Daniel Marell
 */
package se.marell.dvesta.cl300;

import gnu.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.marell.dcommons.time.PassiveTimer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * Handle collection of parameters from a CL300 (control unit for heat pump) through a serial port.
 * <p/>
 * <code>
 * Example:
 * CL300 c = new CL300("/dev/ttyS0");
 * int value = c.getParameter(4, 500);
 * </code>
 */
public class CL300 implements SerialPortEventListener {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private byte firstRecByte;
    private byte secondRecByte;
    private int recLength;
    private boolean recError;

    public CL300(String portName) throws IOException {

        // Get all serial ports in the system
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();

        // Search for portName among those
        while (portList.hasMoreElements()) {
            CommPortIdentifier portId = portList.nextElement();

            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL &&
                    portId.getName().equals(portName)) {  // Found portName

                // Try to open it - wait max 2 secs
                try {
                    serialPort = (SerialPort) portId.open("CL300", 2000);
                } catch (PortInUseException e) {
                    throw new IOException("Failed to open port " + portName);
                }

                // Setup comm parameters
                try {
                    serialPort.setSerialPortParams(19200,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                } catch (UnsupportedCommOperationException e) {
                    throw new IOException("Illegal hard coded port params");
                }

                // Init inputStream
                try {
                    inputStream = serialPort.getInputStream();
                } catch (IOException e) {
                    throw new IOException("Failed to get inputStream");
                }

                // Init outputStream
                try {
                    outputStream = serialPort.getOutputStream();
                } catch (IOException e) {
                    throw new IOException("Failed to get outputStream");
                }

                // Set ourselves to listen for port events
                try {
                    serialPort.addEventListener(this);
                } catch (TooManyListenersException e) {
                    throw new IOException("Failed to add port listener");
                }

                serialPort.notifyOnDataAvailable(true);
                serialPort.notifyOnBreakInterrupt(true);
                serialPort.notifyOnFramingError(true);
                serialPort.notifyOnOverrunError(true);
                serialPort.notifyOnParityError(true);

                log.info("CL300 init ok");
                return;
            }
        }
        throw new IOException("Port " + portName + " not found");
    }

    public void close() {
        serialPort.close();
    }

    public short getParameter(int paramNo, long timeoutMsec) throws IOException {
        byte[] array = new byte[1];
        array[0] = (byte) paramNo;

        // Reset before each send
        resetRecBuffer();

        try {
            outputStream.write(array);
        } catch (IOException e) {
            // Ignore
        }

        return waitForReply(timeoutMsec);
    }

    private synchronized void resetRecBuffer() {
        recError = false;
        recLength = 0;
    }

    private synchronized short waitForReply(long timeoutMsec) throws IOException {
        PassiveTimer t = new PassiveTimer(timeoutMsec);

        while (recLength < 2 || recError) {
            try {
                wait(timeoutMsec);
            } catch (InterruptedException e) {
                // Ignore
            }

            if (timeoutMsec != 0 && t.hasExpired()) {
                if (recError)
                    throw new IOException("Receive error");

                throw new IOException("Receive timeout");
            }
        }

        return (short) (firstRecByte * 256 + secondRecByte);
    }

    private synchronized void storeReceivedByte(byte b) {
        switch (recLength) {
            case 0:
                firstRecByte = b;
                ++recLength;
                break;

            case 1:
                secondRecByte = b;
                ++recLength;
                break;

            default:
                recError = true;
                log.trace("More than 2 chars received");
                break;
        }
        notifyAll();
    }

    private synchronized void setRecError() {
        recError = true;
        notifyAll();
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
                log.trace("Break detected");
                setRecError();
                break;

            case SerialPortEvent.OE:
                log.trace("Overrun error");
                setRecError();
                break;

            case SerialPortEvent.FE:
                log.trace("Framing error");
                setRecError();
                break;

            case SerialPortEvent.PE:
                log.trace("Parity error");
                setRecError();
                break;

            case SerialPortEvent.DATA_AVAILABLE:
                byte[] readBuffer = new byte[20];

                try {
                    while (inputStream.available() > 0) {
                        int numBytes = inputStream.read(readBuffer);
                        for (int i = 0; i < numBytes; ++i) {
                            storeReceivedByte(readBuffer[i]);
                        }
                    }
                } catch (IOException e) {
                    // Ignore
                }
                break;
        }
    }

    public static void main(String[] args) {
        System.out.println("CL300 test program");

        if (args.length != 1) {
            System.out.println("Specify serial port, e.g. /dev/ttyS0");
            System.exit(0);
        }

        CL300 cl300;
        try {
            cl300 = new CL300(args[0]);
            while (true) {
                for (int i = 1; i <= 23; ++i) {
                    System.out.print(i);
                    try {
                        int v = cl300.getParameter(i, 1000);
                        System.out.println("=" + v);
                    } catch (IOException e) {
                        System.out.println(" Error:" + e.getMessage());
                    }
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // Ignore
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
