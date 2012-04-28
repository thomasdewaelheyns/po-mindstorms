package penoplatinum.simulator.entities;

import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntityConfigTest extends TestCase {
  @Test
  public void testStatic(){
    assertEquals(17.5, EntityConfig.WHEEL_SIZE, 0.000001);
    assertEquals(16, EntityConfig.WHEEL_BASE, 0.000001);
    assertEquals(1, EntityConfig.MOTOR_LEFT);
    assertEquals(0, EntityConfig.MOTOR_RIGHT);
    assertEquals(2, EntityConfig.MOTOR_SONAR);
  }
}
