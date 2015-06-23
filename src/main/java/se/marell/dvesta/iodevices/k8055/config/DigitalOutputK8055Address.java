/**
 * Created by Daniel Marell Dec 16, 2002 10:56:21 PM
 */
package se.marell.dvesta.iodevices.k8055.config;

public class DigitalOutputK8055Address extends AbstractK8055Address {
    private static final long serialVersionUID = 1;

    public DigitalOutputK8055Address(int deviceNumber, int port) {
        super("do", deviceNumber, port);
    }
}