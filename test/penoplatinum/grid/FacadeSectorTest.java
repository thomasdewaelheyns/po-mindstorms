package penoplatinum.grid;

/**
 * FacadeSectorTest
 *  
 * Test availability of FacadeSector interface
 * 
 * @author Team Platinum
 */

import static org.mockito.Mockito.*;

import junit.framework.TestCase;
import penoplatinum.util.Bearing;
import penoplatinum.util.Rotation;
import penoplatinum.util.TransformationTRT;

public class FacadeSectorTest extends TestCase {

  public FacadeSectorTest(String name) {
    super(name);
  }

  public void testAvailablity() {
    mock(FacadeSector.class);
  }

}
