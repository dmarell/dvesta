/*
 * Created by Daniel Marell 2011-12-04 11:37
 */
package se.marell.dvesta.ioscan.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.marell.dvesta.ioscan.IoMapper;

public class AbstractResource {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IoMapper ioMapper;

    protected IoMapper getIoMapper() {
        return ioMapper;
    }
}
