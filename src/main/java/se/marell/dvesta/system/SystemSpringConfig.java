/*
 * Created by Daniel Marell 14-11-30 12:22
 */
package se.marell.dvesta.system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import se.marell.dcommons.time.DefaultTimeSource;
import se.marell.dcommons.time.TimeSource;

@Configuration
@ComponentScan
public class SystemSpringConfig {
    @Bean
    public TimeSource timeSource() {
        return new DefaultTimeSource();
    }
}
