package penoplatinum.grid;

import junit.framework.*; 

import penoplatinum.simulator.Bearing;
import penoplatinum.simulator.Rotation;


public class SectorTest extends TestCase {

  public SectorTest(String name) { 
    super(name);
  }

  public void testSectorCopyRetainsWallInfo() {
    Sector original = this.createSectorWithWallsNW();
    Sector copy = new Sector(original);

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
    Sector sector90 = new Sector(original).rotate(90);  

    Sector sector180 = new Sector(original).rotate(180);
    // rotate further to 180 degrees
    sector90.rotate(90);

    assertEquals( "two rotations of 90 degrees doesn't match 180 degrees",
                  sector90.toString(), sector180.toString() );

    Sector sector270 = new Sector(original).rotate(270);
    // rotate further to 270 degrees
    sector90.rotate(90);

    assertEquals( "three rotations of 90 degrees doesn't match 270 degrees",
                  sector90.toString(), sector270.toString() );

    Sector sector360 = new Sector(original).rotate(360);
    // rotate further to 360 degrees
    sector90.rotate(90);

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
    return new Sector().addWall   (Bearing.N)
                       .removeWall(Bearing.E)
                       .removeWall(Bearing.S)
                       .addWall   (Bearing.W);
  }
}
