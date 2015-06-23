/*
 * Created by Daniel Marell 2011-11-14 23:41
 */
package se.marell.dvesta.ioscan.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.marell.dvesta.ioscan.FloatInput;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FloatInputsResource extends AbstractResource {
    @RequestMapping(value = "/io/floatinputs", method = RequestMethod.GET)
    public FloatInputBean[] getFloatInputs() {
        log.trace("Received getFloatInputs");
        List<FloatInput> a = new ArrayList<>(getIoMapper().getFloatInputs());
        FloatInputBean[] result = new FloatInputBean[a.size()];
        for (int i = 0; i < a.size(); ++i) {
            result[i] = new FloatInputBean(a.get(i));
        }
        return result;
    }
}
