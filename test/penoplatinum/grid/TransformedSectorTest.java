/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import static org.mockito.Mockito.*;

import junit.framework.TestCase;
import penoplatinum.util.Bearing;
import penoplatinum.util.Rotation;
import penoplatinum.util.TransformationTRT;

/**
 *
 * @author MHGameWork
 */
public class TransformedSectorTest extends TestCase {

  public void testHasNeighbour() {
    Sector sector = new TransformedSector(createSectorWithNeighbourE(), TransformationTRT.fromRotation(Rotation.R90));

    assertFalse(sector.hasNeighbour(Bearing.N));
    assertFalse(sector.hasNeighbour(Bearing.E));
    assertTrue(sector.hasNeighbour(Bearing.S));
    assertFalse(sector.hasNeighbour(Bearing.W));
  }

  public void testGetNeighbour() {
    Sector sector = new TransformedSector(createSectorWithNeighbourE(), TransformationTRT.fromRotation(Rotation.R90));

    assertNull(sector.getNeighbour(Bearing.N));
    assertNull(sector.getNeighbour(Bearing.E));
    assertNotNull(sector.getNeighbour(Bearing.S));
    assertNull(sector.getNeighbour(Bearing.W));

    Sector sector2 = sector.getNeighbour(Bearing.S);

    assertEquals(sector, sector2.getNeighbour(Bearing.N));
    assertNull(sector2.getNeighbour(Bearing.E));
    assertNull(sector2.getNeighbour(Bearing.S));
    assertNull(sector2.getNeighbour(Bearing.W));

  }

  public void testSectorRotationRotatesWalls() {
    Sector original = this.createSectorWithWallsNW();

    Sector sector;


    sector = new TransformedSector(original, TransformationTRT.fromRotation(Rotation.R90));

    assertTrue("90 degree rotated sector doesn't have N wall.",
            sector.hasWall(Bearing.N));
    assertTrue("90 degree rotated sector doesn't have E wall.",
            sector.hasWall(Bearing.E));
    assertFalse("90 degree rotated sector has W wall.",
            sector.knowsWall(Bearing.W));
    assertFalse("90 degree rotated sector has S wall.",
            sector.hasWall(Bearing.S));

    sector = new TransformedSector(original, TransformationTRT.fromRotation(Rotation.R180));

    assertTrue("180 degree rotated sector doesn't have E wall.",
            sector.hasWall(Bearing.E));
    assertTrue("180 degree rotated sector doesn't have S wall.",
            sector.hasWall(Bearing.S));
    assertFalse("180 degree rotated sector has W wall.",
            sector.hasWall(Bearing.W));
    assertFalse("180 degree rotated sector has N wall.",
            sector.knowsWall(Bearing.N));

    sector = new TransformedSector(original, TransformationTRT.fromRotation(Rotation.R270));

    assertTrue("270 degree rotated sector doesn't have S wall.",
            sector.hasWall(Bearing.S));
    assertTrue("270 degree rotated sector doesn't have W wall.",
            sector.hasWall(Bearing.W));
    assertFalse("270 degree rotated sector has E wall.",
            sector.knowsWall(Bearing.E));
    assertFalse("270 degree rotated sector has N wall.",
            sector.hasWall(Bearing.N));

    sector = new TransformedSector(original, TransformationTRT.fromRotation(Rotation.R360));

    assertTrue("360 degree rotated sector doesn't have N wall.",
            sector.hasWall(Bearing.N));
    assertTrue("360 degree rotated sector doesn't have W wall.",
            sector.hasWall(Bearing.W));
    assertFalse("360 degree rotated sector has E wall.",
            sector.hasWall(Bearing.E));
    assertFalse("360 degree rotated sector has S wall.",
            sector.knowsWall(Bearing.S));
  }

  public void testAlterWalls() {
  }

  public void testHasSameWallsAs() {
    Sector s1 = createSectorWithWallsNE();
    Sector s2 = createSectorWithWallsNW();

    Sector t1 = new TransformedSector(s1, TransformationTRT.fromRotation(Rotation.L90));
    Sector t2 = new TransformedSector(s1, TransformationTRT.fromRotation(Rotation.L90));
    t1.toString();
    assertFalse(t1.hasSameWallsAs(s1));
    assertTrue(t1.hasSameWallsAs(s2));
    assertTrue(t1.hasSameWallsAs(t2));
    assertTrue(t2.hasSameWallsAs(t1));

  }

