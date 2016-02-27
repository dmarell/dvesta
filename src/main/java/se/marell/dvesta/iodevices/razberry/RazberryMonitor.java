/*
 * Created by Daniel Marell 26/02/16.
 */
package se.marell.dvesta.iodevices.razberry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.marell.dvesta.iodevices.razberry.impl.RazberryIoController;

@Service
public class RazberryMonitor {
    @Autowired
    private RazberryIoController controller;

    public RazberryStatus getStatus(long since) {
        return controller.getStatus(since);
    }
}
