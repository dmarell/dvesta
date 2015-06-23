/*
 * Created by Daniel Marell 14-02-15 14:29
 */
package se.marell.dvesta.batterybackdata;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BatteryBackupDataTest {
  private Map<String, Object> objectMap = new HashMap<>();

  private BatteryBackupRepository repository = new BatteryBackupRepository() {

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
  };

  @Test
  public void testBitData() throws Exception {
    assertThat(objectMap.size(), is(0));
    BitData d = new BitData(repository, "bit1");
    assertThat(objectMap.size(), is(1));
    assertThat(d.getValue(), is(false));
    assertThat(d.getName(), is("bit1"));
    d.setValue(true);
    assertThat(d.getValue(), is(true));
  }

  @Test
  public void testFloatData() throws Exception {
    assertThat(objectMap.size(), is(0));
    FloatData d = new FloatData(repository, "float1", 2);
    assertThat(objectMap.size(), is(1));
    assertThat(d.getValue(), is(0f));
    assertThat(d.getName(), is("float1"));
    d.setValue(1.42f);
    assertThat(d.getValue(), is(1.42f));
  }

  @Test
  public void testIntData() throws Exception {
    assertThat(objectMap.size(), is(0));
    IntData d = new IntData(repository, "int1");
    assertThat(objectMap.size(), is(1));
    assertThat(d.getValue(), is(0));
    assertThat(d.getName(), is("int1"));
    d.setValue(42);
    assertThat(d.getValue(), is(42));
  }

  @Test
  public void testShortData() throws Exception {
    assertThat(objectMap.size(), is(0));
    ShortData d = new ShortData(repository, "short1");
    assertThat(objectMap.size(), is(1));
    assertThat(d.getValue(), is((short) 0));
    assertThat(d.getName(), is("short1"));
    d.setValue((short) 42);
    assertThat(d.getValue(), is((short) 42));
  }
}
