package penoplatinum.model;

/**
 * ModelTest
 * 
 * Tests Model class is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class ModelTest extends TestCase {

  public ModelTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(Model.class);
	}
}
