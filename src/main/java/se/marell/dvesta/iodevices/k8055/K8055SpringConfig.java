/*
 * Created by Daniel Marell 14-11-30 12:22
 */
package se.marell.dvesta.iodevices.k8055;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "se.marell.dvesta.iodevices.k8055.config",
        "se.marell.dvesta.iodevices.k8055.impl",
        "se.marell.dvesta.iodevices.k8055.monitor"
})
public class K8055SpringConfig {
}
