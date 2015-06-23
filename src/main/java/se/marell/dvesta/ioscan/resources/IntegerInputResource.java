/*
 * Created by Daniel Marell 2011-12-10 21:41
 */
package se.marell.dvesta.ioscan.resources;

import org.springframework.web.bind.annotation.*;
import se.marell.dvesta.ioscan.IntegerInput;
import se.marell.dvesta.ioscan.IntegerPost;

@RestController
public class IntegerInputResource extends AbstractResource {
    @RequestMapping(value = "/io/integerinputs/{logicalName}", method = RequestMethod.GET)
    public IntegerInput getIntegerInput(@PathVariable("logicalName") String logicalName) {
        log.trace("Received getIntegerInput,logicalName=" + logicalName);
        IntegerInput in = getIoMapper().findIntegerInput(logicalName);
        if (in == null) {
            throw new NotFoundException(logicalName);
        }
        return in;
    }

    @RequestMapping(value = "/io/integerinputs/{logicalName}", method = RequestMethod.POST)
    public void setIntegerInput(@PathVariable("logicalName") String logicalName, @RequestBody IntegerPost command) {
        log.trace("Received setIntegerInput,logicalName=" + logicalName + ",command=" + command);
        IntegerInput out = getIoMapper().findIntegerInput(logicalName);
        if (out == null) {
            throw new NotFoundException(logicalName);
        }
        out.setStatus(System.currentTimeMillis(), false, command.getValue());
        out.setOverrideEnable(command.isOverrideEnable());
        out.setOverrideValue(command.getOverrideValue());
    }
}
