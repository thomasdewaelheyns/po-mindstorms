package penoplatinum.util;

import java.util.Iterator;

public class SimpleHashMap<K, V> {

  Entry<K, V>[] list;
  private int size;

  @SuppressWarnings("unchecked")
  public SimpleHashMap() {
    size = 0;
    list = new Entry[16];
  }

  public K findKey(V value) {
    for (Entry<K, V> e : list) {
      while (e != null && !e.value.equals(value)) {
        e = e.next;
      }
      if (e == null) {
        continue;
      }
      return e.key;
    }
    return null;
  }

  public V get(K key) {
    int hash = Math.abs(key.hashCode()) % list.length;
    Entry<K, V> e = list[hash];
    while (e != null && !e.key.equals(key)) {
      e = e.next;
    }
    if (e == null) {
      return null;
    }
    return e.value;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  @SuppressWarnings("unchecked")
  public void put(K key, V value) {
    ensureCapacity();
    size++;
    int hash = Math.abs(key.hashCode()) % list.length;
    Entry<K, V> c = list[hash];
    if (c == null) {
      list[hash] = new Entry<K, V>(key, value);
      return;
    }
    while (c.next != null && !c.key.equals(key)) {
      c = c.next;
    }
    if (c.key.equals(key)) {
      c.value = value;
      size--;
    } else {
      c.next = new Entry(key, value);
    }
  }

  public void remove(K key) {
    int hash = Math.abs(key.hashCode()) % list.length;
    Entry<K, V> e = list[hash];
    if (e.key == key) {
      list[hash] = e.next;
      size--;
      return;
    }
    while (e.next.key != key) {
      e = e.next;
      if (e.next == null) {
        return;
      }
    }
    e.next = e.next.next;
    size--;
    return;
  }

  public int size() {
    return size;
  }

  @SuppressWarnings("unchecked")
  private void ensureCapacity() {
    if (size <= list.length * 0.75) {
      return;
    }
    Entry<K, V>[] oldList = list;
    list = new Entry[list.length * 2];
    size = 0;
    for (Entry<K, V> e : oldList) {
      while (e != null) {
        put(e.key, e.value);
        e = e.next;
      }
    }
  }

  @SuppressWarnings("unchecked")
  public Iterable<Entry<K, V>> entries() {
    return new It(new EntryIterator(this));
  }

  @SuppressWarnings("unchecked")
  public Iterable<K> keys() {
    return new It(new KeyIterator(this));
  }

  @SuppressWarnings("unchecked")
  public Iterable<V> values() {
    return new It(new ValueIterator(this));
  }
  
  public class It<T> implements Iterable<T> {
    private Iterator iterator;

    public It(Iterator iterator) {
      this.iterator = iterator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<T> iterator() {
      return iterator;
    }
    
  }
}
