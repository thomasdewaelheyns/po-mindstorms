import junit.framework.*; 

public class TileTest extends TestCase { 
  private Tile tile;

  public TileTest(String name) { 
    super(name);
  }
  
  public void testNorthWall() {
    Tile tile = this.createEmptyTile();
    tile.withWall(Baring.N);
    assertEquals("10000000", tile.toString() ); 
    assertTrue(tile.hasWall(Baring.N));
    tile.withoutWall(Baring.N);
    assertEquals("00000000", tile.toString() ); 
  }

  public void testEastWall() {
    Tile tile = this.createEmptyTile();
    tile.withWall(Baring.E);
    assertEquals("01000000", tile.toString() ); 
    assertTrue(tile.hasWall(Baring.E));
    tile.withoutWall(Baring.E);
    assertEquals("00000000", tile.toString() ); 
  }

  public void testSouthWall() {
    Tile tile = this.createEmptyTile();
    tile.withWall(Baring.S);
    assertEquals("00100000", tile.toString() ); 
    assertTrue(tile.hasWall(Baring.S));
    tile.withoutWall(Baring.S);
    assertEquals("00000000", tile.toString() ); 
  }

  public void testWestWall() {
    Tile tile = this.createEmptyTile();
    tile.withWall(Baring.W);
    assertEquals("00010000", tile.toString() ); 
    assertTrue(tile.hasWall(Baring.W));
    tile.withoutWall(Baring.W);
    assertEquals("00000000", tile.toString() ); 
  }
  
  private Tile createEmptyTile() {
    return new Tile();
  }

}
