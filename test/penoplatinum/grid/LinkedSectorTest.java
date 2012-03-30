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
import penoplatinum.util.Rotation;


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
  
  
  
  

  public void testSectorCopyRetainsWallInfo() {
    Sector original = this.createSectorWithWallsNW();
    Sector copy = new LinkedSector(original);

    assertTrue ( "copied Sector doesn't have N wall.", copy.hasWall(Bearing.N));
    assertTrue ( "copied Sector doesn't have W wall.", copy.hasWall(Bearing.W));
    assertFalse( "copied Sector has wall E.",          copy.hasWall(Bearing.E) );
    assertFalse( "copied Sector has wall S.",          copy.hasWall(Bearing.S) );
  }

  public void testSectorRotationRotatesWalls() {
    Sector sector = this.createSectorWithWallsNW();

    sector.putOn(mockedGrid);
    
    when(mockedGrid.getRotation()).thenReturn(Rotation.R90);

    assertTrue ( "90 degree rotated sector doesn't have N wall.",
                 sector.hasWall(Bearing.N) );
    assertTrue ( "90 degree rotated sector doesn't have E wall.",
                 sector.hasWall(Bearing.E) );
    assertFalse( "90 degree rotated sector has W wall.",
                 sector.hasWall(Bearing.W) );
    assertFalse( "90 degree rotated sector has S wall.",
                 sector.hasWall(Bearing.S) );

    when(mockedGrid.getRotation()).thenReturn(Rotation.R180);

    assertTrue ( "180 degree rotated sector doesn't have E wall.",
                 sector.hasWall(Bearing.E) );
    assertTrue ( "180 degree rotated sector doesn't have S wall.",
                 sector.hasWall(Bearing.S) );
    assertFalse( "180 degree rotated sector has W wall.",
                 sector.hasWall(Bearing.W) );
    assertFalse( "180 degree rotated sector has N wall.",
                 sector.hasWall(Bearing.N) );

    when(mockedGrid.getRotation()).thenReturn(Rotation.R270);

    assertTrue ( "270 degree rotated sector doesn't have S wall.",
                 sector.hasWall(Bearing.S) );
    assertTrue ( "270 degree rotated sector doesn't have W wall.",
                 sector.hasWall(Bearing.W) );
    assertFalse( "270 degree rotated sector has E wall.",
                 sector.hasWall(Bearing.E) );
    assertFalse( "270 degree rotated sector has N wall.",
                 sector.hasWall(Bearing.N) );

    when(mockedGrid.getRotation()).thenReturn(Rotation.R360);

    assertTrue ( "360 degree rotated sector doesn't have N wall.",
                 sector.hasWall(Bearing.N) );
    assertTrue ( "360 degree rotated sector doesn't have W wall.",
                 sector.hasWall(Bearing.W) );
    assertFalse( "360 degree rotated sector has E wall.",
                 sector.hasWall(Bearing.E) );
    assertFalse( "360 degree rotated sector has S wall.",
                 sector.hasWall(Bearing.S)  );
  }

  public void testAddNeighbour()
  {
    fail();
//    use case 1 : onze code heeft een sector en wil er een sector aanhangen (bvb bij sonarsweep en nowalls == nieuwe unknown sector buur)
//procedure:
//knownSector.createNeighbour(Bearing)
//die maakt een nieuwe sector en hangt die aan de bearing
//  
  }
  
  
  // utility methods to setup basic components

  private Sector createSectorWithWallsNW() {
    /* result looks like this:
    *    +--+
    *    |
    *    +
    */
    return new LinkedSector().addWall   (Bearing.N)
                             .removeWall(Bearing.E)
                             .removeWall(Bearing.S)
                             .addWall   (Bearing.W);
  }
  
  private Grid createMockedGrid()
  {
    Grid ret = mock(Grid.class);
    
    return ret;
  }
}
