/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

/**
 *
 * Taken from http://www.scribd.com/doc/14768610/Array-Circular-Queue
 */
public class CircularQueue<T> {

  private int front = 0, rear = 0;
  private Object[] queue;

  public CircularQueue(int maxElements) {
    queue = new Object[maxElements+1];
  }

  public void insert(T o) {
    int temp = rear;
    rear = (rear + 1) % queue.length;
    if (front == rear) {
      rear = temp;
      throw new RuntimeException("Queue is full!");
    }
    queue[rear] = o;
  }

  public boolean isEmpty() {
    return front == rear;
  }

  public boolean isFull() {
    return ((rear + 1) % queue.length) == front;
  }

  @SuppressWarnings("unchecked")
  public T remove() {
    if (front == rear) {
      throw new RuntimeException("Queue is empty");
    }
    front = (front + 1) % queue.length;
    return (T) queue[front];
  }
}
