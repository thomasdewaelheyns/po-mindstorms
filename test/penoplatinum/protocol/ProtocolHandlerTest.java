package penoplatinum.protocol;

/**
 * ProtocolHandlerTest
 * 
 * Tests if ProtocolHandler interface is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class ProtocolHandlerTest extends TestCase {

  public ProtocolHandlerTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(ProtocolHandler.class);
	}
}
