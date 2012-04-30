package penoplatinum.grid;

/**
 * SectorTest
 * 
 * Tests Sector class.
 * 
 * @author: Team Platinum
 */
import junit.framework.*;
import static org.mockito.Mockito.*;

import penoplatinum.util.Bearing;

public class LinkedSectorTest extends TestCase {

  private Grid mockedGrid;

  public LinkedSectorTest(String name) {
    super(name);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mockedGrid = createMockedGrid();
  }

  public void testWallManipulation() {
    LinkedSector s1 = new LinkedSector();
    s1.setWall(Bearing.E);
    s1.setNoWall(Bearing.W);
    assertTrue(s1.hasWall(Bearing.E));
    assertTrue(s1.hasNoWall(Bearing.W));
    assertTrue(s1.knowsWall(Bearing.E));
    assertTrue(s1.knowsWall(Bearing.W));

    s1.clearWall(Bearing.E);
    assertTrue(s1.hasNoWall(Bearing.W));
    assertFalse(s1.knowsWall(Bearing.E));
    assertTrue(s1.knowsWall(Bearing.W));

    boolean thrown = false;
    try {
      s1.hasWall(Bearing.E);
    } catch (IllegalArgumentException e) {
      thrown = true;
    }
    assertTrue(thrown);

  }

  public void testIsFullyKnown() {
    LinkedSector s1 = new LinkedSector();
    s1.setWall(Bearing.E);
    assertFalse(s1.isFullyKnown());
    s1.setWall(Bearing.N);
    assertFalse(s1.isFullyKnown());
    s1.setWall(Bearing.S);
    assertFalse(s1.isFullyKnown());
    s1.setNoWall(Bearing.S);
    assertFalse(s1.isFullyKnown());

    s1.setNoWall(Bearing.W);
    assertTrue(s1.isFullyKnown());

    s1.clearWall(Bearing.S);
    assertFalse(s1.isFullyKnown());

  }

  public void testClearWalls() {
    LinkedSector s1 = new LinkedSector();
    s1.setWall(Bearing.E).setWall(Bearing.N).setNoWall(Bearing.S);
    s1.clearWalls();

    for (Bearing b : Bearing.NESW)
      assertFalse(s1.knowsWall(b));
  }

  public void testGivesAccessTo() {
    LinkedSector s1 = new LinkedSector();
    s1.setWall(Bearing.N);
    assertFalse(s1.givesAccessTo(Bearing.N));
    s1.setNoWall(Bearing.N);
    assertTrue(s1.givesAccessTo(Bearing.N));
    s1.clearWall(Bearing.N);
    assertFalse(s1.givesAccessTo(Bearing.N));
  }

  public void testAddNeighbourSimple() {
    LinkedSector s1 = new LinkedSector();
    LinkedSector s2 = new LinkedSector();
    s1.addNeighbour(s2, Bearing.E);

    assertEquals(s2, s1.getNeighbour(Bearing.E));
    assertEquals(true, s1.hasNeighbour(Bearing.E));

    assertEquals(s1, s2.getNeighbour(Bearing.W));
    assertEquals(true, s2.hasNeighbour(Bearing.W));
  }

  public void testAddNeighbourComplex() {
    // Layout:
    // s2 | s3
    //    ----
    // s1 | s4

    Sector s1 = new LinkedSector();
    Sector s2 = new LinkedSector();
    Sector s3 = new LinkedSector();
    Sector s4 = new LinkedSector();

    s1.setWall(Bearing.E).setNoWall(Bearing.N);
    s2.setWall(Bearing.E);
    s3.setWall(Bearing.S);


    s1.addNeighbour(s2, Bearing.N);
    assertEquals(s2, s1.getNeighbour(Bearing.N));
    assertEquals(s1, s2.getNeighbour(Bearing.S));
    assertTrue(s1.hasNoWall(Bearing.N));
    assertTrue(s2.hasNoWall(Bearing.S));
    assertTrue(s1.knowsWall(Bearing.N));
    assertTrue(s2.knowsWall(Bearing.S));

    s2.addNeighbour(s3, Bearing.E);
    assertEquals(s2, s3.getNeighbour(Bearing.W));
    assertEquals(s3, s2.getNeighbour(Bearing.E));
    assertTrue(s2.hasWall(Bearing.E));
    assertTrue(s3.hasWall(Bearing.W));
    assertTrue(s2.knowsWall(Bearing.E));
    assertTrue(s3.knowsWall(Bearing.W));



    s1.addNeighbour(s4, Bearing.E);
    assertEquals(s4, s1.getNeighbour(Bearing.E));
    assertEquals(s1, s4.getNeighbour(Bearing.W));
    assertEquals(s3, s4.getNeighbour(Bearing.N));
    assertEquals(s4, s3.getNeighbour(Bearing.S));
    assertTrue(s1.hasWall(Bearing.E));
    assertTrue(s4.hasWall(Bearing.W));
    assertTrue(s1.knowsWall(Bearing.E));
    assertTrue(s4.knowsWall(Bearing.W));

    assertTrue(s3.hasWall(Bearing.S));
    assertTrue(s4.hasWall(Bearing.N));
    assertTrue(s3.knowsWall(Bearing.S));
    assertTrue(s4.knowsWall(Bearing.N));



    // Test neighbour removal, linked
    s1.addNeighbour(null, Bearing.E);
    assertEquals(null, s1.getNeighbour(Bearing.E));
    assertEquals(null, s3.getNeighbour(Bearing.S));
    //TODO: this is logical but not supported
    //assertEquals(null, s4.getNeighbour(Bearing.W)); 
    //assertEquals(null, s4.getNeighbour(Bearing.N));
  }

