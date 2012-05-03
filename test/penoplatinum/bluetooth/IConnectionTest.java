package penoplatinum.bluetooth;

/**
 * IConnectionTest
 * 
 * Tests IConnection class is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class IConnectionTest extends TestCase {

  public IConnectionTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(IConnection.class);
	}
}
