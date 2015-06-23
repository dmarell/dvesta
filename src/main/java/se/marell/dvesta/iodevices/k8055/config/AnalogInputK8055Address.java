/*
 * Created by Daniel Marell 2011-10-09 19:05
 */
package se.marell.dvesta.iodevices.k8055.config;

public class AnalogInputK8055Address extends AbstractK8055Address {
    private static final long serialVersionUID = 1;

    public AnalogInputK8055Address(int deviceNumber, int port) {
        super("ai", deviceNumber, port);
    }
}
