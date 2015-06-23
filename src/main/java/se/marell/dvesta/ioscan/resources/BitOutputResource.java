/*
 * Created by Daniel Marell 2011-11-15 00:06
 */
package se.marell.dvesta.ioscan.resources;

import org.springframework.web.bind.annotation.*;
import se.marell.dvesta.ioscan.BitOutput;
import se.marell.dvesta.ioscan.BitPost;

@RestController
public class BitOutputResource extends AbstractResource {
    @RequestMapping(value = "/io/bitoutputs/{logicalName}", method = RequestMethod.GET)
    public BitOutput getBitOutput(@PathVariable("logicalName") String logicalName) {
        log.trace("Received getBitOutput,logicalName=" + logicalName);
        BitOutput out = getIoMapper().findBitOutput(logicalName);
        if (out == null) {
            throw new NotFoundException(logicalName);
        }
        return out;
    }

    @RequestMapping(value = "/io/bitoutputs/{logicalName}", method = RequestMethod.POST)
    public void setBitOutput(@PathVariable("logicalName") String logicalName, @RequestBody BitPost command) {
        log.trace("Received setBitOutput,logicalName=" + logicalName + ",command=" + command);
        BitOutput out = getIoMapper().findBitOutput(logicalName);
        if (out == null) {
            throw new NotFoundException(logicalName);
        }
        out.setOutputStatus(command.getStatus());
        out.setOverrideEnable(command.getOverrideEnable());
        out.setOverrideStatus(command.getOverrideStatus());
    }
}

