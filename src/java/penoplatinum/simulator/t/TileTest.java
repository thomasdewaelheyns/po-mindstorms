import penoplatinum.simulator.Baring;
import penoplatinum.simulator.Tile;
import junit.framework.*; 

public class TileTest extends TestCase { 
  private Tile tile;

  public TileTest(String name) { 
    super(name);
  }
  
  public void testNorthWall() {
    Tile tile = this.createEmptyTile();
    tile.setWall(Baring.N);
    assertEquals("10000000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Baring.N));
    tile.unsetWall(Baring.N);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testEastWall() {
    Tile tile = this.createEmptyTile();
    tile.setWall(Baring.E);
    assertEquals("01000000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Baring.E));
    tile.unsetWall(Baring.E);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testSouthWall() {
    Tile tile = this.createEmptyTile();
    tile.setWall(Baring.S);
    assertEquals("00100000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Baring.S));
    tile.unsetWall(Baring.S);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testWestWall() {
    Tile tile = this.createEmptyTile();
    tile.setWall(Baring.W);
    assertEquals("00010000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Baring.W));
    tile.unsetWall(Baring.W);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testNorthLine() {
    Tile tile = this.createEmptyTile();
    tile.setLine(Baring.N);
    assertEquals("00001000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Baring.N));
    tile.unsetLine(Baring.N);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testEastLine() {
    Tile tile = this.createEmptyTile();
    tile.setLine(Baring.E);
    assertEquals("00000100000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Baring.E));
    tile.unsetLine(Baring.E);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testSouthLine() {
    Tile tile = this.createEmptyTile();
    tile.setLine(Baring.S);
    assertEquals("00000010000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Baring.S));
    tile.unsetLine(Baring.S);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testWestLine() {
    Tile tile = this.createEmptyTile();
    tile.setLine(Baring.W);
    assertEquals("00000001000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Baring.W));
    tile.unsetLine(Baring.W);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testBarcode() {
    Tile tile = this.createEmptyTile();
    tile.withBarcode(15);
    assertEquals("00000000111100000000000000000000", tile.toString() ); 
    assertEquals(15, tile.getBarcode());
    tile.unwithBarcode();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testBarcodeLocationNorth() {
    Tile tile = this.createEmptyTile();
    tile.withBarcodeLocation(Baring.N);
    assertEquals("00000000000010000000000000000000", tile.toString() ); 
    assertEquals(Baring.N, tile.getBarcodeLocation());
    tile.unwithBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationEast() {
    Tile tile = this.createEmptyTile();
    tile.withBarcodeLocation(Baring.E);
    assertEquals("00000000000001000000000000000000", tile.toString() ); 
    assertEquals(Baring.E, tile.getBarcodeLocation());
    tile.unwithBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationSouth() {
    Tile tile = this.createEmptyTile();
    tile.withBarcodeLocation(Baring.S);
    assertEquals("00000000000011000000000000000000", tile.toString() ); 
    assertEquals(Baring.S, tile.getBarcodeLocation());
    tile.unwithBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationWest() {
    Tile tile = this.createEmptyTile();
    tile.withBarcodeLocation(Baring.W);
    assertEquals("00000000000000100000000000000000", tile.toString() ); 
    assertEquals(Baring.W, tile.getBarcodeLocation());
    tile.unwithBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testNarrowingOrientationNorth() {
    Tile tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Baring.N);
    assertEquals("00000000000000010000000000000000", tile.toString() ); 
    assertEquals(Baring.N, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationEast() {
    Tile tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Baring.E);
    assertEquals("00000000000000001000000000000000", tile.toString() ); 
    assertEquals(Baring.E, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationSouth() {
    Tile tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Baring.S);
    assertEquals("00000000000000011000000000000000", tile.toString() ); 
    assertEquals(Baring.S, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationWest() {
    Tile tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Baring.W);
    assertEquals("00000000000000000100000000000000", tile.toString() ); 
    assertEquals(Baring.W, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  private Tile createEmptyTile() {
    return new Tile();
  }

}
