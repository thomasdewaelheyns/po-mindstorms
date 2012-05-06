package penoplatinum.grid;

/**
 * ModelTest
 * 
 * Tests Model class is available
 * 
 * @author: Team Platinum
 */
import penoplatinum.grid.view.GridView;
import junit.framework.*;
import static org.mockito.Mockito.*;

public class GridViewTest extends TestCase {

  public GridViewTest(String name) {
    super(name);
  }

  public void testAvailablity() {
    mock(GridView.class);
  }
}
