package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.Test;
import penoplatinum.simulator.sensors.Motor;
import static org.junit.Assert.*;

public class MotorTest extends TestCase {
  /**
   * Test of getDirection method, of class Motor.
   */
  @Test
  public void testGetDirection() {
    Motor instance = new Motor();
    assertEquals(Motor.FORWARD, instance.getDirection());
    assertEquals(Motor.FORWARD, instance.goForward().getDirection());
    assertEquals(Motor.BACKWARD, instance.goBackward().getDirection());
  }

  /**
   * Test of isMoving method, of class Motor.
   */
  @Test
  public void testIsMoving() {
    Motor instance = new Motor();
    assertFalse(instance.isMoving());
    instance.start();
    assertTrue(instance.isMoving());
    instance.stop();
    assertFalse(instance.isMoving());
    instance.rotateBy(400);
    assertTrue(instance.isMoving());
    instance.tick(1);
    assertTrue(instance.isMoving());
    instance.tick(1);
    assertFalse(instance.isMoving());
    instance.rotateTo(130);
    assertTrue(instance.isMoving());
    instance.tick(1);
    assertTrue(instance.isMoving());
    instance.tick(1);
    assertFalse(instance.isMoving());
  }

  /**
   * Test of setLabel method, of class Motor.
   */
  @Test
  public void testSetLabel() {
    String label = "azerty";
    Motor instance = new Motor();
    Motor result = instance.setLabel(label);
    assertEquals(instance, result);
    assertEquals(label, instance.getLabel());
  }

  /**
   * Test of setSpeed method, of class Motor.
   */
  @Test
  public void testSetSpeed() {
    Motor instance = new Motor();
    instance.start().tick(1);
    assertEquals(250, instance.getValue());
    Motor result = instance.setSpeed(400);
    assertEquals(instance, result);
    instance.tick(1);
    assertEquals(650, instance.getValue());

  }

  /**
   * Test of start method, of class Motor.
   */
  @Test
  public void testStart() {
    Motor instance = new Motor();
    assertEquals(instance, instance.start());
    assertTrue(instance.isMoving());
    int temp = instance.getValue();
    instance.tick(200000);
    assertTrue(instance.isMoving());
    assertTrue(temp < instance.getValue());
  }

  /**
   * Test of stop method, of class Motor.
   */
  @Test
  public void testStop() {
    Motor instance = new Motor();
    instance.start();
    assertTrue(instance.isMoving());
    Motor result = instance.stop();
    assertEquals(instance, result);
    assertFalse(instance.isMoving());
    int temp = instance.getValue();
    instance.tick(1);
    assertEquals(temp, instance.getValue());
  }

  /**
   * Test of rotateTo method, of class Motor.
   */
  @Test
  public void testRotateTo() {
    Motor instance = new Motor();
    Motor result = instance.rotateTo(400);
    assertEquals(instance, result);
    assertEquals(0, instance.getValue());
    assertTrue(instance.isMoving());
    instance.tick(1);
    assertEquals(250, instance.getValue());
    assertTrue(instance.isMoving());
    instance.tick(1);
    assertEquals(400, instance.getValue());
    assertFalse(instance.isMoving());
  }

  /**
   * Test of rotateBy method, of class Motor.
   */
  @Test
  public void testRotateBy() {
    Motor instance = new Motor();
    instance.setSpeed(400);
    instance.rotateBy(398);
    instance.tick(0.5);
    assertEquals(200, instance.getValue());
    instance.tick(0.5);
    assertEquals(398, instance.getValue());
    instance.tick(0.5);
    assertEquals(398, instance.getValue());

    instance.rotateBy(-400);
    instance.tick(0.5);
    assertEquals(198, instance.getValue());
    instance.tick(0.5);
    assertEquals(-2, instance.getValue());
    instance.tick(0.5);
    assertEquals(-2, instance.getValue());
  }

  /**
   * Test of goForward method, of class Motor.
   */
  @Test
  public void testGoForward() {
    Motor instance = new Motor();
    Motor result = instance.goForward();
    assertEquals(Motor.FORWARD, instance.getDirection());
    assertEquals(instance, result);
  }

  /**
   * Test of goBackward method, of class Motor.
   */
  @Test
  public void testGoBackward() {
    Motor instance = new Motor();
    Motor result = instance.goBackward();
    assertEquals(Motor.BACKWARD, instance.getDirection());
    assertEquals(instance, result);
  }

  /**
   * Test of toggleDirection method, of class Motor.
   */
  @Test
  public void testToggleDirection() {
    Motor instance = new Motor();
    int prev = instance.getDirection();
    Motor result = instance.toggleDirection();
    assertEquals(prev * -1, result.getDirection());
    assertEquals(instance, result);
  }

  /**
   * Test of getValue method, of class Motor.
   */
  @Test
  public void testGetValue() {
    Motor instance = new Motor();
    assertEquals(0, instance.getValue());
    instance.tick(1);
    assertEquals(0, instance.getValue());
    instance.setSpeed(400).goForward().start();
    instance.tick(1);
    assertEquals(400, instance.getValue());
    instance.tick(1);
    assertEquals(800, instance.getValue());
    instance.goBackward().start();
    instance.tick(1);
    assertEquals(400, instance.getValue());
  }
}
