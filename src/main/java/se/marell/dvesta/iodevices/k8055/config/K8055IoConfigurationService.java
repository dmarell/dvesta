/*
 * Created by Daniel Marell 2011-09-18 22:58
 */
package se.marell.dvesta.iodevices.k8055.config;

import java.util.List;

public interface K8055IoConfigurationService {
    void setConfiguration(List<K8055Configuration> configs);
}
