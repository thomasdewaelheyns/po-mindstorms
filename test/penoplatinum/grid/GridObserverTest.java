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

public class GridObserverTest extends TestCase {

  public GridObserverTest(String name) {
    super(name);
  }

  public void testAvailablity() {
    mock(GridObserver.class);
  }
}
