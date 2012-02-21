package penoplatinum.simulator.t;

import penoplatinum.simulator.Baring;
import penoplatinum.simulator.tiles.Panel;
import junit.framework.*; 

public class TileTest extends TestCase { 
  private Panel tile;

  public TileTest(String name) { 
    super(name);
  }
  
  public void testNorthWall() {
    Panel tile = this.createEmptyTile();
    tile.withWall(Baring.N);
    assertEquals("10000000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Baring.N));
    tile.withoutWall(Baring.N);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testEastWall() {
    Panel tile = this.createEmptyTile();
    tile.withWall(Baring.E);
    assertEquals("01000000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Baring.E));
    tile.withoutWall(Baring.E);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testSouthWall() {
    Panel tile = this.createEmptyTile();
    tile.withWall(Baring.S);
    assertEquals("00100000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Baring.S));
    tile.withoutWall(Baring.S);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testWestWall() {
    Panel tile = this.createEmptyTile();
    tile.withWall(Baring.W);
    assertEquals("00010000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Baring.W));
    tile.withoutWall(Baring.W);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testNorthLine() {
    Panel tile = this.createEmptyTile();
    tile.withLine(Baring.N, Panel.WHITE);
    assertEquals("00001000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Baring.N));
    tile.withoutLine(Baring.N);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testEastLine() {
    Panel tile = this.createEmptyTile();
    tile.withLine(Baring.E, Panel.WHITE);
    assertEquals("00000100000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Baring.E));
    tile.withoutLine(Baring.E);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testSouthLine() {
    Panel tile = this.createEmptyTile();
    tile.withLine(Baring.S, Panel.WHITE);
    assertEquals("00000010000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Baring.S));
    tile.withoutLine(Baring.S);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testWestLine() {
    Panel tile = this.createEmptyTile();
    tile.withLine(Baring.W, Panel.WHITE);
    assertEquals("00000001000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Baring.W));
    tile.withoutLine(Baring.W);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testCombinationOfLines() {
    Panel tile = this.createEmptyTile();
    tile.withLine(Baring.N, Panel.WHITE);
    tile.withLine(Baring.W, Panel.WHITE);
    assertEquals("00001001000000000000000000000000", tile.toString() );
    assertTrue(tile.hasLine(Baring.N));
    assertTrue(tile.hasLine(Baring.N, Panel.WHITE));
    assertFalse(tile.hasLine(Baring.N, Panel.BLACK));
    assertFalse(tile.hasLine(Baring.S));
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
    tile.withBarcodeLocation(Baring.N);
    assertEquals("00000000000000000000000010000000", tile.toString() ); 
    assertEquals(Baring.N, tile.getBarcodeLocation());
    tile.withoutBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationEast() {
    Panel tile = this.createEmptyTile();
    tile.withBarcodeLocation(Baring.E);
    assertEquals("00000000000000000000000001000000", tile.toString() ); 
    assertEquals(Baring.E, tile.getBarcodeLocation());
    tile.withoutBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationSouth() {
    Panel tile = this.createEmptyTile();
    tile.withBarcodeLocation(Baring.S);
    assertEquals("00000000000000000000000011000000", tile.toString() ); 
    assertEquals(Baring.S, tile.getBarcodeLocation());
    tile.withoutBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationWest() {
    Panel tile = this.createEmptyTile();
    tile.withBarcodeLocation(Baring.W);
    assertEquals("00000000000000000000000000100000", tile.toString() ); 
    assertEquals(Baring.W, tile.getBarcodeLocation());
    tile.withoutBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testNarrowingOrientationNorth() {
    Panel tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Baring.N);
    assertEquals("00000000000000000000000000010000", tile.toString() ); 
    assertEquals(Baring.N, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationEast() {
    Panel tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Baring.E);
    assertEquals("00000000000000000000000000001000", tile.toString() ); 
    assertEquals(Baring.E, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationSouth() {
    Panel tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Baring.S);
    assertEquals("00000000000000000000000000011000", tile.toString() ); 
    assertEquals(Baring.S, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationWest() {
    Panel tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Baring.W);
    assertEquals("00000000000000000000000000000100", tile.toString() ); 
    assertEquals(Baring.W, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  private Panel createEmptyTile() {
    return new Panel();
  }

}
