package penoplatinum.util;

import java.util.Iterator;

class KeyIterator<K> implements Iterator<K> {
  EntryIterator<K, ?> entry;

  KeyIterator(SimpleHashMap<K, ?> outer) {
     entry = new EntryIterator(outer);
  }

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
