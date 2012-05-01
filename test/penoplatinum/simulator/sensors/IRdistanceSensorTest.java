package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.Test;
import static org.mockito.Mockito.*;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.entities.PacmanEntity;
import penoplatinum.util.Point;

public class IRdistanceSensorTest extends TestCase {

  /**
   * Test of getValue method, of class IRdistanceSensor.
   */
  @Test
  public void testGetValue() {
    IRdistanceSensor instance = new IRdistanceSensor(30);
    PacmanEntity p = mock(PacmanEntity.class);
    when(p.getPosX()).thenReturn(30.0);
    when(p.getPosY()).thenReturn(30.0);
    Simulator sim = mock(Simulator.class);
    when(sim.getPacMan()).thenReturn(p);
    when(sim.getFreeDistance(new Point(1, 1), new Point(20, 20), 315)).thenReturn(100);

    SimulatedEntity entity = mock(SimulatedEntity.class);
    when(entity.getCurrentOnTileCoordinates()).thenReturn(new Point(20, 20));
    when(entity.getCurrentTileCoordinates()).thenReturn(new Point(1, 1));
    when(entity.getPosX()).thenReturn(20.0);
    when(entity.getPosY()).thenReturn(20.0);

    instance.useSimulatedEntity(entity);
    instance.useSimulator(sim);

    when(entity.getDirection()).thenReturn(240.0);
    assertEquals(170, instance.getValue());
    when(entity.getDirection()).thenReturn(241.0);
    assertEquals(0, instance.getValue());
    when(entity.getDirection()).thenReturn(150.0);
    assertEquals(170, instance.getValue());
    when(entity.getDirection()).thenReturn(149.0);
    assertEquals(0, instance.getValue());
  }

  /**
   * Test of useSimulator method, of class IRdistanceSensor.
   */
  @Test
  public void testUseSimulator() {
    IRdistanceSensor instance = new IRdistanceSensor(30);
    PacmanEntity p = mock(PacmanEntity.class);
    when(p.getPosX()).thenReturn(30.0);
    when(p.getPosY()).thenReturn(30.0);
    Simulator sim = mock(Simulator.class);
    when(sim.getPacMan()).thenReturn(p);
    when(sim.getFreeDistance(new Point(1, 1), new Point(20, 20), 315)).thenReturn(100);

    SimulatedEntity entity = mock(SimulatedEntity.class);
    when(entity.getCurrentOnTileCoordinates()).thenReturn(new Point(20, 20));
    when(entity.getCurrentTileCoordinates()).thenReturn(new Point(1, 1));
    when(entity.getPosX()).thenReturn(20.0);
    when(entity.getPosY()).thenReturn(20.0);

    instance.useSimulatedEntity(entity);
    instance.useSimulator(sim);
    instance.getValue();

    verify(sim, times(1)).getPacMan();
    verify(sim, times(1)).getFreeDistance(new Point(1, 1), new Point(20, 20), 315);
    verify(p, times(1)).getPosX();
    verify(p, times(1)).getPosY();
  }

  /**
   * Test of useSimulatedEntity method, of class IRdistanceSensor.
   */
  @Test
  public void testUseSimulatedEntity() {
    IRdistanceSensor instance = new IRdistanceSensor(30);
    PacmanEntity p = mock(PacmanEntity.class);
    when(p.getPosX()).thenReturn(30.0);
    when(p.getPosY()).thenReturn(30.0);
    Simulator sim = mock(Simulator.class);
    when(sim.getPacMan()).thenReturn(p);
    when(sim.getFreeDistance(new Point(1, 1), new Point(20, 20), 315)).thenReturn(100);

    SimulatedEntity entity = mock(SimulatedEntity.class);
    when(entity.getCurrentOnTileCoordinates()).thenReturn(new Point(20, 20));
    when(entity.getCurrentTileCoordinates()).thenReturn(new Point(1, 1));
    when(entity.getPosX()).thenReturn(20.0);
    when(entity.getPosY()).thenReturn(20.0);

    instance.useSimulatedEntity(entity);
    instance.useSimulator(sim);
    instance.getValue();
    
    verify(entity, times(1)).getPosX();
    verify(entity, times(1)).getPosY();
  }
}
