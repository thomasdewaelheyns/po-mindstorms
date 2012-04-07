package penoplatinum.driver.action;

/**
 * DriverActionTest
 * 
 * Tests DriverAction class is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class DriverActionTest extends TestCase {

  public DriverActionTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(DriverAction.class);
	}
}
