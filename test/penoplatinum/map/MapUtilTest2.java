package penoplatinum.map;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;
import org.junit.Test;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.util.Bearing;

public class MapUtilTest2 extends TestCase {

  /**
   * Test of findHitDistance method, of class MapUtil.
   */
  @Test
  public void testFindHitDistance() {
    Map map = getMap();
    assertEquals(20, MapUtil.findHitDistance(map, 90, 20.0, 20.0));
    assertEquals(60, MapUtil.findHitDistance(map, 0, 20.0, 20.0));
    assertEquals(60, MapUtil.findHitDistance(map, -90, 20.0, 20.0));
    assertEquals(20, MapUtil.findHitDistance(map, 180, 20.0, 20.0));
    
    assertEquals(28, MapUtil.findHitDistance(map, 45, 20.0, 20.0));
    assertEquals(28, MapUtil.findHitDistance(map, 135, 20.0, 20.0));
    assertEquals(28, MapUtil.findHitDistance(map, 225, 20.0, 20.0));
    assertEquals(84, MapUtil.findHitDistance(map, 315, 20.0, 20.0));
    
//    assertEquals(28, MapUtil.findHitDistance(map, -44, 20.0, 20.0));
//    assertEquals(105, MapUtil.findHitDistance(map, -72, 20.0, 20.0));
  }
  
    public static Map getMap() {
    Map m = new MapHashed();
    Tile t = mock(Tile.class);
    when(t.getSize()).thenReturn((Integer) 40);
    when(t.hasWall(Bearing.SW)).thenReturn(true);
    when(t.hasWall(Bearing.W)).thenReturn(true);
    when(t.hasWall(Bearing.NW)).thenReturn(true);
    when(t.hasWall(Bearing.N)).thenReturn(true);
    when(t.hasWall(Bearing.NE)).thenReturn(true);
    m.put(t, 1, 1);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn((Integer) 40);
    when(t.hasWall(Bearing.NW)).thenReturn(true);
    when(t.hasWall(Bearing.N)).thenReturn(true);
    when(t.hasWall(Bearing.NE)).thenReturn(true);
    when(t.hasWall(Bearing.E)).thenReturn(true);
    when(t.hasWall(Bearing.SE)).thenReturn(true);
    m.put(t, 2, 1);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn((Integer) 40);
    when(t.hasWall(Bearing.SE)).thenReturn(true);
    when(t.hasWall(Bearing.S)).thenReturn(true);
    when(t.hasWall(Bearing.SW)).thenReturn(true);
    when(t.hasWall(Bearing.W)).thenReturn(true);
    when(t.hasWall(Bearing.NW)).thenReturn(true);
    m.put(t, 1, 2);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn((Integer) 40);
    when(t.hasWall(Bearing.NE)).thenReturn(true);
    when(t.hasWall(Bearing.E)).thenReturn(true);
    when(t.hasWall(Bearing.SE)).thenReturn(true);
    when(t.hasWall(Bearing.S)).thenReturn(true);
    when(t.hasWall(Bearing.SW)).thenReturn(true);
    m.put(t, 2, 2);
    return m;
  }
}