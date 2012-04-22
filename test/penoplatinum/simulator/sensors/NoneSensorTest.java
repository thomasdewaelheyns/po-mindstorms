package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.Simulator;

public class NoneSensorTest extends TestCase {

  /**
   * Test of getValue method, of class NoneSensor.
   */
  @Test
  public void testGetValue() {
    System.out.println("getValue");
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
    System.out.println("useSimulator");
    Simulator sim = null;
    NoneSensor instance = new NoneSensor();
    instance.useSimulator(sim);
  }

  /**
   * Test of useSimulatedEntity method, of class NoneSensor.
   */
  @Test
  public void testUseSimulatedEntity() {
    System.out.println("useSimulatedEntity");
    SimulatedEntity simEntity = null;
    NoneSensor instance = new NoneSensor();
    instance.useSimulatedEntity(simEntity);
  }
}
