/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

import org.junit.Rule;
import org.junit.runners.JUnit4;
import org.junit.runner.RunWith;
import org.junit.rules.ExpectedException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Florian
 */
@RunWith(JUnit4.class)
public class CircularQueueTest {

  private Object value;
  private Object value2;
  private Object value4;
  private Object value3;
  private CircularQueue instance;
  @Rule
  public ExpectedException exception = ExpectedException.none();

  public CircularQueueTest() {
  }

  @Before
  public void setUp() {
    value = new Object();
    value2 = new Object();
    value3 = new Object();
    value4 = new Object();
    instance = new CircularQueue<Object>(3);
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of insert method, of class CircularQueue.
   */
  @Test
  public void testAllMethods() {
    exception.expect(RuntimeException.class);
    instance.remove();
    instance.insert(value);
    assertEquals(instance.remove(), value);
    instance.insert(value);
    instance.insert(value2);
    assertEquals(instance.remove(), value2);
    instance.insert(value2);
    instance.insert(value3);
    assertEquals(instance.remove(), value3);
    instance.insert(value3);
    exception.expect(RuntimeException.class);
    instance.insert(value4);
    assertTrue(instance.isFull());
    assertFalse(instance.isEmpty());
    instance.remove();
    instance.remove();
    instance.remove();
    assertTrue(instance.isEmpty());

  }

}