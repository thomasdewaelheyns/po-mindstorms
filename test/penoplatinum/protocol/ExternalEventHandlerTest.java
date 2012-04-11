package penoplatinum.protocol;

/**
 * ExternalEventHandlerTest
 * 
 * Tests ExternalEventHandler interface is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class ExternalEventHandlerTest extends TestCase {

  public ExternalEventHandlerTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(ExternalEventHandler.class);
	}
}
