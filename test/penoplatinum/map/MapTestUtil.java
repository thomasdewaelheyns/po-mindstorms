package penoplatinum.map;

import static org.mockito.Mockito.*;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.util.Bearing;

public class MapTestUtil {
  //WAAAAAAAARNING DO NOT CHANGE OR A LOT OF TESTS DONT WORK POSSIBLY
  public static Map getMap() {
    Map m = new MapHashed();
    Tile t = mock(Tile.class);
    when(t.getSize()).thenReturn((Integer) 40);
    when(t.hasWall(Bearing.N)).thenReturn(true);
    when(t.hasWall(Bearing.NE)).thenReturn(true);
    when(t.hasWall(Bearing.NW)).thenReturn(true);
    m.put(t, 1, 1);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn((Integer) 40);
    when(t.hasWall(Bearing.E)).thenReturn(true);
    when(t.hasWall(Bearing.SE)).thenReturn(true);
    when(t.hasWall(Bearing.NE)).thenReturn(true);
    m.put(t, 2, 1);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn((Integer) 40);
    when(t.hasWall(Bearing.W)).thenReturn(true);
    when(t.hasWall(Bearing.SW)).thenReturn(true);
    when(t.hasWall(Bearing.NW)).thenReturn(true);
    m.put(t, 3, 1);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn((Integer) 40);
    when(t.hasWall(Bearing.E)).thenReturn(true);
    when(t.hasWall(Bearing.SE)).thenReturn(true);
    when(t.hasWall(Bearing.NE)).thenReturn(true);
	when(t.hasWall(Bearing.W)).thenReturn(true);
    when(t.hasWall(Bearing.SW)).thenReturn(true);
    when(t.hasWall(Bearing.NW)).thenReturn(true);
    m.put(t, 1, 2);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn((Integer) 40);
    when(t.hasWall(Bearing.S)).thenReturn(true);
    when(t.hasWall(Bearing.SE)).thenReturn(true);
    when(t.hasWall(Bearing.SW)).thenReturn(true);
    when(t.hasWall(Bearing.W)).thenReturn(true);
    when(t.hasWall(Bearing.SW)).thenReturn(true);
    when(t.hasWall(Bearing.NW)).thenReturn(true);
    when(t.getSize()).thenReturn((Integer) 40);
    m.put(t, 2, 2);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn((Integer) 40);
    when(t.hasWall(Bearing.E)).thenReturn(true);
    when(t.hasWall(Bearing.SE)).thenReturn(true);
    when(t.hasWall(Bearing.NE)).thenReturn(true);
    m.put(t, 3, 2);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn((Integer) 40);
    when(t.hasWall(Bearing.W)).thenReturn(true);
    when(t.hasWall(Bearing.SW)).thenReturn(true);
    when(t.hasWall(Bearing.NW)).thenReturn(true);
    m.put(t, 1, 3);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn((Integer) 40);
    when(t.hasWall(Bearing.S)).thenReturn(true);
    when(t.hasWall(Bearing.SE)).thenReturn(true);
    when(t.hasWall(Bearing.SW)).thenReturn(true);
    when(t.hasWall(Bearing.N)).thenReturn(true);
    when(t.hasWall(Bearing.NE)).thenReturn(true);
    when(t.hasWall(Bearing.NW)).thenReturn(true);
    m.put(t, 2, 3);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn((Integer) 40);
    m.put(t, 3, 3);
    return m;
  }
}

