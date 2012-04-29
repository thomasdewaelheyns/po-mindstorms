package penoplatinum.ui.admin;

/**
 * RobotAdminShellRunnerTest
 * 
 * Tests RobotAdminShellRunner class is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class RobotAdminShellRunnerTest extends TestCase {

  public RobotAdminShellRunnerTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(RobotAdminShellRunner.class);
	}
}
