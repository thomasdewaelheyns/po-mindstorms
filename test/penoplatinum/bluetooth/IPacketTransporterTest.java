package penoplatinum.bluetooth;

/**
 * IPacketTransporterTest
 * 
 * Tests IPacketTransporter class is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class IPacketTransporterTest extends TestCase {

  public IPacketTransporterTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(IPacketTransporter.class);
	}
}
