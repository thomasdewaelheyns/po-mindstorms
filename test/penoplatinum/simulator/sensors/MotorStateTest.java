package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.Test;
import penoplatinum.simulator.entities.SensorConfig;

public class MotorStateTest extends TestCase {

  /**
   * Test of getValue method, of class MotorState.
   */
  @Test
  public void testGetValue() {
    System.out.println("getValue");
    Motor m = new Motor();
    MotorState instance = new MotorState(m);
    assertEquals(SensorConfig.MOTORSTATE_STOPPED, instance.getValue());
    m.goForward().start();
    assertEquals(SensorConfig.MOTORSTATE_FORWARD, instance.getValue());
    m.goBackward().start();
    assertEquals(SensorConfig.MOTORSTATE_BACKWARD, instance.getValue());
  }
}
