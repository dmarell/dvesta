/*
 * Created by Daniel Marell 14-11-30 12:22
 */
package se.marell.dvesta.iodevices.razberry;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "se.marell.dvesta.iodevices.razberry.config",
        "se.marell.dvesta.iodevices.razberry.impl",
        "se.marell.dvesta.iodevices.razberry.monitor"
})
public class RazberrySpringConfig {
}
