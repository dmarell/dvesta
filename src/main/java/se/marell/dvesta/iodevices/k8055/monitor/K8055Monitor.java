/*
 * Created by Daniel Marell 26/02/16.
 */
package se.marell.dvesta.iodevices.k8055.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.marell.dvesta.iodevices.k8055.impl.K8055IoController;
import se.marell.dvesta.iodevices.k8055.impl.K8055Status;

@Service
public class K8055Monitor {
    @Autowired
    private K8055IoController controller;

    public K8055Status getStatus(long since) {
        return controller.getStatus(since);
    }

    public int getNumUnits() {
        return controller.getNumUnits();
    }
}
