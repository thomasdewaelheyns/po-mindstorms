package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.Test;
import penoplatinum.simulator.entities.RobotConfig;

public class MotorStateTest extends TestCase {

  /**
   * Test of getValue method, of class MotorState.
   */
  @Test
  public void testGetValue() {
    System.out.println("getValue");
    Motor m = new Motor();
    MotorState instance = new MotorState(m);
    assertEquals(RobotConfig.MOTORSTATE_STOPPED, instance.getValue());
    m.goForward().start();
    assertEquals(RobotConfig.MOTORSTATE_FORWARD, instance.getValue());
    m.goBackward().start();
    assertEquals(RobotConfig.MOTORSTATE_BACKWARD, instance.getValue());
  }
}
