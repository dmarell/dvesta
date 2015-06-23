/*
 * Created by Daniel Marell 2011-10-09 19:10
 */
package se.marell.dvesta.iodevices.k8055.config;

public class AnalogOutputK8055Address extends AbstractK8055Address {
    private static final long serialVersionUID = 1;

    public AnalogOutputK8055Address(int deviceNumber, int port) {
        super("ao", deviceNumber, port);
    }
}