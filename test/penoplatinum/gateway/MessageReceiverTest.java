package penoplatinum.gateway;

/**
 * MessageReceiverTest
 * 
 * Tests MessageReceiver interface is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class MessageReceiverTest extends TestCase {

  public MessageReceiverTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(MessageReceiver.class);
	}
}
