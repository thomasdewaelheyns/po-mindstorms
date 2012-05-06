package penoplatinum.util;

import java.util.Iterator;

class ValueIterator<V> implements Iterator<V> {
  private EntryIterator<?, V> entry;
  private SimpleHashMap<?, V> outer;

  public ValueIterator(SimpleHashMap<?, V> outer) {
    this.outer = outer;
    entry = new EntryIterator(outer);
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
