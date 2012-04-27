package penoplatinum.util;

/**
 * CircularQueueTest
 *
 * Tests the CircularQueue class
 *
 * @author Team Platinum
 */

import junit.framework.*;


public class CircularQueueTest  extends TestCase{

  private Object object1, object2, object3, object4, object5;

  private CircularQueue queue;


  public void testToStringOfEmptyQueue() {
    this.setupEmptyQueue();
    assertEquals( "null null null null 0 0", this.queue.toString() );
  }

  public void testToStringOfFilledQueue() {
    this.setupFilledQueue();
    assertEquals( "obj 1 obj 2 obj 3 obj 4 0 4", this.queue.toString() );
  }
  
  public void testSizeOfEmptyQueue() {
    this.setupEmptyQueue();
    assertEquals( 0, this.queue.size() );
  }

  public void testSizeOfFilledQueue() {
    this.setupFilledQueue();
    assertEquals( 4, this.queue.size() );
  }

  public void testIsFullAndNotEmpty() {
    this.setupFilledQueue();
    assertTrue(this.queue.isFull());
    assertFalse(this.queue.isEmpty());
  }

  public void testIsEmptyAndNotFull() {
    this.setupEmptyQueue();
    assertFalse(this.queue.isFull());
    assertTrue(this.queue.isEmpty());
  }

  @SuppressWarnings("unchecked")
  public void testRemove() {
    this.setupFilledQueue();
    assertEquals(this.object1, this.queue.remove());
    assertEquals(this.object2, this.queue.remove());
    assertEquals(this.object3, this.queue.remove());
    assertEquals(this.object4, this.queue.remove());
  }
  
  @SuppressWarnings("unchecked")
  public void testInsertInFullQueue() {
    this.setupFilledQueue();
    try {
      this.queue.insert(this.object5);
      fail( "Should not be able to insert into full queue." );
    } catch(Exception e) {}
  }

  public void testRemoveFromEmptyQueue() {
    this.setupEmptyQueue();
    try {
      this.queue.remove();
      fail( "Should not be able to remove from queue." );
    } catch(Exception e) {}
  }

  @SuppressWarnings("unchecked")
  public void testCircularBehaviour() {
    this.setupFilledQueue();
    this.queue.remove();
    this.queue.insert(this.object5);
    assertEquals("obj 5 obj 2 obj 3 obj 4 1 4", this.queue.toString());
    assertTrue(this.queue.isFull());
    assertFalse(this.queue.isEmpty());
    assertEquals(this.object2, this.queue.remove());
    assertEquals(this.object3, this.queue.remove());
    assertEquals(this.object4, this.queue.remove());
    assertEquals(this.object5, this.queue.remove());
    assertTrue(this.queue.isEmpty());
    assertFalse(this.queue.isFull());
  }

  // construction helpers

  private void setupEmptyQueue() {
    this.queue  = new CircularQueue<Object>(4);
  }
  
  @SuppressWarnings("unchecked")
  private void setupFilledQueue() {
    this.setupEmptyQueue();

    this.object1 = this.mockObject(1);
    this.object2 = this.mockObject(2);
    this.object3 = this.mockObject(3);
    this.object4 = this.mockObject(4);
    this.object5 = this.mockObject(5);

    this.queue.insert(this.object1);
    this.queue.insert(this.object2);
    this.queue.insert(this.object3);
    this.queue.insert(this.object4);
  }
  
  private Object mockObject(final int index) {
    return new Object() { 
      public String toString() {
        return "obj " + index;
      }
    };
  }

}
