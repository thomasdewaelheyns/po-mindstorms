package penoplatinum.grid;

/**
 * ModelTest
 * 
 * Tests Model class is available
 * 
 * @author: Team Platinum
 */
import junit.framework.*;
import static org.mockito.Mockito.*;

public class GridTest extends TestCase {

  public GridTest(String name) {
    super(name);
  }

  public void testAvailablity() {
    mock(Grid.class);
  }
}
