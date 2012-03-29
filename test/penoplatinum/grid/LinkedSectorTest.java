package penoplatinum.grid;

/**
 * SectorTest
 * 
 * Tests Sector class.
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 

import penoplatinum.util.Bearing;
import penoplatinum.util.Rotation;


public class LinkedSectorTest extends TestCase {

  public LinkedSectorTest(String name) { 
    super(name);
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

    sector.rotate(Rotation.R90);

    assertTrue ( "90 degree rotated sector doesn't have N wall.",
                 sector.hasWall(Bearing.N) );
    assertTrue ( "90 degree rotated sector doesn't have E wall.",
                 sector.hasWall(Bearing.E) );
    assertFalse( "90 degree rotated sector has W wall.",
                 sector.hasWall(Bearing.W) );
    assertFalse( "90 degree rotated sector has S wall.",
                 sector.hasWall(Bearing.S) );

    sector.rotate(Rotation.R90);

    assertTrue ( "180 degree rotated sector doesn't have E wall.",
                 sector.hasWall(Bearing.E) );
    assertTrue ( "180 degree rotated sector doesn't have S wall.",
                 sector.hasWall(Bearing.S) );
    assertFalse( "180 degree rotated sector has W wall.",
                 sector.hasWall(Bearing.W) );
    assertFalse( "180 degree rotated sector has N wall.",
                 sector.hasWall(Bearing.N) );

    sector.rotate(Rotation.R90);

    assertTrue ( "270 degree rotated sector doesn't have S wall.",
                 sector.hasWall(Bearing.S) );
    assertTrue ( "270 degree rotated sector doesn't have W wall.",
                 sector.hasWall(Bearing.W) );
    assertFalse( "270 degree rotated sector has E wall.",
                 sector.hasWall(Bearing.E) );
    assertFalse( "270 degree rotated sector has N wall.",
                 sector.hasWall(Bearing.N) );

    sector.rotate(Rotation.R90);

    assertTrue ( "360 degree rotated sector doesn't have N wall.",
                 sector.hasWall(Bearing.N) );
    assertTrue ( "360 degree rotated sector doesn't have W wall.",
                 sector.hasWall(Bearing.W) );
    assertFalse( "360 degree rotated sector has E wall.",
                 sector.hasWall(Bearing.E) );
    assertFalse( "360 degree rotated sector has S wall.",
                 sector.hasWall(Bearing.S)  );
  }

  public void testConsecutiveSectorRotationsEqualsTotalRotation() {
    Sector original = this.createSectorWithWallsNW();

    // start with one rotation by 90 degrees
    Sector sector90 = new LinkedSector(original).rotate(Rotation.R90);  

    Sector sector180 = new LinkedSector(original).rotate(Rotation.R180);
    // rotate further to 180 degrees
    sector90.rotate(Rotation.R90);

    assertEquals( "two rotations of 90 degrees doesn't match 180 degrees",
                  sector90.toString(), sector180.toString() );

    Sector sector270 = new LinkedSector(original).rotate(Rotation.R270);
    // rotate further to 270 degrees
    sector90.rotate(Rotation.R90);

    assertEquals( "three rotations of 90 degrees doesn't match 270 degrees",
                  sector90.toString(), sector270.toString() );

    Sector sector360 = new LinkedSector(original).rotate(Rotation.R360);
    // rotate further to 360 degrees
    sector90.rotate(Rotation.R90);

    assertEquals( "four rotations of 90 degrees doesn't match 360 degrees",
                  sector90.toString(), sector360.toString() );
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
}
