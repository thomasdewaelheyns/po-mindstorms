package penoplatinum.simulator.tiles;

import penoplatinum.util.Bearing;
import penoplatinum.simulator.tiles.Panel;
import junit.framework.*; 

public class TileTest extends TestCase { 
  private Panel tile;

  public TileTest(String name) { 
    super(name);
  }
  
  public void testNorthWall() {
    Panel tile = this.createEmptyTile();
    tile.withWall(tile.mapBearingToInt(Bearing.N));
    assertEquals("10000000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(tile.mapBearingToInt(Bearing.N)));
    tile.withoutWall(tile.mapBearingToInt(Bearing.N));
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testEastWall() {
    Panel tile = this.createEmptyTile();
    tile.withWall(tile.mapBearingToInt(Bearing.E));
    assertEquals("01000000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(tile.mapBearingToInt(Bearing.E)));
    tile.withoutWall(tile.mapBearingToInt(Bearing.E));
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testSouthWall() {
    Panel tile = this.createEmptyTile();
    tile.withWall(tile.mapBearingToInt(Bearing.S));
    assertEquals("00100000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(tile.mapBearingToInt(Bearing.S)));
    tile.withoutWall(tile.mapBearingToInt(Bearing.S));
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testWestWall() {
    Panel tile = this.createEmptyTile();
    tile.withWall(tile.mapBearingToInt(Bearing.W));
    assertEquals("00010000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(tile.mapBearingToInt(Bearing.W)));
    tile.withoutWall(tile.mapBearingToInt(Bearing.W));
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testNorthLine() {
    Panel tile = this.createEmptyTile();
    int bearing = tile.mapBearingToInt(Bearing.N);
    tile.withLine(bearing, Panel.WHITE);
    System.out.println(tile.toString());
    assertEquals("00001000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(tile.mapBearingToInt(Bearing.N)));
    tile.withoutLine(tile.mapBearingToInt(Bearing.N));
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testEastLine() {
    Panel tile = this.createEmptyTile();
    tile.withLine(tile.mapBearingToInt(Bearing.E), Panel.WHITE);
    assertEquals("00000100000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(tile.mapBearingToInt(Bearing.E)));
    tile.withoutLine(tile.mapBearingToInt(Bearing.E));
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testSouthLine() {
    Panel tile = this.createEmptyTile();
    tile.withLine(tile.mapBearingToInt(Bearing.S), Panel.WHITE);
    assertEquals("00000010000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(tile.mapBearingToInt(Bearing.S)));
    tile.withoutLine(tile.mapBearingToInt(Bearing.S));
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testWestLine() {
    Panel tile = this.createEmptyTile();
    tile.withLine(tile.mapBearingToInt(Bearing.W), Panel.WHITE);
    assertEquals("00000001000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(tile.mapBearingToInt(Bearing.W)));
    tile.withoutLine(tile.mapBearingToInt(Bearing.W));
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testCombinationOfLines() {
    Panel tile = this.createEmptyTile();
    tile.withLine(tile.mapBearingToInt(Bearing.N), Panel.WHITE);
    tile.withLine(tile.mapBearingToInt(Bearing.W), Panel.WHITE);
    assertEquals("00001001000000000000000000000000", tile.toString() );
    assertTrue(tile.hasLine(tile.mapBearingToInt(Bearing.N)));
    assertTrue(tile.hasLine(tile.mapBearingToInt(Bearing.N), Panel.WHITE));
    assertFalse(tile.hasLine(tile.mapBearingToInt(Bearing.N), Panel.BLACK));
    assertFalse(tile.hasLine(tile.mapBearingToInt(Bearing.S)));
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
    tile.withBarcodeLocation(tile.mapBearingToInt(Bearing.N));
    assertEquals("00000000000000000000000010000000", tile.toString() ); 
    assertEquals(tile.mapBearingToInt(Bearing.N), tile.getBarcodeLocation());
    tile.withoutBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationEast() {
    Panel tile = this.createEmptyTile();
    tile.withBarcodeLocation(tile.mapBearingToInt(Bearing.E));
    assertEquals("00000000000000000000000001000000", tile.toString() ); 
    assertEquals(tile.mapBearingToInt(Bearing.E), tile.getBarcodeLocation());
    tile.withoutBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationSouth() {
    Panel tile = this.createEmptyTile();
    tile.withBarcodeLocation(tile.mapBearingToInt(Bearing.S));
    assertEquals("00000000000000000000000011000000", tile.toString() ); 
    assertEquals(tile.mapBearingToInt(Bearing.S), tile.getBarcodeLocation());
    tile.withoutBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationWest() {
    Panel tile = this.createEmptyTile();
    tile.withBarcodeLocation(tile.mapBearingToInt(Bearing.W));
    assertEquals("00000000000000000000000000100000", tile.toString() ); 
    assertEquals(tile.mapBearingToInt(Bearing.W), tile.getBarcodeLocation());
    tile.withoutBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testNarrowingOrientationNorth() {
    Panel tile = this.createEmptyTile();
    tile.setNarrowingOrientation(tile.mapBearingToInt(Bearing.N));
    assertEquals("00000000000000000000000000010000", tile.toString() ); 
    assertEquals(tile.mapBearingToInt(Bearing.N), tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationEast() {
    Panel tile = this.createEmptyTile();
    tile.setNarrowingOrientation(tile.mapBearingToInt(Bearing.E));
    assertEquals("00000000000000000000000000001000", tile.toString() ); 
    assertEquals(tile.mapBearingToInt(Bearing.E), tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationSouth() {
    Panel tile = this.createEmptyTile();
    tile.setNarrowingOrientation(tile.mapBearingToInt(Bearing.S));
    assertEquals("00000000000000000000000000011000", tile.toString() ); 
    assertEquals(tile.mapBearingToInt(Bearing.S), tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationWest() {
    Panel tile = this.createEmptyTile();
    tile.setNarrowingOrientation(tile.mapBearingToInt(Bearing.W));
    assertEquals("00000000000000000000000000000100", tile.toString() ); 
    assertEquals(tile.mapBearingToInt(Bearing.W), tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  private Panel createEmptyTile() {
    return new Panel();
  }

}
