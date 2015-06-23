/*
 * Created by Daniel Marell 2011-12-10 21:44
 */
package se.marell.dvesta.ioscan.resources;

import org.springframework.web.bind.annotation.*;
import se.marell.dvesta.ioscan.IntegerOutput;
import se.marell.dvesta.ioscan.IntegerPost;

@RestController
public class IntegerOutputResource extends AbstractResource {
    @RequestMapping(value = "/io/integeroutputs/{logicalName}", method = RequestMethod.GET)
    public IntegerOutput getIntegerOutput(@PathVariable("logicalName") String logicalName) {
        log.trace("Received getIntegerOutput,logicalName=" + logicalName);
        IntegerOutput out = getIoMapper().findIntegerOutput(logicalName);
        if (out == null) {
            throw new NotFoundException(logicalName);
        }
        return out;
    }

    @RequestMapping(value = "/io/integeroutputs/{logicalName}", method = RequestMethod.POST)
    public void setIntegerOutput(@PathVariable("logicalName") String logicalName, @RequestBody IntegerPost command) {
        log.trace("Received setIntegerOutput,logicalName=" + logicalName + ",command=" + command);
        IntegerOutput out = getIoMapper().findIntegerOutput(logicalName);
        if (out == null) {
            throw new NotFoundException(logicalName);
        }
        out.setValue(command.getValue());
        out.setOverrideEnable(command.isOverrideEnable());
        out.setOverrideValue(command.getOverrideValue());
    }
}
