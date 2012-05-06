package penoplatinum.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class SimpleHashMapTest extends TestCase {

  private SimpleHashMap map;

  @Before
  public void setUp() {
    map = new SimpleHashMap();
  }

  public SimpleHashMapTest(String name) {
    super(name);
  }

  @Test
  public void testMultipleHash() {
    for(int i = 0; i < 100; i++){
      map.put(i, i);
    }
    boolean[] found = new boolean[100];
    int foundCount = 0;
    for(Object o : map.values()){
      if(found[(Integer) o]){
        fail();
      }
      found[(Integer) o] = true;
      foundCount++;
    }
    assertEquals(100, foundCount);
  }

  @Test
  public void testGet() {
    assertEquals(null, map.get("thisDoesNotExist"));

    map.put("test1", 1);
    assertEquals((Integer) 1, map.get("test1"));

    map.put("test2", 2);
    assertEquals((Integer) 2, map.get("test2"));

    map.put("test3", 3);
    assertEquals((Integer) 3, map.get("test3"));
  }

  @Test
  public void testPut() {
    assertEquals(0, map.size());

    map.put("test1", 1);
    assertEquals(1, map.size());
    assertEquals((Integer) 1, map.get("test1"));
    assertEquals("test1", map.findKey(1));

    map.put("test2", 2);
    assertEquals(2, map.size());
    assertEquals((Integer) 2, map.get("test2"));
    assertEquals("test2", map.findKey(2));

    map.put("test1", 3);
    assertEquals(2, map.size());
    assertEquals((Integer) 3, map.get("test1"));
    assertEquals("test1", map.findKey(3));
  }

  @Test
  public void testSize() {
    assertEquals(0, map.size());

    map.put("test1", 1);
    assertEquals(1, map.size());

    map.put("test2", 2);
    assertEquals(2, map.size());

    map.put("test1", 3);
    assertEquals(2, map.size());

    map.put("test3", 4);
    assertEquals(3, map.size());
  }

  @Test
  public void testIsEmpty() {
    assertTrue(map.isEmpty());

    map.put("test1", 1);
    assertFalse(map.isEmpty());
  }

  @Test
  public void testValues() {
    map.put("test1", 1);
    map.put("test2", 2);
    map.put("test3", 3);
    List<Integer> val = new ArrayList<Integer>();

    for (Object i : map.values()) {
      val.add((Integer) i);
    }
    Collections.sort(val);
    assertEquals(3, val.size());
    assertEquals((Integer) 1, val.get(0));
    assertEquals((Integer) 2, val.get(1));
    assertEquals((Integer) 3, val.get(2));
  }

  @Test
  public void testFindKey() {
    map.put("test1", 1);
    map.put("test2", 2);
    map.put("test3", 3);
    assertEquals("test3", map.findKey(3));
    assertEquals("test1", map.findKey(1));
    assertEquals("test2", map.findKey(2));
    assertEquals(null, map.findKey(4));
  }
}