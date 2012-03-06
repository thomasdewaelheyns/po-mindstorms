/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MHGameWork
 */
public class SimpleHashMap<K, T> {

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

  public void put(K key, T value) {
    int index = indexOf(key);
    if (index == -1) {
      keys.add(key);
      values.add(value);
      return;
    }
    
    values.set(index, value);
    
  }

  public int size() {
    return keys.size();
  }

  public boolean isEmpty() {
    return keys.size() == 0;
  }

  public List<T> values() {
    return values;
  }
}
