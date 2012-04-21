/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.runners.JUnit4;
import org.junit.runner.RunWith;
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
public class BufferTest {

  private int value;
  private int value2;
  private int value3;
  private int value4;
  private Buffer instance;
  @Rule
  public ExpectedException exception = ExpectedException.none();

  public BufferTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
    value = 0;
    value2 = 1;
    value3 = 2;
    value4 = 3;
    instance = new Buffer(3);
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of insert method, of class Buffer.
   */
  @Test
  public void testInsert() {
    instance.setCheckPoint();
    instance.insert(value);
    assertEquals(instance.getEndPoint(), 1);
    instance.insert(value2);
    assertEquals(instance.getEndPoint(), 2);
    instance.insert(value3);
    assertEquals(instance.getEndPoint(), 3);
    exception.expect(RuntimeException.class);
    instance.insert(value4);



  }

  /**
   * Test of removeLast method, of class Buffer.
   */
  @Test
  public void testRemoveLast() {
    instance.insert(value);
    instance.insert(value2);
    instance.insert(value3);
    assertEquals(instance.get(0), value3);
    instance.removeLast();
    assertEquals(instance.get(0), value2);
    instance.removeLast();
    assertEquals(instance.get(0), value);
    instance.removeLast();
    assertEquals(value3, instance.get(0));

  }
}
