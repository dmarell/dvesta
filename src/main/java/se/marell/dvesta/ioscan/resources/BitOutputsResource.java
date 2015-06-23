/*
 * Created by Daniel Marell 2011-11-15 00:06
 */
package se.marell.dvesta.ioscan.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.marell.dvesta.ioscan.BitOutput;

import java.util.Collection;

@RestController
public class BitOutputsResource extends AbstractResource {
    @RequestMapping(value = "/io/bitoutputs", method = RequestMethod.GET)
    public Collection<BitOutput> getBitOutputs() {
        log.trace("Received getBitOutputs");
        return getIoMapper().getBitOutputs();
    }
}