package penoplatinum.grid;

/**
 * ModelTest
 * 
 * Tests Model class is available
 * 
 * @author: Team Platinum
 */
import penoplatinum.model.*;
import junit.framework.*;
import static org.mockito.Mockito.*;

public class SectorTest extends TestCase {

  public SectorTest(String name) {
    super(name);
  }

  public void testAvailablity() {
    mock(Sector.class);
  }
}
