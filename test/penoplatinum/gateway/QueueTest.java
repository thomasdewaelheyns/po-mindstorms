package penoplatinum.gateway;

/**
 * QueueTest
 * 
 * Tests Queue interface is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class QueueTest extends TestCase {

  public QueueTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(Queue.class);
	}
}
