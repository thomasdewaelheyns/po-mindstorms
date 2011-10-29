import junit.framework.*; 

public class TileTest extends TestCase { 
  private Tile tile;

  public TileTest(String name) { 
    super(name);
  }
  
  public void testNorthWall() {
    Tile tile = this.createEmptyTile();
    tile.setWall(Tile.N);
    assertEquals("10000000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Tile.N));
    tile.unsetWall(Tile.N);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testEastWall() {
    Tile tile = this.createEmptyTile();
    tile.setWall(Tile.E);
    assertEquals("01000000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Tile.E));
    tile.unsetWall(Tile.E);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testSouthWall() {
    Tile tile = this.createEmptyTile();
    tile.setWall(Tile.S);
    assertEquals("00100000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Tile.S));
    tile.unsetWall(Tile.S);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testWestWall() {
    Tile tile = this.createEmptyTile();
    tile.setWall(Tile.W);
    assertEquals("00010000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasWall(Tile.W));
    tile.unsetWall(Tile.W);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testNorthLine() {
    Tile tile = this.createEmptyTile();
    tile.setLine(Tile.N);
    assertEquals("00001000000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Tile.N));
    tile.unsetLine(Tile.N);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testEastLine() {
    Tile tile = this.createEmptyTile();
    tile.setLine(Tile.E);
    assertEquals("00000100000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Tile.E));
    tile.unsetLine(Tile.E);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testSouthLine() {
    Tile tile = this.createEmptyTile();
    tile.setLine(Tile.S);
    assertEquals("00000010000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Tile.S));
    tile.unsetLine(Tile.S);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testWestLine() {
    Tile tile = this.createEmptyTile();
    tile.setLine(Tile.W);
    assertEquals("00000001000000000000000000000000", tile.toString() ); 
    assertTrue(tile.hasLine(Tile.W));
    tile.unsetLine(Tile.W);
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testBarcode() {
    Tile tile = this.createEmptyTile();
    tile.setBarcode(15);
    assertEquals("00000000111100000000000000000000", tile.toString() ); 
    assertEquals(15, tile.getBarcode());
    tile.unsetBarcode();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testBarcodeLocationNorth() {
    Tile tile = this.createEmptyTile();
    tile.setBarcodeLocation(Tile.N);
    assertEquals("00000000000010000000000000000000", tile.toString() ); 
    assertEquals(Tile.N, tile.getBarcodeLocation());
    tile.unsetBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationEast() {
    Tile tile = this.createEmptyTile();
    tile.setBarcodeLocation(Tile.E);
    assertEquals("00000000000001000000000000000000", tile.toString() ); 
    assertEquals(Tile.E, tile.getBarcodeLocation());
    tile.unsetBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationSouth() {
    Tile tile = this.createEmptyTile();
    tile.setBarcodeLocation(Tile.S);
    assertEquals("00000000000011000000000000000000", tile.toString() ); 
    assertEquals(Tile.S, tile.getBarcodeLocation());
    tile.unsetBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testBarcodeLocationWest() {
    Tile tile = this.createEmptyTile();
    tile.setBarcodeLocation(Tile.W);
    assertEquals("00000000000000100000000000000000", tile.toString() ); 
    assertEquals(Tile.W, tile.getBarcodeLocation());
    tile.unsetBarcodeLocation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }
  
  public void testNarrowingOrientationNorth() {
    Tile tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Tile.N);
    assertEquals("00000000000000010000000000000000", tile.toString() ); 
    assertEquals(Tile.N, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationEast() {
    Tile tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Tile.E);
    assertEquals("00000000000000001000000000000000", tile.toString() ); 
    assertEquals(Tile.E, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationSouth() {
    Tile tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Tile.S);
    assertEquals("00000000000000011000000000000000", tile.toString() ); 
    assertEquals(Tile.S, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  public void testNarrowingOrientationWest() {
    Tile tile = this.createEmptyTile();
    tile.setNarrowingOrientation(Tile.W);
    assertEquals("00000000000000000100000000000000", tile.toString() ); 
    assertEquals(Tile.W, tile.getNarrowingOrientation());
    tile.unsetNarrowingOrientation();
    assertEquals("00000000000000000000000000000000", tile.toString() ); 
  }

  private Tile createEmptyTile() {
    return new Tile();
  }

}
