/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

/**
 *
 * @author MHGameWork
 */
public class TransformedSectorTest {
   public void testSectorRotationRotatesWalls() {
    Sector sector = this.createSectorWithWallsNW();

    sector.putOn(mockedGrid);

    when(mockedGrid.getTransformation()).thenReturn(TransformationTRT.fromRotation(Rotation.R90));

    assertTrue("90 degree rotated sector doesn't have N wall.",
            sector.hasWall(Bearing.N));
    assertTrue("90 degree rotated sector doesn't have E wall.",
            sector.hasWall(Bearing.E));
    assertFalse("90 degree rotated sector has W wall.",
            sector.hasWall(Bearing.W));
    assertFalse("90 degree rotated sector has S wall.",
            sector.hasWall(Bearing.S));

    when(mockedGrid.getTransformation()).thenReturn(TransformationTRT.fromRotation(Rotation.R180));

    assertTrue("180 degree rotated sector doesn't have E wall.",
            sector.hasWall(Bearing.E));
    assertTrue("180 degree rotated sector doesn't have S wall.",
            sector.hasWall(Bearing.S));
    assertFalse("180 degree rotated sector has W wall.",
            sector.hasWall(Bearing.W));
    assertFalse("180 degree rotated sector has N wall.",
            sector.hasWall(Bearing.N));

    when(mockedGrid.getTransformation()).thenReturn(TransformationTRT.fromRotation(Rotation.R270));

    assertTrue("270 degree rotated sector doesn't have S wall.",
            sector.hasWall(Bearing.S));
    assertTrue("270 degree rotated sector doesn't have W wall.",
            sector.hasWall(Bearing.W));
    assertFalse("270 degree rotated sector has E wall.",
            sector.hasWall(Bearing.E));
    assertFalse("270 degree rotated sector has N wall.",
            sector.hasWall(Bearing.N));

    when(mockedGrid.getTransformation()).thenReturn(TransformationTRT.fromRotation(Rotation.R360));

    assertTrue("360 degree rotated sector doesn't have N wall.",
            sector.hasWall(Bearing.N));
    assertTrue("360 degree rotated sector doesn't have W wall.",
            sector.hasWall(Bearing.W));
    assertFalse("360 degree rotated sector has E wall.",
            sector.hasWall(Bearing.E));
    assertFalse("360 degree rotated sector has S wall.",
            sector.hasWall(Bearing.S));
  }
}
