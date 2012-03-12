package penoplatinum.simulator.t;

import penoplatinum.simulator.Bearing;
import penoplatinum.simulator.tiles.Panel;
import junit.framework.*; 

public class TileTest extends TestCase { 
  private Panel tile;

  public TileTest(String name) { 
    super(name);
  }
  
  public void testNorthWall() {
    Panel tile = this.createEmptyTile();
    tile.withWall(Bearing.N);
    assertEquals("10000000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Bearing.N));
    tile.withoutWall(Bearing.N);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testEastWall() {
    Panel tile = this.createEmptyTile();
    tile.withWall(Bearing.E);
    assertEquals("01000000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Bearing.E));
    tile.withoutWall(Bearing.E);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testSouthWall() {
    Panel tile = this.createEmptyTile();
    tile.withWall(Bearing.S);
    assertEquals("00100000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Bearing.S));
    tile.withoutWall(Bearing.S);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testWestWall() {
    Panel tile = this.createEmptyTile();
    tile.withWall(Bearing.W);
    assertEquals("00010000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Bearing.W));
    tile.withoutWall(Bearing.W);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testNorthLine() {
    Panel tile = this.createEmptyTile();
    tile.withLine(Bearing.N, Panel.WHITE);
    assertEquals("00001000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Bearing.N));
    tile.withoutLine(Bearing.N);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testEastLine() {
    Panel tile = this.createEmptyTile();
    tile.withLine(Bearing.E, Panel.WHITE);
    assertEquals("00000100000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Bearing.E));
    tile.withoutLine(Bearing.E);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testSouthLine() {
    Panel tile = this.createEmptyTile();
    tile.withLine(Bearing.S, Panel.WHITE);
    assertEquals("00000010000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Bearing.S));
    tile.withoutLine(Bearing.S);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testWestLine() {
    Panel tile = this.createEmptyTile();
    tile.withLine(Bearing.W, Panel.WHITE);
    assertEquals("00000001000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Bearing.W));
    tile.withoutLine(Bearing.W);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testCombinationOfLines() {
    Panel tile = this.createEmptyTile();
    tile.withLine(Bearing.N, Panel.WHITE);
    tile.withLine(Bearing.W, Panel.WHITE);
    assertEquals("00001001000000000000000000000000", tile.toString() );
    assertTrue(tile.hasLine(Bearing.N));
    assertTrue(tile.hasLine(Bearing.N, Panel.WHITE));
    assertFalse(tile.hasLine(Bearing.N, Panel.BLACK));
    assertFalse(tile.hasLine(Bearing.S));
  }
  
  public void testBarcode() {
    Panel tile = this.createEmptyTile();
    tile.withBarcode(15);
    assertEquals("00000000000000000000111100000000", tile.toString() ); 
    assertEquals(15, tile.getBarcode());
    tile.withoutBarcode();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testBarcodeLocationNorth() {
    Panel tile = this.createEmptyTile();
    tile.withBarcodeLocation(Bearing.N);
    assertEquals("00000000000000000000000010000000", tile.toString() ); 
    assertEquals(Bearing.N, tile.getBarcodeLocation());
    tile.withoutBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationEast() {
    Panel tile = this.createEmptyTile();
    tile.withBarcodeLocation(Bearing.E);
    assertEquals("00000000000000000000000001000000", tile.toString() ); 
    assertEquals(Bearing.E, tile.getBarcodeLocation());
    tile.withoutBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationSouth() {
    Panel tile = this.createEmptyTile();
    tile.withBarcodeLocation(Bearing.S);
    assertEquals("00000000000000000000000011000000", tile.toString() ); 
    assertEquals(Bearing.S, tile.getBarcodeLocation());
    tile.withoutBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationWest() {
    Panel tile = this.createEmptyTile();
    tile.withBarcodeLocation(Bearing.W);
    assertEquals("00000000000000000000000000100000", tile.toString() ); 
    assertEquals(Bearing.W, tile.getBarcodeLocation());
    tile.withoutBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testNarrowingOrientationNorth() {
    Panel tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Bearing.N);
    assertEquals("00000000000000000000000000010000", tile.toString() ); 
    assertEquals(Bearing.N, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationEast() {
    Panel tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Bearing.E);
    assertEquals("00000000000000000000000000001000", tile.toString() ); 
    assertEquals(Bearing.E, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationSouth() {
    Panel tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Bearing.S);
    assertEquals("00000000000000000000000000011000", tile.toString() ); 
    assertEquals(Bearing.S, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationWest() {
    Panel tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Bearing.W);
    assertEquals("00000000000000000000000000000100", tile.toString() ); 
    assertEquals(Bearing.W, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  private Panel createEmptyTile() {
    return new Panel();
  }

}
