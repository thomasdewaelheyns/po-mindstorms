package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.Test;
import static org.mockito.Mockito.*;
import penoplatinum.map.MapTestUtil;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Simulator;
import penoplatinum.util.Point;

public class SonarTest extends TestCase {
  /**
   * Test of getValue method, of class Sonar.
   */
  @Test
  public void testGetValue() {
    Motor m = mock(Motor.class);
    Sonar instance = new Sonar(m);
    Simulator s = new Simulator();
    s.useMap(MapTestUtil.getMap());
    instance.useSimulator(s);
    SimulatedEntity e = mock(SimulatedEntity.class);
    when(e.getCurrentTileCoordinates()).thenReturn(new Point(1,1));
    when(e.getCurrentOnTileCoordinates()).thenReturn(new Point(20, 20));
    when(e.getAngle()).thenReturn(0);
    instance.useSimulatedEntity(e);
    
    when(m.getValue()).thenReturn(0);
    assertEquals(60, instance.getValue());
    
    when(m.getValue()).thenReturn(45);
    when(e.getAngle()).thenReturn(45);
    assertEquals(20, instance.getValue());
    
    when(m.getValue()).thenReturn(-45);
    when(e.getAngle()).thenReturn(0);
    assertEquals(28, instance.getValue());
  }
}
