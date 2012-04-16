/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

import java.util.List;
import junit.framework.TestCase;


/**
 *
 * @author Team Platinum
 */
public class SimpleHashMapTest extends TestCase{
  
  public SimpleHashMapTest(String name) {
    super(name);
  }
  
  public void testGet(){
    SimpleHashMap<String, Integer> map = new SimpleHashMap<String, Integer>();
    assertEquals(null,map.get("thisDoesNotExist"));
    
    map.put("test1", 1);
    assertEquals((Integer)1, map.get("test1"));
    
    map.put("test2", 2);
    assertEquals((Integer)2, map.get("test2"));
    
    map.put("test3", 3);
    assertEquals((Integer)3, map.get("test3"));
  }
  
  public void testPut(){
    SimpleHashMap<String, Integer> map = new SimpleHashMap<String, Integer>();
    assertEquals(0, map.size());
    
    map.put("test1", 1);
    assertEquals(1, map.size());
    assertEquals((Integer)1, map.get("test1"));
    assertEquals("test1", map.findKey(1));
    
    map.put("test2", 2);
    assertEquals(2, map.size());
    assertEquals((Integer)2, map.get("test2"));
    assertEquals("test2", map.findKey(2));
    
    map.put("test1", 3);
    assertEquals(2, map.size());
    assertEquals((Integer)3, map.get("test1"));
    assertEquals("test1", map.findKey(3));
  }
  
  public void testSize(){
    SimpleHashMap<String, Integer> map = new SimpleHashMap<String, Integer>();
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
  
  public void testIsEmpty(){
    SimpleHashMap<String, Integer> map = new SimpleHashMap<String, Integer>();
    assertTrue(map.isEmpty());
    
    map.put("test1", 1);
    assertFalse(map.isEmpty());
  }
  
  public void testValues(){
    SimpleHashMap<String, Integer> map = new SimpleHashMap<String, Integer>();
    map.put("test1", 1);
    map.put("test2", 2);
    map.put("test3", 3);
    List<Integer> val = map.values();
    assertEquals((Integer)1, val.get(0));
    assertEquals((Integer)2, val.get(1));
    assertEquals((Integer)3, val.get(2));
  }
  
  public void testFindKey(){
    SimpleHashMap<String, Integer> map = new SimpleHashMap<String, Integer>();
    map.put("test1", 1);
    map.put("test2", 2);
    map.put("test3", 3);
    assertEquals("test3",map.findKey(3));
    assertEquals("test1",map.findKey(1));
    assertEquals("test2",map.findKey(2));
    assertEquals(null, map.findKey(4));
  }
}