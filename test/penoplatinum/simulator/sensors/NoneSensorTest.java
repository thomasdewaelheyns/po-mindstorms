package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.Test;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Simulator;

public class NoneSensorTest extends TestCase {

  /**
   * Test of getValue method, of class NoneSensor.
   */
  @Test
  public void testGetValue() {
    NoneSensor instance = new NoneSensor();
    int expResult = 0;
    int result = instance.getValue();
    assertEquals(expResult, result);
  }

  /**
   * Test of useSimulator method, of class NoneSensor.
   */
  @Test
  public void testUseSimulator() {
    Simulator sim = null;
    NoneSensor instance = new NoneSensor();
    instance.useSimulator(sim);
  }

  /**
   * Test of useSimulatedEntity method, of class NoneSensor.
   */
  @Test
  public void testUseSimulatedEntity() {
    SimulatedEntity simEntity = null;
    NoneSensor instance = new NoneSensor();
    instance.useSimulatedEntity(simEntity);
  }
}
