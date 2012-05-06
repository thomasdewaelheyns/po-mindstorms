package penoplatinum.util;

import java.util.Iterator;

class EntryIterator<K, V> implements Iterator<Entry<K, V>> {
  private int hashPos = -1;
  private Entry<K, V> currentEntry;
  private SimpleHashMap<K, V> outer;

  public EntryIterator(SimpleHashMap<K, V> outer) {
    this.outer = outer;
    for (hashPos++; hashPos < outer.list.length; hashPos++) {
      currentEntry = outer.list[hashPos];
      if (outer.list[hashPos] != null) {
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
    if (currentEntry != null) {
      return out;
    }
    for (hashPos++; hashPos < outer.list.length; hashPos++) {
      currentEntry = outer.list[hashPos];
      if (currentEntry != null) {
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
