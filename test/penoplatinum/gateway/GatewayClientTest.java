package penoplatinum.gateway;

/**
 * GatewayClientTest
 * 
 * Tests GatewayClient interface is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class GatewayClientTest extends TestCase {

  public GatewayClientTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(GatewayClient.class);
	}
}