  public void testGivesAccessTo() {
    Sector s1 = createSectorWithWallsNE();

    Sector t1 = new TransformedSector(s1, TransformationTRT.fromRotation(Rotation.L90));
        
    t1.givesAccessTo(Bearing.N);
    verify(s1).givesAccessTo(Bearing.E);
    t1.givesAccessTo(Bearing.E);
    verify(s1).givesAccessTo(Bearing.S);
  }

  // utility methods to setup basic components
  private Sector createSectorWithWallsNW() {
    /* result looks like this:
     *    +--+
     *    |
     *    +??+
     */

    Sector ret = mock(Sector.class);

    when(ret.hasWall(Bearing.N)).thenReturn(true);
    when(ret.hasNoWall(Bearing.N)).thenReturn(false);
    when(ret.knowsWall(Bearing.N)).thenReturn(true);

    when(ret.hasWall(Bearing.E)).thenReturn(false);
    when(ret.hasNoWall(Bearing.E)).thenReturn(true);
    when(ret.knowsWall(Bearing.E)).thenReturn(true);

    when(ret.knowsWall(Bearing.S)).thenReturn(false);

    when(ret.hasWall(Bearing.W)).thenReturn(true);
    when(ret.hasNoWall(Bearing.W)).thenReturn(false);
    when(ret.knowsWall(Bearing.W)).thenReturn(true);

    return ret;
  }

  private Sector createSectorWithWallsNE() {
    /* result looks like this:
     *    +--+
     *    ?  |
     *    +  +
     */

    Sector ret = mock(Sector.class);

    when(ret.hasWall(Bearing.N)).thenReturn(true);
    when(ret.hasNoWall(Bearing.N)).thenReturn(false);
    when(ret.knowsWall(Bearing.N)).thenReturn(true);


    when(ret.knowsWall(Bearing.W)).thenReturn(false);

    when(ret.hasWall(Bearing.S)).thenReturn(false);
    when(ret.hasNoWall(Bearing.S)).thenReturn(true);
    when(ret.knowsWall(Bearing.S)).thenReturn(true);

    when(ret.hasWall(Bearing.E)).thenReturn(true);
    when(ret.hasNoWall(Bearing.E)).thenReturn(false);
    when(ret.knowsWall(Bearing.E)).thenReturn(true);

    return ret;
  }

  private Sector createSectorWithNeighbourE() {
    /* result looks like this:
     *    +--+
     *    |
     *    +??+
     */

    Sector ret = mock(Sector.class);
    Sector neighbour = mock(Sector.class);

    when(ret.hasNeighbour(Bearing.E)).thenReturn(true);
    when(ret.hasNeighbour(Bearing.S)).thenReturn(false);
    when(ret.hasNeighbour(Bearing.W)).thenReturn(false);
    when(ret.hasNeighbour(Bearing.N)).thenReturn(false);

    when(ret.getNeighbour(Bearing.E)).thenReturn(neighbour);
    when(ret.getNeighbour(Bearing.S)).thenReturn(null);
    when(ret.getNeighbour(Bearing.W)).thenReturn(null);
    when(ret.getNeighbour(Bearing.N)).thenReturn(null);


    when(neighbour.hasNeighbour(Bearing.E)).thenReturn(false);
    when(neighbour.hasNeighbour(Bearing.S)).thenReturn(false);
    when(neighbour.hasNeighbour(Bearing.W)).thenReturn(true);
    when(neighbour.hasNeighbour(Bearing.N)).thenReturn(false);

    when(neighbour.getNeighbour(Bearing.E)).thenReturn(null);
    when(neighbour.getNeighbour(Bearing.S)).thenReturn(null);
    when(neighbour.getNeighbour(Bearing.W)).thenReturn(ret);
    when(neighbour.getNeighbour(Bearing.N)).thenReturn(null);


    return ret;
  }
}
