package penoplatinum.map;

import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import penoplatinum.simulator.entities.SimulatedEntity;
import static org.mockito.Mockito.*;

public class MapUtilTest extends TestCase {

  /**
   * Test of findHitDistance method, of class MapUtil.
   */
  @Test
  public void testFindHitDistance() {
    Map map = MapTestUtil.getMap();
    assertEquals(20, MapUtil.findHitDistance(map, 90, 1, 1, 20.0, 20.0));
    assertEquals(60, MapUtil.findHitDistance(map, 0, 1, 1, 20.0, 20.0));
    assertEquals(85, MapUtil.findHitDistance(map, -45, 1, 1, 20.0, 20.0));
    assertEquals(Integer.MAX_VALUE, MapUtil.findHitDistance(map, -44, 1, 1, 20.0, 20.0));
    assertEquals(64, MapUtil.findHitDistance(map, -71, 1, 1, 20.0, 20.0));
    assertEquals(Integer.MAX_VALUE, MapUtil.findHitDistance(map, -72, 1, 1, 20.0, 20.0));
  }

  /**
   * Test of hasTile method, of class MapUtil.
   */
  @Test
  public void testHasTile() {
    Map map = MapTestUtil.getMap();
    assertTrue(MapUtil.hasTile(map, 10.0, 10.0));
    assertTrue(MapUtil.hasTile(map, 50.0, 10.0));
    assertFalse(MapUtil.hasTile(map, 90.0, 10.0));
  }

  /**
   * Test of goesThroughWallX method, of class MapUtil.
   */
  @Test
  public void testGoesThroughWallX() {
    Map map = MapTestUtil.getMap();
    SimulatedEntity entity = mock(SimulatedEntity.class);
    when(entity.getPosX()).thenReturn(20.0);
    when(entity.getPosY()).thenReturn(20.0);
    assertFalse(MapUtil.goesThroughWallX(map, entity, 10.0));
    assertFalse(MapUtil.goesThroughWallX(map, entity, 20.0));
    assertFalse(MapUtil.goesThroughWallX(map, entity, 21.0));
    assertFalse(MapUtil.goesThroughWallX(map, entity, 30.0));
    
    when(entity.getPosX()).thenReturn(60.0);
    when(entity.getPosY()).thenReturn(20.0);
    assertFalse(MapUtil.goesThroughWallX(map, entity, 5.0));
    assertFalse(MapUtil.goesThroughWallX(map, entity, 10.0));
    assertTrue(MapUtil.goesThroughWallX(map, entity, 10.01));
    assertTrue(MapUtil.goesThroughWallX(map, entity, 21.0));
    
    when(entity.getPosX()).thenReturn(20.0);
    when(entity.getPosY()).thenReturn(60.0);
    assertFalse(MapUtil.goesThroughWallX(map, entity, -5.0));
    assertFalse(MapUtil.goesThroughWallX(map, entity, -10.0));
    assertTrue(MapUtil.goesThroughWallX(map, entity, -10.01));
    assertTrue(MapUtil.goesThroughWallX(map, entity, -21.0));
  }

  /**
   * Test of goesThroughWallY method, of class MapUtil.
   */
  @Test
  public void testGoesThroughWallY() {
    Map map = MapTestUtil.getMap();
    SimulatedEntity entity = mock(SimulatedEntity.class);
    when(entity.getPosX()).thenReturn(20.0);
    when(entity.getPosY()).thenReturn(20.0);
    assertFalse(MapUtil.goesThroughWallY(map, entity, -4.0));
    assertFalse(MapUtil.goesThroughWallY(map, entity, -10.0));
    assertTrue(MapUtil.goesThroughWallY(map, entity, -10.1));
    assertTrue(MapUtil.goesThroughWallY(map, entity, -15.0));
    assertTrue(MapUtil.goesThroughWallY(map, entity, -30.0));
    
    when(entity.getPosX()).thenReturn(60.0);
    when(entity.getPosY()).thenReturn(20.0);
    assertFalse(MapUtil.goesThroughWallY(map, entity, 9.0));
    assertFalse(MapUtil.goesThroughWallY(map, entity, 10.0));
    assertFalse(MapUtil.goesThroughWallY(map, entity, 11.0));
    assertFalse(MapUtil.goesThroughWallY(map, entity, 21.0));
    assertFalse(MapUtil.goesThroughWallY(map, entity, 30.0));
    
    when(entity.getPosX()).thenReturn(60.0);
    when(entity.getPosY()).thenReturn(60.0);
    assertFalse(MapUtil.goesThroughWallY(map, entity, 9.0));
    assertFalse(MapUtil.goesThroughWallY(map, entity, 10.0));
    assertTrue(MapUtil.goesThroughWallY(map, entity, 10.01));
    assertTrue(MapUtil.goesThroughWallY(map, entity, 21.0));
    assertTrue(MapUtil.goesThroughWallY(map, entity, 30.0));
  }
}
