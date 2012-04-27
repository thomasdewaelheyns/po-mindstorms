package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.Test;

import penoplatinum.Config;


public class MotorStateTest extends TestCase {

  /**
   * Test of getValue method, of class MotorState.
   */
  @Test
  public void testGetValue() {
    Motor m = new Motor();
    MotorState instance = new MotorState(m);
    assertEquals(Config.MOTORSTATE_STOPPED, instance.getValue());
    m.goForward().start();
    assertEquals(Config.MOTORSTATE_FORWARD, instance.getValue());
    m.goBackward().start();
    assertEquals(Config.MOTORSTATE_BACKWARD, instance.getValue());
  }
}
