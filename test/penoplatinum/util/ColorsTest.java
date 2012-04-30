/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import junit.framework.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Florian
 */
public class ColorsTest {

  public ColorsTest() {
  }

  public void testAvailablity() {
    mock(Colors.class);
  }

  @Test
  public void testEnumValues() {
    assertEquals(Colors.GREY.getB(), 200);
    assertEquals(Colors.GREY.getR(), 200);
    assertEquals(Colors.GREY.getG(), 200);
assertEquals(Colors.YELLOW.getB(), 0);
    assertEquals(Colors.YELLOW.getR(), 255);
    assertEquals(Colors.YELLOW.getG(),255);
    assertEquals(Colors.WHITE.getB(), 255);
    assertEquals(Colors.WHITE.getR(), 255);
    assertEquals(Colors.WHITE.getG(), 255);
  }
}
