package penoplatinum.bluetooth;

/**
 * IRemoteLoggerCallbackTest
 * 
 * Tests IRemoteLoggerCallback class is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class IRemoteLoggerCallbackTest extends TestCase {

  public IRemoteLoggerCallbackTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(IRemoteLoggerCallback.class);
	}
}
