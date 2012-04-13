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


public class GhostNavigatorTest extends TestCase {

  public GhostNavigatorTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(GhostNavigator.class);
	}
	
	/**
	 * Implementing a useful test for this class requires a LOT of setup to
	 * mock all environmental aspects like sectors with values.
	 * Both modes are also based on the same HillClimbingMode.
	 * If we even implement this test, we have nothing else to do or have found
	 * a bug that really can't be caught elsewhere ;-)
	 */
}
