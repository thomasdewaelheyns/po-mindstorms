package penoplatinum.navigator.mode;

/**
 * NavigatorModeTest
 * 
 * Tests NavigatorMode interface is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class NavigatorModeTest extends TestCase {

  public NavigatorModeTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(NavigatorMode.class);
	}
}
