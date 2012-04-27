package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.Test;
import static org.mockito.Mockito.*;
import penoplatinum.simulator.RobotEntity;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.entities.PacmanEntity;
import penoplatinum.util.Point;

public class IRSensorTest extends TestCase {
  
  /**
   * Test of getValue method, of class IRSensor.
   */
  @Test
  public void testGetValue() {
    IRSensor instance = new IRSensor();
    
    RobotEntity r = mock(PacmanEntity.class);
    when(r.getPosX()).thenReturn(20.0);
    when(r.getPosY()).thenReturn(20.0);
    Simulator sim = mock(Simulator.class);
    when(sim.getFreeDistance(any(Point.class), any(Point.class), anyInt())).thenReturn(100); //Far away
    SimulatedEntity entity = mock(SimulatedEntity.class);
    when(entity.getPosX()).thenReturn(20.0);
    when(entity.getPosY()).thenReturn(40.0);
    when(entity.getDir()).thenReturn(0.0);
    instance.useSimulatedEntity(entity);
    instance.useSimulator(sim);
    
    assertEquals(0, instance.getValue());
    when(sim.getPacMan()).thenReturn(r);
    assertEquals(5, instance.getValue());
    when(entity.getDir()).thenReturn(-90.0);
    assertEquals(2, instance.getValue());
    when(entity.getDir()).thenReturn(90.0);
    assertEquals(8, instance.getValue());
    
    when(entity.getDir()).thenReturn(-15.0);
    assertEquals(5, instance.getValue());
    when(entity.getDir()).thenReturn(-16.0);
    assertEquals(4, instance.getValue());
    when(entity.getDir()).thenReturn(14.0);
    assertEquals(5, instance.getValue());
    when(entity.getDir()).thenReturn(15.0);
    assertEquals(6, instance.getValue());
    
    
    when(entity.getDir()).thenReturn(0.0);
    assertEquals(5, instance.getValue());
    when(entity.getDir()).thenReturn(-30.0);
    assertEquals(4, instance.getValue());
    when(entity.getDir()).thenReturn(-60.0);
    assertEquals(3, instance.getValue());
    when(entity.getDir()).thenReturn(-90.0);
    assertEquals(2, instance.getValue());
    when(entity.getDir()).thenReturn(-120.0);
    assertEquals(1, instance.getValue());
    when(entity.getDir()).thenReturn(-150.0);
    assertEquals(0, instance.getValue());
    when(entity.getDir()).thenReturn(-180.0);
    assertEquals(0, instance.getValue());
    
    when(entity.getDir()).thenReturn(0.0);
    assertEquals(5, instance.getValue());
    when(entity.getDir()).thenReturn(30.0);
    assertEquals(6, instance.getValue());
    when(entity.getDir()).thenReturn(60.0);
    assertEquals(7, instance.getValue());
    when(entity.getDir()).thenReturn(90.0);
    assertEquals(8, instance.getValue());
    when(entity.getDir()).thenReturn(120.0);
    assertEquals(9, instance.getValue());
    when(entity.getDir()).thenReturn(150.0);
    assertEquals(0, instance.getValue());
    when(entity.getDir()).thenReturn(180.0);
    assertEquals(0, instance.getValue());
    
    when(sim.getFreeDistance(any(Point.class), any(Point.class), anyInt())).thenReturn(10); //Far away
    when(entity.getDir()).thenReturn(120.0);
    assertEquals(0, instance.getValue());
  }

  /**
   * Test of useSimulator method, of class IRSensor.
   */
  @Test
  public void testUseSimulator() {
    Simulator sim = mock(Simulator.class);
    when(sim.getPacMan()).thenReturn(mock(PacmanEntity.class));
    IRSensor instance = new IRSensor();
    instance.useSimulator(sim);
    instance.useSimulatedEntity(mock(SimulatedEntity.class));
    instance.getValue();
    verify(sim, times(1)).getPacMan();
    verify(sim, times(1)).getFreeDistance(any(Point.class), any(Point.class), anyInt());
  }

  /**
   * Test of useSimulatedEntity method, of class IRSensor.
   */
  @Test
  public void testUseSimulatedEntity() {
    SimulatedEntity simEntity = mock(SimulatedEntity.class);
    IRSensor instance = new IRSensor();
    instance.useSimulatedEntity(simEntity);
    Simulator sim = mock(Simulator.class);
    when(sim.getPacMan()).thenReturn(mock(PacmanEntity.class));
    instance.useSimulator(sim);
    
    instance.getValue();
    verify(simEntity, times(1)).getPosX();
    verify(simEntity, times(1)).getPosY();
  }
}
