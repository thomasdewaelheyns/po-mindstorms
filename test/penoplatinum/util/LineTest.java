package penoplatinum.util;

/**
 * LineTest
 * 
 * Tests Line interface is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class LineTest extends TestCase {

  public LineTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		Line test = Line.NONE;
		     test = Line.BLACK;
		     test = Line.WHITE;
	}
}
