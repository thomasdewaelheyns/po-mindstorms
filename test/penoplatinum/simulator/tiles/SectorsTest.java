package penoplatinum.simulator.tiles;

/**
 * SectorsTest
 * 
 * Tests Sectors data class is available
 * 
 * @author: Team Platinum
 */
import junit.framework.*;
import static org.mockito.Mockito.*;

public class SectorsTest extends TestCase {

  public SectorsTest(String name) {
    super(name);
  }

  public void testAvailablity() {
    mock(Sectors.class);
  }
}
