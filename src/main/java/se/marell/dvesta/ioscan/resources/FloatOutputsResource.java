/*
 * Created by Daniel Marell 2011-11-15 00:06
 */
package se.marell.dvesta.ioscan.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.marell.dvesta.ioscan.FloatOutput;

import java.util.Collection;

@RestController
public class FloatOutputsResource extends AbstractResource {
    @RequestMapping(value = "/io/floatoutputs", method = RequestMethod.GET)
    public Collection<FloatOutput> getFloatOutputs() {
        log.trace("Received getFloatOutputs");
        return getIoMapper().getFloatOutputs();
    }
}