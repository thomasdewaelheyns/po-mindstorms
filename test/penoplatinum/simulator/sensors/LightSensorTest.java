package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.Test;
import penoplatinum.map.MapTestUtil;
import static org.mockito.Mockito.*;
import penoplatinum.map.Map;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.tiles.Sector;

public class LightSensorTest extends TestCase {

  /**
   * Test of getValue method, of class LightSensor.
   */
  @Test
  public void testGetValue() {
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
    LightSensor instance = new LightSensor();

    Simulator sim = mock(Simulator.class);
    when(sim.getTileSize()).thenReturn(40);
    Map map = MapTestUtil.getMap();
    when(sim.getMap()).thenReturn(map);

    SimulatedEntity entity = mock(SimulatedEntity.class);
    when(entity.getPosX()).thenReturn(20.0);
    when(entity.getPosY()).thenReturn(20.0);
    when(entity.getDirection()).thenReturn(90.0);

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
    when(entity.getDirection()).thenReturn(0.0);
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
    when(entity.getPosX()).thenReturn(43.0);
    when(entity.getPosY()).thenReturn(43.0);
    when(entity.getDirection()).thenReturn(90.0);
    instance.useSimulatedEntity(entity);
    instance.useSimulator(sim);

    when(map.get(2, 2).getColorAt(anyInt(), anyInt())).thenReturn(Sector.WHITE);
    when(map.get(1, 2).getColorAt(38, 3)).thenReturn(Sector.NO_COLOR);
    when(map.get(2, 1).getColorAt(3, 38)).thenReturn(Sector.BLACK);
    when(entity.getDirection()).thenReturn(0.0);
    assertEquals(LightSensor.BLACK, instance.getActualValue());
    when(entity.getDirection()).thenReturn(90.0);
    assertEquals(LightSensor.BROWN, instance.getActualValue());
    when(entity.getDirection()).thenReturn(180.0);
    assertEquals(LightSensor.WHITE, instance.getActualValue());
    
    
    when(entity.getPosX()).thenReturn(78.0);
    when(entity.getPosY()).thenReturn(78.0);
    when(map.get(1, 1).getColorAt(anyInt(), anyInt())).thenReturn(Sector.WHITE);
    when(map.get(1, 2).getColorAt(2, 3)).thenReturn(Sector.NO_COLOR);
    when(map.get(2, 1).getColorAt(3, 2)).thenReturn(Sector.BLACK);
    when(entity.getDirection()).thenReturn(0.0);
    
    when(entity.getPosX()).thenReturn(38.0);
    when(entity.getPosY()).thenReturn(38.0);
    assertEquals(LightSensor.WHITE, instance.getActualValue());
    when(entity.getDirection()).thenReturn(-90.0);
    assertEquals(LightSensor.BLACK, instance.getActualValue());
    when(entity.getDirection()).thenReturn(-180.0);
    assertEquals(LightSensor.BROWN, instance.getActualValue());
  }

  /**
   * Test of useSimulator method, of class LightSensor.
   */
  @Test
  public void testUseSimulator() {
    LightSensor instance = new LightSensor();
    
    Simulator sim = mock(Simulator.class);
    SimulatedEntity entity = mock(SimulatedEntity.class);
    instance.useSimulator(sim);
    instance.useSimulatedEntity(entity);
    when(entity.getPosX()).thenReturn(60.0);
    when(entity.getPosY()).thenReturn(60.0);
    Map map = MapTestUtil.getMap();
    when(sim.getMap()).thenReturn(map);
    when(sim.getTileSize()).thenReturn((Integer) 40);
    
    instance.getValue();
    verify(sim, times(6)).getTileSize();
    verify(map.get(2, 2), times(1)).getColorAt(anyInt(), anyInt());
  }

  /**
   * Test of useSimulatedEntity method, of class LightSensor.
   */
  @Test
  public void testUseSimulatedEntity() {
    LightSensor instance = new LightSensor();
    
    Simulator sim = mock(Simulator.class);
    SimulatedEntity entity = mock(SimulatedEntity.class);
    instance.useSimulator(sim);
    instance.useSimulatedEntity(entity);
    when(entity.getPosX()).thenReturn(60.0);
    when(entity.getPosY()).thenReturn(60.0);
    Map map = MapTestUtil.getMap();
    when(sim.getMap()).thenReturn(map);
    when(sim.getTileSize()).thenReturn((Integer) 40);
    
    instance.getValue();
    verify(entity, times(1)).getDirection();
    verify(entity, times(2)).getPosX();
    verify(entity, times(2)).getPosY();
  }
}
