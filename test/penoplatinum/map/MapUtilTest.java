package penoplatinum.map;

import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import penoplatinum.simulator.entities.SimulatedEntity;
import static org.mockito.Mockito.*;
/**
 *
 * @author Florian
 */
public class MapUtilTest extends TestCase{
  private double[] values;
  public MapUtilTest() {
  }
  /**
   * Test of findHitDistance method, of class MapUtil.
   */
  @Test
  public void testFindHitDistance() {
    Map map = MapTestUtil.getMap();
    assertEquals(20, MapUtil.findHitDistance(map, 90, 1, 1, 20.0, 20.0));
    assertEquals(60, MapUtil.findHitDistance(map, 0, 1, 1, 20.0, 20.0));
    assertEquals(28, MapUtil.findHitDistance(map, -45, 1, 1, 20.0, 20.0));
    assertEquals(Integer.MAX_VALUE, MapUtil.findHitDistance(map, -44, 1, 1, 20.0, 20.0));
    assertEquals(61, MapUtil.findHitDistance(map, -71, 1, 1, 20.0, 20.0));
    assertEquals(105, MapUtil.findHitDistance(map, -72, 1, 1, 20.0, 20.0));
  }
  
  /**
   *
   */
  @Test
  public void testFindHitDistance1() {
    Map m = MapTestUtil.getMap();
    findDirectionedValues(m,1,1);
    assertEquals(20, values[0],0.01);
    assertEquals(60, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(Integer.MAX_VALUE, values[3],0.01);
    findDirectionedValues(m,1,2);
    assertEquals(60, values[0],0.01);
    assertEquals(20, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(20, values[3],0.01);
    findDirectionedValues(m,1,3);
    assertEquals(100, values[0],0.01);
    assertEquals(Integer.MAX_VALUE, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(20, values[3],0.01);
    findDirectionedValues(m,2,1);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(20, values[1],0.01);
    assertEquals(60, values[2],0.01);
    assertEquals(Integer.MAX_VALUE, values[3],0.01);
    findDirectionedValues(m,2,2);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(60, values[1],0.01);
    assertEquals(20, values[2],0.01);
    assertEquals(20, values[3],0.01);
    findDirectionedValues(m,2,3);
    assertEquals(20, values[0],0.01);
    assertEquals(Integer.MAX_VALUE, values[1],0.01);
    assertEquals(20, values[2],0.01);
    assertEquals(60, values[3],0.01);
    findDirectionedValues(m,3,1);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(Integer.MAX_VALUE, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(20, values[3],0.01);
    findDirectionedValues(m,3,2);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(20, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(60, values[3],0.01);
    findDirectionedValues(m,3,3);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(Integer.MAX_VALUE, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(100, values[3],0.01);
    findDirectionedValues2(m,1,1,0,0);
    assertEquals(0, values[0],0.01);
    assertEquals(80, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(Integer.MAX_VALUE, values[3],0.01);
    findDirectionedValues2(m,1,2,1,1);
    assertEquals(41, values[0],0.01);
    assertEquals(39, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(1, values[3],0.01);
    findDirectionedValues2(m,1,3,2,2);
    assertEquals(82, values[0],0.01);
    assertEquals(Integer.MAX_VALUE, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(2, values[3],0.01);
    findDirectionedValues2(m,2,1,39,1);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(1, values[1],0.01);
    assertEquals(79, values[2],0.01);
    assertEquals(Integer.MAX_VALUE, values[3],0.01);
    findDirectionedValues2(m,2,2,1,40);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(79, values[1],0.01);
    assertEquals(0, values[2],0.01);
    assertEquals(1, values[3],0.01);
    findDirectionedValues2(m,2,3,0,38);
    assertEquals(38, values[0],0.01);
    assertEquals(Integer.MAX_VALUE, values[1],0.01);
    assertEquals(2, values[2],0.01);
    assertEquals(40, values[3],0.01);
    findDirectionedValues2(m,3,1,40,0);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(Integer.MAX_VALUE, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(40, values[3],0.01);
    findDirectionedValues2(m,3,2,1,0);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(39, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(41, values[3],0.01);
    findDirectionedValues2(m,3,3,0,1);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(Integer.MAX_VALUE, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(80, values[3],0.01);
    findDirectionedValues2(m,1,1,2,19);
    assertEquals(19, values[0],0.01);
    assertEquals(78, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(Integer.MAX_VALUE, values[3],0.01);
    findDirectionedValues2(m,1,2,40,0);
    assertEquals(40, values[0],0.01);
    assertEquals(0, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(40, values[3],0.01);
    findDirectionedValues2(m,1,3,9,0);
    assertEquals(80, values[0],0.01);
    assertEquals(Integer.MAX_VALUE, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(9, values[3],0.01);
    findDirectionedValues2(m,2,1,16,5);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(24, values[1],0.01);
    assertEquals(75, values[2],0.01);
    assertEquals(Integer.MAX_VALUE, values[3],0.01);
    findDirectionedValues2(m,2,2,18,33);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(62, values[1],0.01);
    assertEquals(7, values[2],0.01);
    assertEquals(18, values[3],0.01);
    findDirectionedValues2(m,2,3,22,11);
    assertEquals(11, values[0],0.01);
    assertEquals(Integer.MAX_VALUE, values[1],0.01);
    assertEquals(29, values[2],0.01);
    assertEquals(62, values[3],0.01);
    findDirectionedValues2(m,3,1,0,20);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(Integer.MAX_VALUE, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(0, values[3],0.01);
    findDirectionedValues2(m,3,2,20,0);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(20, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(60, values[3],0.01);
    findDirectionedValues2(m,3,3,40,40);
    assertEquals(Integer.MAX_VALUE, values[0],0.01);
    assertEquals(Integer.MAX_VALUE, values[1],0.01);
    assertEquals(Integer.MAX_VALUE, values[2],0.01);
    assertEquals(120, values[3],0.01);
  }

  private void findDirectionedValues(Map m,int left, int top) {
    findDirectionedValues2(m, left, top,20,20);
  }

  private void findDirectionedValues2(Map m, int left, int top,double x,double y) {
    values = new double[4];
    values[0] = MapUtil.findHitDistance(m, 90, left, top, x, y);
    values[1] = MapUtil.findHitDistance(m, 0, left, top, x, y);
    values[2] = MapUtil.findHitDistance(m, 270, left, top, x, y);
    values[3] = MapUtil.findHitDistance(m, 180, left, top, x, y);
  }

  /**
   * Test of hasTile method, of class MapUtil.
   */
  @Test
  public void testHasTile() {
    Map map = MapTestUtil.getMap();
    assertTrue(MapUtil.hasTile(map, 10.0, 10.0));
    assertTrue(MapUtil.hasTile(map, 50.0, 10.0));
    assertFalse(MapUtil.hasTile(map,150.0, 10.0));
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
