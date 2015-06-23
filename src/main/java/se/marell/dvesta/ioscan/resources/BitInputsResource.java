/*
 * Created by Daniel Marell 2011-11-14 23:41
 */
package se.marell.dvesta.ioscan.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.marell.dvesta.ioscan.BitInput;

import java.util.Collection;

@RestController
public class BitInputsResource extends AbstractResource {
    @RequestMapping(value = "/io/bitinputs", method = RequestMethod.GET)
    public Collection<BitInput> getBitInputs() {
        log.trace("Received getBitInputs");
        return getIoMapper().getBitInputs();
    }
}
