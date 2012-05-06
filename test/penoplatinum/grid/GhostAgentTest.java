package penoplatinum.grid;

/**
 * ModelTest
 * 
 * Tests Model class is available
 * 
 * @author: Team Platinum
 */
import penoplatinum.grid.agent.GhostAgent;
import junit.framework.*;
import penoplatinum.util.Color;
import static org.mockito.Mockito.*;

public class GhostAgentTest extends TestCase {

  public GhostAgentTest(String name) {
    super(name);
  }

  public void testAvailablity() {
    GhostAgent ghost = new GhostAgent("Joske", new Color(255,0,0));
    assertEquals("Joske", ghost.getName());
    assertEquals(new Color(255,0,0), ghost.getColor());
  }
}
