package penoplatinum;

/**
 * ConfigTest
 * 
 * Tests Config class is available and contains minimal expected configuration
 * 
 * @author: Team Platinum
 */
import junit.framework.*;
import static org.mockito.Mockito.*;

public class ConfigTest extends TestCase {

  public ConfigTest(String name) {
    super(name);
  }

  public void testProperties() {
    // path verschilt tussen command-line en netbeans
    try {
      Config.load("test.properties");
    } catch (RuntimeException e) {
      Config.load("../../test/test.properties");
    }

    assertTrue(Config.DEBUGMODE);
    assertTrue(Config.USE_LOCAL_MQ);
    assertEquals("localhost-test", Config.MQ_SERVER);
    assertEquals("ghost-test", Config.GHOST_CHANNEL);
  }

  public void testMotorSpeeds() {
    int speed_mode = Config.MOTOR_SPEED_MOVE;
    int speed_sonar = Config.MOTOR_SPEED_SONAR;
  }

  public void testMagicNumbers() {
    assertEquals(Config.BT_GHOST_PROTOCOL, 568348043);
    assertEquals(Config.BT_LOG, 672631252);
    assertEquals(Config.BT_START_LOG, 356356545);

    assertEquals(Config.BT_MODEL, 123);
    assertEquals(Config.BT_WALLS, 124);
    assertEquals(Config.BT_VALUES, 125);
    assertEquals(Config.BT_AGENTS, 126);
    
    assertEquals(Config.WALL_DISTANCE, 35);
  }

  public void testPhysicalConfig() {
    assertEquals(17.5, Config.WHEEL_SIZE, 0.000001);
    assertEquals(16,   Config.WHEEL_BASE, 0.000001);
    assertEquals( 1,   Config.MOTOR_LEFT);
    assertEquals( 0,   Config.MOTOR_RIGHT);
    assertEquals( 2,   Config.MOTOR_SONAR);
  }
}
