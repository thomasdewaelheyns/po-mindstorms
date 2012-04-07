package penoplatinum.driver;

/**
 * DriverTest
 * 
 * Tests Driver class is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class DriverTest extends TestCase {

  public DriverTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(Driver.class);
	}
}
