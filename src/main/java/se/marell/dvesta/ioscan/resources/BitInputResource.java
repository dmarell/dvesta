/*
 * Created by Daniel Marell 2011-11-13 22:52
 */
package se.marell.dvesta.ioscan.resources;

import org.springframework.web.bind.annotation.*;
import se.marell.dvesta.ioscan.BitInput;
import se.marell.dvesta.ioscan.BitPost;

@RestController
public class BitInputResource extends AbstractResource {
    @RequestMapping(value = "/io/bitinputs/{logicalName}", method = RequestMethod.GET)
    public BitInput getBitInput(@PathVariable("logicalName") String logicalName) {
        log.trace("Received getBitInput,logicalName=" + logicalName);
        BitInput in = getIoMapper().findBitInput(logicalName);
        if (in == null) {
            throw new NotFoundException(logicalName);
        }
        return in;
    }

    @RequestMapping(value = "/io/bitinputs/{logicalName}", method = RequestMethod.POST)
    public void setBitInput(@PathVariable("logicalName") String logicalName, @RequestBody BitPost command) {
        log.trace("Received setBitInput,logicalName=" + logicalName + ",command=" + command);
        BitInput in = getIoMapper().findBitInput(logicalName);
        if (in == null) {
            throw new NotFoundException(logicalName);
        }
        in.setInputStatus(System.currentTimeMillis(), command.getStatus());
        in.setOverrideEnable(command.getOverrideEnable());
        in.setOverrideStatus(command.getOverrideStatus());
    }
}
