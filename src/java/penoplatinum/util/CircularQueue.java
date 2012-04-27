package penoplatinum.util;

/**
 * CircularQueue
 *
 * A this.queue that stores generic objects. It doesn't discard items but 
 * instead moves an internal pointer forward when removing an item.
 *
 * @author Team Platinum
 *
 * Taken from http://www.scribd.com/doc/14768610/Array-Circular-Queue
 */

public class CircularQueue<T> {

  private int front = 0, size = 0;
  private Object[] queue;


  public CircularQueue(int maxElements) {
    this.queue = new Object[maxElements];
  }

  public boolean isEmpty() {
    return this.size == 0;
  }

  public boolean isFull() {
    return this.size == this.queue.length;
  }

  public void insert(T o) {
    if(this.isFull()) { throw new RuntimeException( "Queue is full!" ); }
    this.queue[(this.front+this.size) % this.queue.length] = o;
    this.size++;
  }
  
  public int size() {
    return this.size;
  }

  public String toString() {
    String buffer = "";
    for(int i=0; i<this.queue.length; i++) {
      buffer += this.queue[i] + " ";
    }
    return buffer + this.front + " " + this.size;
  }

  @SuppressWarnings("unchecked")
  public T remove() {
    if( this.isEmpty() ) { throw new RuntimeException( "Queue is empty." ); }
    Object object = this.queue[this.front];
    this.front = (this.front + 1) % this.queue.length;
    this.size--;
    return (T) object;
  }
}
