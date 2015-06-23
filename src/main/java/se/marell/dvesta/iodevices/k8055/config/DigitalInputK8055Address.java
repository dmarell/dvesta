/**
 * Created by Daniel Marell Dec 16, 2002 10:00:10 PM
 */
package se.marell.dvesta.iodevices.k8055.config;

public class DigitalInputK8055Address extends AbstractK8055Address {
    private static final long serialVersionUID = 1;

    public DigitalInputK8055Address(int deviceNumber, int port) {
        super("di", deviceNumber, port);
    }
}
