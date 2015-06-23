/*
 * Created by Daniel Marell 2011-09-18 18:58
 */
package se.marell.dvesta.iodevices.adu208.config;

import java.util.List;

public interface Adu208IoConfigurationService {
    void setConfiguration(List<Adu208Configuration> configs);
}
