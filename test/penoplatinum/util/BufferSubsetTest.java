/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;import static org.mockito.Mockito.*;



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
public class BufferSubsetTest {
  private Buffer buffer;
  private BufferSubset instance2;
  private Buffer mockedBuffer;
  private BufferSubset instance;
  
  public BufferSubsetTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }
  
  @Before
  public void setUp() {
     buffer = new Buffer(10);
    instance2 = new BufferSubset(buffer, 1, 10, 20);
    buffer.insert(8);
    buffer.insert(9);
    mockedBuffer = mock(Buffer.class);
    when(mockedBuffer.getRaw(5)).thenReturn(6);
    instance = new BufferSubset(mockedBuffer, 5, 10, 20);
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of get method, of class BufferSubset.
   */
  @Test
  public void testGet() {

    int result = instance.get(0);
    assertEquals(6, result);
    assertEquals(9, instance2.get(0));

  }

  /**
   * Test of size method, of class BufferSubset.
   */
  @Test
  public void testSize() {
    assertEquals(instance.size(), 5);
    assertEquals(instance2.size(), 9);
    buffer.insert(10);
    assertEquals(instance2.size(), 9);
  }
}
