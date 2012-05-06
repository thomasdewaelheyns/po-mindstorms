package penoplatinum.util;

import java.util.Iterator;

public class SimpleHashMap<K, V> {
  private Entry<K, V>[] list;
  private int size;

  public SimpleHashMap() {
    size = 0;
    list = new Entry[16];
  }
  
  public static class Entry<K, V> {
    public K key;
    public V value;
    public Entry<K, V> next;

    public Entry(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }

  public K findKey(V value) {
    for(Entry<K, V> e : list){
      while(e != null && !e.value.equals(value)){
        e = e.next;
      }
      if(e == null){
        continue;
      }
      return e.key;
    }
    return null;
  }

  public V get(K key) {
    int hash = Math.abs(key.hashCode() % list.length);
    Entry<K, V> e = list[hash];
    while(e != null && !e.key.equals(key)){
      e = e.next;
    }
    if(e == null){
      return null;
    }
    return e.value;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public Iterable<K> keys() {
    return new Iterable<K>() {
      @Override
      public Iterator<K> iterator() {
        return new KeyIterator();
      }
    };
  }
  
  public void put(K key, V value) {
    ensureCapacity();
    size++;
    int hash = Math.abs(key.hashCode()) % list.length;
    Entry<K, V> c = list[hash];
    if(c == null){
      list[hash] = new Entry<K, V>(key, value);
      return;
    }
    while(c.next != null && !c.key.equals(key)){
      c = c.next;
    }
    if(c.key.equals(key)){
      c.value = value;
      size--;
    } else {
      c.next = new Entry(key, value);
    }
  }

  public void remove(K key) {
    int hash = Math.abs(key.hashCode()) % list.length;
    Entry<K, V> e = list[hash];
    if(e.key == key){
      list[hash] = e.next;
      size--;
      return;
    }
    while(e.next.key != key){
      e = e.next;
      if(e.next == null){
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

  public Iterable<V> values() {
    return new Iterable<V>() {

      @Override
      public Iterator<V> iterator() {
        return new ValueIterator();
      }
    };
  }

  private void ensureCapacity() {
    if(size <= list.length*0.75){
      return;
    }
    Entry<K, V>[] oldList = list;
    list = new Entry[list.length*2];
    size = 0;
    for(Entry<K, V> e : oldList){
      while(e != null){
        put(e.key, e.value);
        e = e.next;
      }
    }
  }

  public Iterable<Entry<K, V>> entries() {
    return new Iterable<Entry<K, V>>() {

      @Override
      public Iterator<Entry<K, V>> iterator() {
        return new EntryIterator();
      }
    };
  }

  private class ValueIterator implements Iterator<V> {
    EntryIterator entry = new EntryIterator();
    public ValueIterator() {
    }

    @Override
    public boolean hasNext() {
      return entry.hasNext();
    }

    @Override
    public V next() {
      return entry.next().value;
    }

    @Override
    public void remove() {
      entry.next();
    }

  }

  private class EntryIterator implements Iterator<Entry<K, V>> {
    private int hashPos = -1;
    private Entry<K, V> currentEntry;

    public EntryIterator() {
      for(hashPos++; hashPos < list.length; hashPos++){
        currentEntry = list[hashPos];
        if(list[hashPos] != null){
          break;
        }
      }
    }
    
    @Override
    public boolean hasNext() {
      return currentEntry != null;
    }

    @Override
    public Entry<K, V> next() {
      Entry out = currentEntry;
      currentEntry = currentEntry.next;
      if(currentEntry != null){
        return out;
      }
      for(hashPos++; hashPos < list.length; hashPos++){
        currentEntry = list[hashPos];
        if(currentEntry != null){
          return out;
        }
      }
      return out;
    }

    @Override
    public void remove() {
      next();
    }

  }
  private class KeyIterator implements Iterator<K> {
      EntryIterator entry = new EntryIterator();

    @Override
    public boolean hasNext() {
      return entry.hasNext();
    }

    @Override
    public K next() {
      return entry.next().key;
    }

    @Override
    public void remove() {
      entry.next();
    }
      
  }

}
