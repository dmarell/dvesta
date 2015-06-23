/*
 * Created by Daniel Marell 2011-12-10 21:38
 */
package se.marell.dvesta.ioscan.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.marell.dvesta.ioscan.IntegerOutput;

import java.util.Collection;

@RestController
public class IntegerOutputsResource extends AbstractResource {
    @RequestMapping(value = "/io/integeroutputs", method = RequestMethod.GET)
    public Collection<IntegerOutput> getIntegerOutputs() {
        log.trace("Received getIntegerOutputs");
        return getIoMapper().getIntegerOutputs();
    }
}