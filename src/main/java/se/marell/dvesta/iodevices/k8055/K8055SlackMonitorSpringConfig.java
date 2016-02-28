/*
 * Created by Daniel Marell 14-11-30 12:22
 */
package se.marell.dvesta.iodevices.k8055;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "se.marell.dvesta.iodevices.k8055.slackmonitor"
})
public class K8055SlackMonitorSpringConfig {
}
