/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rupsbant
 */
public class MotorStateTest extends TestCase {

  /**
   * Test of getValue method, of class MotorState.
   */
  @Test
  public void testGetValue() {
    System.out.println("getValue");
    Motor m = new Motor();
    MotorState instance = new MotorState(m);
    assertEquals(MotorState.MOTORSTATE_STOPPED, instance.getValue());
    m.goForward().start();
    assertEquals(MotorState.MOTORSTATE_FORWARD, instance.getValue());
    m.goBackward().start();
    assertEquals(MotorState.MOTORSTATE_BACKWARD, instance.getValue());
  }
}
