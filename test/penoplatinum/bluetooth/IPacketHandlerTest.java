package penoplatinum.bluetooth;

/**
 * IPacketHandlerTest
 * 
 * Tests IPacketHandler class is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class IPacketHandlerTest extends TestCase {

  public IPacketHandlerTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(IPacketHandler.class);
	}
}
