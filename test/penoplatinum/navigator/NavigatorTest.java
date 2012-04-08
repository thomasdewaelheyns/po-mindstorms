package penoplatinum.navigator;

/**
 * NavigatorTest
 * 
 * Tests Navigator class is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class NavigatorTest extends TestCase {

  public NavigatorTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(Navigator.class);
	}
}
