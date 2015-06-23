/*
 * Created by Daniel Marell 2011-11-13 22:52
 */
package se.marell.dvesta.ioscan.resources;

import org.springframework.web.bind.annotation.*;
import se.marell.dvesta.ioscan.FloatInput;
import se.marell.dvesta.ioscan.FloatPost;

@RestController
public class FloatInputResource extends AbstractResource {
    @RequestMapping(value = "/io/floatinputs/{logicalName}", method = RequestMethod.GET)
    public FloatInput getFloatInput(@PathVariable("logicalName") String logicalName) {
        log.trace("Received getFloatInput,logicalName=" + logicalName);
        FloatInput in = getIoMapper().findFloatInput(logicalName);
        if (in == null) {
            throw new NotFoundException(logicalName);
        }
        return in;
    }

    @RequestMapping(value = "/io/floatinputs/{logicalName}", method = RequestMethod.POST)
    public void setFloatInput(@PathVariable("logicalName") String logicalName, @RequestBody FloatPost command) {
        log.trace("Received setFloatInput,logicalName=" + logicalName + ",command=" + command);
        FloatInput out = getIoMapper().findFloatInput(logicalName);
        if (out == null) {
            throw new NotFoundException(logicalName);
        }
        out.setStatus(System.currentTimeMillis(), command.getValue());
        out.setOverrideEnable(command.isOverrideEnable());
        out.setOverrideValue(command.getOverrideValue());
    }
}

