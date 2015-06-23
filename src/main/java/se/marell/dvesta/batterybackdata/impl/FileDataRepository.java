/*
 * Created by Daniel Marell 14-02-15 14:52
 */
package se.marell.dvesta.batterybackdata.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.stereotype.Service;
import se.marell.dvesta.batterybackdata.BatteryBackupRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class FileDataRepository implements BatteryBackupRepository {
    private Map<String, Object> objectMap = new HashMap<>();
    private File file;
    private Kryo kryo = new Kryo();

    public FileDataRepository() {
        this.file = new File("batterybackrepository.bin");
    }

    @Override
    public <T> T createObject(String name, T object) {
        T o = (T) objectMap.get(name);
        if (o != null) {
            return o;
        }
        objectMap.put(name, object);
        return object;
    }

    @Override
    public void deleteObject(String name) {
        objectMap.remove(name);
    }

    @Override
    public <T> T getObject(String name, Class<T> objectClass) {
        return (T) objectMap.get(name);
    }

    @Override
    public Set<String> getObjectNames() {
        return objectMap.keySet();
    }

    public void save() throws ParameterPersistenceException {
        try {
            Output output = new Output(new FileOutputStream(file));
            kryo.writeObject(output, objectMap);
            output.close();
        } catch (FileNotFoundException e) {
            throw new ParameterPersistenceException("Failed to persist objectMap", e);
        }
    }

    public void restore() throws ParameterPersistenceException {
        try {
            Input input = new Input(new FileInputStream(file));
            objectMap = kryo.readObject(input, HashMap.class);
            input.close();
        } catch (FileNotFoundException e) {
            throw new ParameterPersistenceException("Failed to restore objectMap", e);
        }
    }
}