  public void testSectorCopyRetainsWallInfo() {
    Sector original = this.createSectorWithWallsNW();
    LinkedSector copy = new LinkedSector(original);

    assertTrue("copied Sector doesn't have N wall.", copy.hasWall(Bearing.N));
    assertTrue("copied Sector doesn't have W wall.", copy.hasWall(Bearing.W));
    assertFalse("copied Sector has wall E.", copy.hasWall(Bearing.E));
    assertFalse("copied Sector has wall S.", copy.hasWall(Bearing.S));
  }

 

  public void testHasSameWallsAs() {
    Sector s1 = new LinkedSector().setWall(Bearing.N).clearWall(Bearing.E).setNoWall(Bearing.N).setWall(Bearing.S);
    Sector s2 = new LinkedSector().clearWall(Bearing.E).setNoWall(Bearing.N).setWall(Bearing.S);
    assertTrue(s1.hasSameWallsAs(s2));
    
  }
  
  public void testMergeWalls()
  {
    Sector s1,s2;
    
    s1 = new LinkedSector().setWall(Bearing.E);
    s2 = new LinkedSector().setWall(Bearing.W);
    s1.addNeighbour(s2, Bearing.E);
    assertTrue(s1.hasWall(Bearing.E));
    
    s1 = new LinkedSector().setWall(Bearing.E);
    s2 = new LinkedSector().clearWall(Bearing.W);
    s1.addNeighbour(s2, Bearing.E);
    assertTrue(s1.hasWall(Bearing.E));
    
    s1 = new LinkedSector().setWall(Bearing.E);
    s2 = new LinkedSector().setNoWall(Bearing.W);
    s1.addNeighbour(s2, Bearing.E);
    assertFalse(s1.knowsWall(Bearing.E));
    
    
    s1 = new LinkedSector().clearWall(Bearing.E);
    s2 = new LinkedSector().setWall(Bearing.W);
    s1.addNeighbour(s2, Bearing.E);
    assertTrue(s1.hasWall(Bearing.E));
    
    s1 = new LinkedSector().clearWall(Bearing.E);
    s2 = new LinkedSector().clearWall(Bearing.W);
    s1.addNeighbour(s2, Bearing.E);
    assertFalse(s1.knowsWall(Bearing.E));
    
    s1 = new LinkedSector().clearWall(Bearing.E);
    s2 = new LinkedSector().setNoWall(Bearing.W);
    s1.addNeighbour(s2, Bearing.E);
    assertFalse(s1.hasWall(Bearing.E));
    
    
    s1 = new LinkedSector().setNoWall(Bearing.E);
    s2 = new LinkedSector().setWall(Bearing.W);
    s1.addNeighbour(s2, Bearing.E);
    assertFalse(s1.knowsWall(Bearing.E));
    
    s1 = new LinkedSector().setNoWall(Bearing.E);
    s2 = new LinkedSector().clearWall(Bearing.W);
    s1.addNeighbour(s2, Bearing.E);
    assertFalse(s1.hasWall(Bearing.E));
    
    s1 = new LinkedSector().setNoWall(Bearing.E);
    s2 = new LinkedSector().setNoWall(Bearing.W);
    s1.addNeighbour(s2, Bearing.E);
    assertFalse(s1.hasWall(Bearing.E));
    
  }
  
  public void testToString()
  {
    assertEquals("NYE S WY", createSectorWithWallsNW().toString());
  }

  // utility methods to setup basic components
  private Sector createSectorWithWallsNW() {
    /* result looks like this:
     *    +--+
     *    |
     *    +
     */
    return new LinkedSector().setWall(Bearing.N).setNoWall(Bearing.E).setNoWall(Bearing.S).setWall(Bearing.W);
  }

  private Grid createMockedGrid() {
    Grid ret = mock(Grid.class);

    return ret;
  }
}
