/*
 * Created by Daniel Marell 2011-12-10 21:37
 */
package se.marell.dvesta.ioscan.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.marell.dvesta.ioscan.IntegerInput;

import java.util.Collection;

@RestController
public class IntegerInputsResource extends AbstractResource {
    @RequestMapping(value = "/io/integerinputs", method = RequestMethod.GET)
    public Collection<IntegerInput> getIntegerInputs() {
        log.trace("Received getIntegerInputs");
        return getIoMapper().getIntegerInputs();
    }
}
