package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.Test;
import penoplatinum.map.MapTestUtil;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import penoplatinum.map.Map;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.tiles.Sector;
import penoplatinum.util.Point;

public class LightSensorTest extends TestCase {

  /**
   * Test of getValue method, of class LightSensor.
   */
  @Test
  public void testGetValue() {
    System.out.println("getValue");
    LightSensor instance = new LightSensor();
    LightSensor spy = spy(instance);
    for (int i = 0; i < LightSensor.LIGHTBUFFER_SIZE; i++) {
      doReturn(i).when(spy).getActualValue();
      assertEquals(LightSensor.BROWN, spy.getValue());
    }
    for (int i = 0; i < LightSensor.LIGHTBUFFER_SIZE; i++) {
      doReturn(i).when(spy).getActualValue();
      assertEquals(i, spy.getValue());
    }
  }

  /**
   * Test of getActualValue method, of class LightSensor.
   */
  @Test
  public void testGetActualValue() {
    System.out.println("getActualValue");
    LightSensor instance = new LightSensor();

    Simulator sim = mock(Simulator.class);
    when(sim.getTileSize()).thenReturn(40);
    Map map = MapTestUtil.getMap();
    when(sim.getMap()).thenReturn(map);

    SimulatedEntity entity = mock(SimulatedEntity.class);
    when(entity.getDir()).thenReturn(90.0);
    when(entity.getCurrentTileCoordinates()).thenReturn(new Point(1, 1));
    when(entity.getCurrentOnTileCoordinates()).thenReturn(new Point(20, 20));

    instance.useSimulatedEntity(entity);
    instance.useSimulator(sim);

    when(map.get(1, 1).getColorAt(anyInt(), anyInt())).thenReturn(Sector.WHITE);
    assertEquals(LightSensor.WHITE, instance.getActualValue());

    when(map.get(1, 1).getColorAt(anyInt(), anyInt())).thenReturn(Sector.NO_COLOR);
    assertEquals(LightSensor.BROWN, instance.getActualValue());

    when(map.get(1, 1).getColorAt(anyInt(), anyInt())).thenReturn(Sector.BLACK);
    assertEquals(LightSensor.BLACK, instance.getActualValue());

    when(map.get(1, 1).getColorAt(15, 20)).thenReturn(Sector.WHITE);
    assertEquals(LightSensor.WHITE, instance.getActualValue());
    when(entity.getDir()).thenReturn(0.0);
    assertEquals(LightSensor.BLACK, instance.getActualValue());
  }

  @Test
  public void testGetActualValueOverFlow() {
    LightSensor instance = new LightSensor();
    Simulator sim = mock(Simulator.class);
    when(sim.getTileSize()).thenReturn(40);
    Map map = MapTestUtil.getMap();
    when(sim.getMap()).thenReturn(map);
    SimulatedEntity entity = mock(SimulatedEntity.class);
    when(entity.getDir()).thenReturn(90.0);
    when(entity.getCurrentTileCoordinates()).thenReturn(new Point(2, 2));
    when(entity.getCurrentOnTileCoordinates()).thenReturn(new Point(3, 3));
    instance.useSimulatedEntity(entity);
    instance.useSimulator(sim);

    when(map.get(2, 2).getColorAt(anyInt(), anyInt())).thenReturn(Sector.WHITE);
    when(map.get(1, 2).getColorAt(38, 3)).thenReturn(Sector.NO_COLOR);
    when(map.get(2, 1).getColorAt(3, 38)).thenReturn(Sector.BLACK);
    when(entity.getDir()).thenReturn(0.0);
    when(entity.getCurrentTileCoordinates()).thenReturn(new Point(2, 2));
    assertEquals(LightSensor.BLACK, instance.getActualValue());
    when(entity.getDir()).thenReturn(90.0);
    when(entity.getCurrentTileCoordinates()).thenReturn(new Point(2, 2));
    assertEquals(LightSensor.BROWN, instance.getActualValue());
    when(entity.getDir()).thenReturn(180.0);
    when(entity.getCurrentTileCoordinates()).thenReturn(new Point(2, 2));
    assertEquals(LightSensor.WHITE, instance.getActualValue());
    
    when(entity.getCurrentOnTileCoordinates()).thenReturn(new Point(38, 38));
    when(map.get(1, 1).getColorAt(anyInt(), anyInt())).thenReturn(Sector.WHITE);
    when(map.get(1, 2).getColorAt(2, 3)).thenReturn(Sector.NO_COLOR);
    when(map.get(2, 1).getColorAt(3, 2)).thenReturn(Sector.BLACK);
    when(entity.getDir()).thenReturn(0.0);
    when(entity.getCurrentTileCoordinates()).thenReturn(new Point(1, 1));
    assertEquals(LightSensor.WHITE, instance.getActualValue());
    when(entity.getDir()).thenReturn(-90.0);
    when(entity.getCurrentTileCoordinates()).thenReturn(new Point(1, 1));
    assertEquals(LightSensor.BLACK, instance.getActualValue());
    when(entity.getDir()).thenReturn(-180.0);
    when(entity.getCurrentTileCoordinates()).thenReturn(new Point(1, 1));
    assertEquals(LightSensor.BROWN, instance.getActualValue());
  }

  /**
   * Test of useSimulator method, of class LightSensor.
   */
  @Test
  public void testUseSimulator() {
    System.out.println("useSimulator");
    LightSensor instance = new LightSensor();
    
    Simulator sim = mock(Simulator.class);
    SimulatedEntity entity = mock(SimulatedEntity.class);
    instance.useSimulator(sim);
    instance.useSimulatedEntity(entity);
    when(entity.getCurrentOnTileCoordinates()).thenReturn(new Point(20, 20));
    when(entity.getCurrentTileCoordinates()).thenReturn(new Point(2, 2));
    Map map = MapTestUtil.getMap();
    when(sim.getMap()).thenReturn(map);
    when(sim.getTileSize()).thenReturn((Integer) 40);
    
    instance.getValue();
    verify(sim, times(4)).getTileSize();
    verify(map.get(2, 2), times(1)).getColorAt(anyInt(), anyInt());
  }

  /**
   * Test of useSimulatedEntity method, of class LightSensor.
   */
  @Test
  public void testUseSimulatedEntity() {
    System.out.println("useSimulatedEntity");
    LightSensor instance = new LightSensor();
    
    Simulator sim = mock(Simulator.class);
    SimulatedEntity entity = mock(SimulatedEntity.class);
    instance.useSimulator(sim);
    instance.useSimulatedEntity(entity);
    when(entity.getCurrentOnTileCoordinates()).thenReturn(new Point(20, 20));
    when(entity.getCurrentTileCoordinates()).thenReturn(new Point(2, 2));
    Map map = MapTestUtil.getMap();
    when(sim.getMap()).thenReturn(map);
    when(sim.getTileSize()).thenReturn((Integer) 40);
    
    instance.getValue();
    verify(entity, times(1)).getDir();
    verify(entity, times(1)).getCurrentOnTileCoordinates();
    verify(entity, times(1)).getCurrentTileCoordinates();
  }
}
