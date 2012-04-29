package penoplatinum.util;

/**
 * Because Lejos doesn't implement HashMap, we implement our own. 
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SimpleHashMap<K, T>  {

  public List<K> keys = new ArrayList<K>();
  public List<T> values = new ArrayList<T>();

  public T get(K key) {
    int index = indexOf(key);
    if (index == -1) {
      return null;
    }

    return values.get(index);
  }

  private int indexOf(K key) {
    for (int i = 0; i < keys.size(); i++) {
      if (keys.get(i).equals(key)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Puts a new entry into the map, or replaces the old one if key already
   * exists in the map
   */
  public void put(K key, T value) {
    int index = indexOf(key);
    if (index == -1) {
      keys.add(key);
      values.add(value);
      return;
    }
    
    values.set(index, value);
    
  }
  
  public void remove(K key)
  {
    values.remove(get(key));
    keys.remove(key);
  }

  public int size() {
    return keys.size();
  }

  public boolean isEmpty() {
    return keys.size() == 0;
  }

  public Iterable<T> values() {
    return values;
  }
  
  public Iterable<K> keys()
  {
    return keys;
  }

  public K findKey(T g) {
    for (int i = 0; i < values.size(); i++) {
      if (g.equals(values.get(i)))
        return keys.get(i);
    }
    return null;
  }

}
