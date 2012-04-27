package penoplatinum.model.part;

/**
 * WallsModelPartTest
 * 
 * Tests WallsModelPart class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;
import org.mockito.Matchers;
import org.mockito.ArgumentMatcher;

import java.util.List;
import java.util.Arrays;

import penoplatinum.Config;

import penoplatinum.grid.Sector;

import penoplatinum.util.Bearing;


public class WallsModelPartTest extends TestCase {

  private WallsModelPart part;
  private Sector         mockedSector1, mockedSector2;


  public WallsModelPartTest(String name) { 
    super(name);
  }

  public void testIsWallFront() {
    this.createModelPart();
    this.part.setWallFrontDistance(Config.WALL_DISTANCE - 5);
    assertTrue(this.part.isWallFront());
    this.part.setWallFrontDistance(Config.WALL_DISTANCE + 5);
    assertFalse(this.part.isWallFront());
  }

  public void testIsWallLeft() {
    this.createModelPart();
    this.part.setWallLeftDistance(Config.WALL_DISTANCE - 5);
    assertTrue(this.part.isWallLeft());
    this.part.setWallLeftDistance(Config.WALL_DISTANCE + 5);
    assertFalse(this.part.isWallLeft());
  }

  public void testIsWallRight() {
    this.createModelPart();
    this.part.setWallRightDistance(Config.WALL_DISTANCE - 5);
    assertTrue(this.part.isWallRight());
    this.part.setWallRightDistance(Config.WALL_DISTANCE + 5);
    assertFalse(this.part.isWallRight());
  }

  public void testWallFrontDistance() {
    this.createModelPart();
    this.part.setWallFrontDistance(25);
    assertEquals(25, this.part.getWallFrontDistance());
  }

  public void testWallLeftDistance() {
    this.createModelPart();
    this.part.setWallLeftDistance(25);
    assertEquals(25, this.part.getWallLeftDistance());
  }

  public void testWallRightDistance() {
    this.createModelPart();
    this.part.setWallRightDistance(25);
    assertEquals(25, this.part.getWallRightDistance());
  }

  public void testUpdateSector() {
    this.createModelPart();
    this.mockSectors();
    this.part.updateSector(this.mockedSector1);
    assertEquals(this.mockedSector1, this.part.getCurrentSector());
    this.part.updateSector(this.mockedSector2);
    assertEquals(this.mockedSector2, this.part.getCurrentSector());
  }

  public void testGetSectorId() {
    this.createModelPart();
    this.mockSectors();
    this.part.updateSector(this.mockedSector1);
    assertEquals(1, this.part.getSectorId());
    this.part.updateSector(this.mockedSector2);
    assertEquals(2, this.part.getSectorId());
  }

  public void testFirstSectorHasChanged() {
    this.createModelPart();
    this.mockSectors();
    this.assertFalse(this.part.currentSectorHasChanged());
    this.part.updateSector(this.mockedSector1);
    this.assertTrue(this.part.currentSectorHasChanged());
  }

  public void testCurrentUnknownSectorHasNotChanged() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(argThat(new anyBearing()))).thenReturn(false);
    when(this.mockedSector2.knowsWall(argThat(new anyBearing()))).thenReturn(false);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertFalse(this.part.currentSectorHasChanged());
  }

  public void testCurrentKnownSectorHasNotChanged() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(argThat(new anyBearing()))).thenReturn(true);
    when(this.mockedSector2.knowsWall(argThat(new anyBearing()))).thenReturn(true);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertFalse(this.part.currentSectorHasChanged());
  }


  public void testChangeDueToChangeUnknown2KnownNorth() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(Bearing.N)).thenReturn(false);
    when(this.mockedSector2.knowsWall(Bearing.N)).thenReturn(true);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertTrue(this.part.currentSectorHasChanged());
  }

  public void testChangeDueToChangeUnknown2KnownEast() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(Bearing.E)).thenReturn(false);
    when(this.mockedSector2.knowsWall(Bearing.E)).thenReturn(true);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertTrue(this.part.currentSectorHasChanged());
  }

  public void testChangeDueToChangeUnknown2KnownSouth() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(Bearing.S)).thenReturn(false);
    when(this.mockedSector2.knowsWall(Bearing.S)).thenReturn(true);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertTrue(this.part.currentSectorHasChanged());
  }

  public void testChangeDueToChangeUnknown2KnownWest() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(Bearing.W)).thenReturn(false);
    when(this.mockedSector2.knowsWall(Bearing.W)).thenReturn(true);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertTrue(this.part.currentSectorHasChanged());
  }

  public void testChangeDueToChangeKnown2UnknownNorth() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(Bearing.N)).thenReturn(true);
    when(this.mockedSector2.knowsWall(Bearing.N)).thenReturn(false);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertTrue(this.part.currentSectorHasChanged());
  }

  public void testChangeDueToChangeKnown2UnknownEast() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(Bearing.E)).thenReturn(true);
    when(this.mockedSector2.knowsWall(Bearing.E)).thenReturn(false);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertTrue(this.part.currentSectorHasChanged());
  }

  public void testChangeDueToChangeKnown2UnknownSouth() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(Bearing.S)).thenReturn(true);
    when(this.mockedSector2.knowsWall(Bearing.S)).thenReturn(false);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertTrue(this.part.currentSectorHasChanged());
  }

  public void testChangeDueToChangeKnown2UnknownWest() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(Bearing.W)).thenReturn(true);
    when(this.mockedSector2.knowsWall(Bearing.W)).thenReturn(false);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertTrue(this.part.currentSectorHasChanged());
  }

  public void testChangeDueToChangeNoWall2WallNorth() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(argThat(new anyBearing()))).thenReturn(true);
    when(this.mockedSector2.knowsWall(argThat(new anyBearing()))).thenReturn(true);
    when(this.mockedSector1.hasWall(Bearing.N)).thenReturn(false);
    when(this.mockedSector2.hasWall(Bearing.N)).thenReturn(true);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertTrue(this.part.currentSectorHasChanged());
  }

  public void testChangeDueToChangeNoWall2WallEast() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(argThat(new anyBearing()))).thenReturn(true);
    when(this.mockedSector2.knowsWall(argThat(new anyBearing()))).thenReturn(true);
    when(this.mockedSector1.hasWall(Bearing.E)).thenReturn(false);
    when(this.mockedSector2.hasWall(Bearing.E)).thenReturn(true);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertTrue(this.part.currentSectorHasChanged());
  }

  public void testChangeDueToChangeNoWall2WallSouth() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(argThat(new anyBearing()))).thenReturn(true);
    when(this.mockedSector2.knowsWall(argThat(new anyBearing()))).thenReturn(true);
    when(this.mockedSector1.hasWall(Bearing.S)).thenReturn(false);
    when(this.mockedSector2.hasWall(Bearing.S)).thenReturn(true);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertTrue(this.part.currentSectorHasChanged());
  }

  public void testChangeDueToChangeNoWall2WallWest() {
    this.createModelPart();
    this.mockSectors();
    when(this.mockedSector1.knowsWall(argThat(new anyBearing()))).thenReturn(true);
    when(this.mockedSector2.knowsWall(argThat(new anyBearing()))).thenReturn(true);
    when(this.mockedSector1.hasWall(Bearing.W)).thenReturn(false);
    when(this.mockedSector2.hasWall(Bearing.W)).thenReturn(true);
    this.part.updateSector(this.mockedSector1);
    this.part.updateSector(this.mockedSector2);
    assertTrue(this.part.currentSectorHasChanged());
  }

  // construction helpers
  
  private void createModelPart() {
    this.part = new WallsModelPart();
  }

  private void mockSectors() {
    this.mockedSector1 = mock(Sector.class);
    this.mockedSector2 = mock(Sector.class);
  }
  
  private class anyBearing extends ArgumentMatcher<Bearing> {
    public boolean matches(Object obj) { return true; }
  }

}
