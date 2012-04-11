package penoplatinum.gateway;

/**
 * ConnectionTest
 * 
 * Tests Connection interface is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class ConnectionTest extends TestCase {

  public ConnectionTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(Connection.class);
	}
}
