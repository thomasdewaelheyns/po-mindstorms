package penoplatinum.robot;

/**
 * RobotTest
 * 
 * Tests Robot interface is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class RobotTest extends TestCase {

  public RobotTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(Robot.class);
	}
}
