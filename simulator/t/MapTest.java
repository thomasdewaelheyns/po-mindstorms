import junit.framework.*; 

public class MapTest extends TestCase { 
  private Tile tile;

  public MapTest(String name) { 
    super(name);
  }
  
  public void testAddingTileSequence() {
    Map  map   = this.createEmptyMapOfWidth(2);
    Tile tile1 = this.createEmptyTile();
    Tile tile2 = this.createEmptyTile();
    Tile tile3 = this.createEmptyTile();
    map.add(tile1).add(tile2).add(tile3);
    assertEquals( tile1, map.get(1,1) );
    assertEquals( tile2, map.get(2,1) );
    assertEquals( tile3, map.get(1,2) );
  }

  private Map createEmptyMapOfWidth( int width ) {
    return new Map( width );
  }

  private Tile createEmptyTile() {
    return new Tile();
  }
}
