/*
 * Created by Daniel Marell 14-03-02 12:02
 */
package se.marell.dvesta.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemController {
    @Autowired
    private Environment environment;

    @RequestMapping(value = "/appversion", method = RequestMethod.GET)
    public String getAppVersion() {
        return BuildInfo.getAppVersion();
    }

    @RequestMapping(value = "/runenvironment", method = RequestMethod.GET)
    public String getRunEnvironment() {
        return RunEnvironment.getCurrentEnvironment(environment).toString();
    }
}