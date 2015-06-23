/*
 * Created by Daniel Marell 2011-12-10 16:38
 */
package se.marell.dvesta.ioscan.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootResource extends AbstractResource {
    @RequestMapping(value = "/io", method = RequestMethod.GET)
    public String getChildren() {
        return "bitinputs\n" + "bitoutputs\n" +
                "floatinputs\n" + "floatoutputs\n" +
                "integerinputs\n" + "integeroutputs\n";
    }
}
