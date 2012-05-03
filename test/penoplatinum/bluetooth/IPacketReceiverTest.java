package penoplatinum.bluetooth;

/**
 * IPacketReceiverTest
 * 
 * Tests IPacketReceiver class is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class IPacketReceiverTest extends TestCase {

  public IPacketReceiverTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(IPacketReceiver.class);
	}
}
