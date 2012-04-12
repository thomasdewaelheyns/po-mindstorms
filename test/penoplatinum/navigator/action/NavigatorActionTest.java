package penoplatinum.navigator.action;

/**
 * NavigatorActionTest
 * 
 * Tests NavigatorAction interface is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class NavigatorActionTest extends TestCase {

  public NavigatorActionTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(NavigatorAction.class);
	}
}
