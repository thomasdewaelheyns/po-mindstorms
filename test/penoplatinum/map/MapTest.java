package penoplatinum.map;

import penoplatinum.map.Map;
import penoplatinum.simulator.tiles.Panel;
import junit.framework.*; 
import penoplatinum.map.MapArray;

public class MapTest extends TestCase { 
  private Panel tile;

  public MapTest(String name) { 
    super(name);
  }
  
  public void testAddingTileSequence() {
    Map  map   = this.createEmptyMapOfWidth(2);
    Panel tile1 = this.createEmptyTile();
    Panel tile2 = this.createEmptyTile();
    Panel tile3 = this.createEmptyTile();
    map.add(tile1).add(tile2).add(tile3);
    assertEquals( tile1, map.get(1,1) );
    assertEquals( tile2, map.get(2,1) );
    assertEquals( tile3, map.get(1,2) );
  }
  
  public void testMapDimensions() {
    Map map = this.createMapOfWidthWithTiles( 2, 3 );
    assertEquals( 2, map.getHeight() );
    assertEquals( 2, map.getWidth()  );
  }

  private Map createMapOfWidthWithTiles( int width, int tiles ) {
    Map  map   = this.createEmptyMapOfWidth(width);
    while( tiles-- > 0 ) {
      map.add( this.createEmptyTile() );
    }
    return map;
  }

  private Map createEmptyMapOfWidth( int width ) {
    return new MapArray( width );
  }

  private Panel createEmptyTile() {
    return new Panel();
  }
}
