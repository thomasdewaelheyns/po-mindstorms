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

  public void testSettings() {
	  boolean debugMode       = Config.DEBUGMODE;
 		String mq_server        = Config.MQ_SERVER;
 		String ghost_channel    = Config.GHOST_CHANNEL;
 		boolean retardedNewLine = Config.PROTOCOL_USE_RETARDEDNEWLINE;
 		boolean use_local_mq    = Config.USE_LOCAL_MQ;
 		int speed_mode          = Config.MOTOR_SPEED_MOVE;
 		int speed_sonar         = Config.MOTOR_SPEED_SONAR;
	}
	
	public void testMagicNumbers() {
  	assertEquals( Config.BT_GHOST_PROTOCOL, 568348043 );
    assertEquals( Config.BT_LOG,            672631252 );
   	assertEquals( Config.BT_START_LOG,      356356545 );

   	assertEquals( Config.BT_MODEL,          123 );
   	assertEquals( Config.BT_WALLS,          124 );
   	assertEquals( Config.BT_VALUES,         125 );
   	assertEquals( Config.BT_AGENTS,         126 );
	}
}