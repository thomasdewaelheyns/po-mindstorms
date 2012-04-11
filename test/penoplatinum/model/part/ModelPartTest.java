package penoplatinum.model.part;

/**
 * ModelPartTest
 * 
 * Tests ModelPart interface is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class ModelPartTest extends TestCase {

  public ModelPartTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
		mock(ModelPart.class);
	}
}
