package penoplatinum.reporter;

/**
 * ReporterTest
 * 
 * Tests Reporter class is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class ReporterTest extends TestCase {

  public ReporterTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(Reporter.class);
	}
}
