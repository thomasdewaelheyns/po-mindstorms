package penoplatinum.map;

import static org.mockito.Mockito.*;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.util.Bearing;

public class MapTestUtil {

  public static Map getMap() {
    Map m = new MapHashed();
    Tile t = mock(Tile.class);
    when(t.getSize()).thenReturn(40);
    when(t.hasWall(Bearing.N)).thenReturn(true);
    m.put(t, 1, 1);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn(40);
    when(t.hasWall(Bearing.E)).thenReturn(true);
    m.put(t, 2, 1);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn(40);
    when(t.hasWall(Bearing.E)).thenReturn(true);
    when(t.hasWall(Bearing.S)).thenReturn(true);
    when(t.getSize()).thenReturn(40);
    m.put(t, 2, 2);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn(40);
    when(t.hasWall(Bearing.E)).thenReturn(true);
    when(t.hasWall(Bearing.S)).thenReturn(true);
    m.put(t, 2, 3);
    t = mock(Tile.class);
    when(t.getSize()).thenReturn(40);
    m.put(t, 1, 2);
    return m;
  }
}
