import junit.framework.*; 
import penoplatinum.grid.Tile;
import penoplatinum.simulator.mini.Bearing;

public class TileTest extends TestCase { 
  private Tile tile;

  public TileTest(String name) { 
    super(name);
  }
  
  public void testNorthWall() {
    Tile tile = this.createEmptyTile();
    tile.withWall(Bearing.N);
    assertEquals("10000000", tile.toString() ); 
    assertTrue(tile.hasWall(Bearing.N));
    tile.withoutWall(Bearing.N);
    assertEquals("00000000", tile.toString() ); 
  }

  public void testEastWall() {
    Tile tile = this.createEmptyTile();
    tile.withWall(Bearing.E);
    assertEquals("01000000", tile.toString() ); 
    assertTrue(tile.hasWall(Bearing.E));
    tile.withoutWall(Bearing.E);
    assertEquals("00000000", tile.toString() ); 
  }

  public void testSouthWall() {
    Tile tile = this.createEmptyTile();
    tile.withWall(Bearing.S);
    assertEquals("00100000", tile.toString() ); 
    assertTrue(tile.hasWall(Bearing.S));
    tile.withoutWall(Bearing.S);
    assertEquals("00000000", tile.toString() ); 
  }

  public void testWestWall() {
    Tile tile = this.createEmptyTile();
    tile.withWall(Bearing.W);
    assertEquals("00010000", tile.toString() ); 
    assertTrue(tile.hasWall(Bearing.W));
    tile.withoutWall(Bearing.W);
    assertEquals("00000000", tile.toString() ); 
  }
  
  private Tile createEmptyTile() {
    return new Tile();
  }

}
