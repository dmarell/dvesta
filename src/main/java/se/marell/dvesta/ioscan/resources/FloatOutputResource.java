/*
 * Created by Daniel Marell 2011-12-10 21:40
 */
package se.marell.dvesta.ioscan.resources;

import org.springframework.web.bind.annotation.*;
import se.marell.dvesta.ioscan.FloatOutput;
import se.marell.dvesta.ioscan.FloatPost;

@RestController
public class FloatOutputResource extends AbstractResource {
    @RequestMapping(value = "/io/floatoutputs/{logicalName}", method = RequestMethod.GET)
    public FloatOutput getFloatOutput(@PathVariable("logicalName") String logicalName) {
        log.trace("Received getFloatOutput,logicalName=" + logicalName);
        FloatOutput out = getIoMapper().findFloatOutput(logicalName);
        if (out == null) {
            throw new NotFoundException(logicalName);
        }
        return out;
    }

    @RequestMapping(value = "/io/floatoutputs/{logicalName}", method = RequestMethod.POST)
    public void setFloatOutput(@PathVariable("logicalName") String logicalName, @RequestBody FloatPost command) {
        log.trace("Received setFloatOutput,logicalName=" + logicalName + ",command=" + command);
        FloatOutput out = getIoMapper().findFloatOutput(logicalName);
        if (out == null) {
            throw new NotFoundException(logicalName);
        }
        out.setValue(command.getValue());
        out.setOverrideEnable(command.isOverrideEnable());
        out.setOverrideValue(command.getOverrideValue());
    }
}
