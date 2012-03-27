package penoplatinum.grid;

import junit.framework.*; 


public class SectorTest() {

  @Test
  public void testSectorCopyRetainsWallInfo() {
    Sector original = this.createSectorWithWallsNW();
    Sector copy = new Sector(original);

    assertTrue ( copy.hasWall(Bearing.N), "copied Sector doesn't have N wall.");
    assertTrue ( copy.hasWall(Bearing.W), "copied Sector doesn't have W wall.");
    assertFalse( copy.hasWall(Bearing.E), "copied Sector has wall E." );
    assertFalse( copy.hasWall(Bearing.S), "copied Sector has wall S." );
  }

  @Test
  public void testSectorRotationRotatesWalls() {
    Sector sector = this.createSectorWithWallsNW();

    sector.rotate(90);

    assertTrue ( sector.hasWall(Bearing.N), 
                 "90 degree rotated sector doesn't have N wall." );
    assertTrue ( sector.hasWall(Bearing.E), 
                 "90 degree rotated sector doesn't have E wall." );
    assertFalse( sector.hasWall(Bearing.W), 
                 "90 degree rotated sector has W wall." );
    assertFalse( sector.hasWall(Bearing.S), 
                 "90 degree rotated sector has S wall." );

    sector.rotate(90);

    assertTrue ( sector.hasWall(Bearing.E), 
                 "180 degree rotated sector doesn't have E wall." );
    assertTrue ( sector.hasWall(Bearing.S), 
                 "180 degree rotated sector doesn't have S wall." );
    assertFalse( sector.hasWall(Bearing.W), 
                 "180 degree rotated sector has W wall." );
    assertFalse( sector.hasWall(Bearing.N), 
                 "180 degree rotated sector has N wall." );

    sector.rotate(90);

    assertTrue ( sector.hasWall(Bearing.S), 
                 "270 degree rotated sector doesn't have S wall." );
    assertTrue ( sector.hasWall(Bearing.W), 
                 "270 degree rotated sector doesn't have W wall." );
    assertFalse( sector.hasWall(Bearing.E), 
                 "270 degree rotated sector has E wall." );
    assertFalse( sector.hasWall(Bearing.N), 
                 "270 degree rotated sector has N wall." );

    sector.rotate(90);

    assertTrue ( sector.hasWall(Bearing.N), 
                 "360 degree rotated sector doesn't have N wall." );
    assertTrue ( sector.hasWall(Bearing.W), 
                 "360 degree rotated sector doesn't have W wall." );
    assertFalse( sector.hasWall(Bearing.E), 
                 "360 degree rotated sector has E wall." );
    assertFalse( sector.hasWall(Bearing.S), 
                 "360 degree rotated sector has S wall." );
  }

  @Test
  public void testConsecutiveSectorRotationsEqualsTotalRotation() {
    Sector original = this.createSectorWithWallsNW();

    // start with one rotation by 90 degrees
    Sector sector90 = new Sector(original).rotate(90);  

    Sector sector180 = new Sector(original).rotate(180);
    // rotate further to 180 degrees
    sector90.rotate(90);

    assertEquals( sector90.toString(), sector180.toString()
                  "two rotations of 90 degrees doesn't match 180 degrees");

    Sector sector270 = new Sector(original).rotate(270);
    // rotate further to 270 degrees
    sector90.rotate(90);

    assertEquals( sector90.toString(), sector270.toString()
                  "three rotations of 90 degrees doesn't match 270 degrees");

    Sector sector360 = new Sector(original).rotate(360);
    // rotate further to 360 degrees
    sector90.rotate(90);

    assertEquals( sector90.toString(), sector360.toString()
                  "four rotations of 90 degrees doesn't match 360 degrees");
  }

  // utility methods to setup basic components

  private Sector createSectorWithWallsNW() {
    /* result looks like this:
    *    +--+
    *    |
    *    +
    */
    return new Sector().withWall(Bearing.N)
                       .withWall(Bearing.W);
  }
}
