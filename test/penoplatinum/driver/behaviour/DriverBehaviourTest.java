package penoplatinum.driver.behaviour;

/**
 * DriverBehaviourTest
 * 
 * Tests DriverBehaviour class is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class DriverBehaviourTest extends TestCase {

  public DriverBehaviourTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(DriverBehaviour.class);
	}
}
