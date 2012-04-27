package penoplatinum.simulator.tiles;

import java.awt.Graphics2D;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SectorDrawTest {
  
  public SectorDrawTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of drawTile method, of class SectorDraw.
   */
  @Test
  public void testDrawTile() {
    Graphics2D g2d = null;
    int left = 0;
    int top = 0;
    SectorDraw instance = null;
    instance.drawTile(g2d, left, top);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of drawSize method, of class SectorDraw.
   */
  @Test
  public void testDrawSize() {
    SectorDraw instance = null;
    int expResult = 0;
    int result = instance.drawSize();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
}
