/*
 * Created daniel 2008-sep-13 21:27:44
 */
package se.marell.dvesta.batterybackdata;

import java.util.Set;

public interface BatteryBackupRepository {
    <T> T createObject(String name, T object);

    void deleteObject(String name);

    <T> T getObject(String name, Class<T> objectClass);

    Set<String> getObjectNames();
}
