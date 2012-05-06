package penoplatinum.grid;

/**
 * ModelTest
 * 
 * Tests Model class is available
 * 
 * @author: Team Platinum
 */
import penoplatinum.grid.agent.PacmanAgent;
import junit.framework.*;
import static org.mockito.Mockito.*;

public class PacmanAgentTest extends TestCase {

  public PacmanAgentTest(String name) {
    super(name);
  }

  public void testAvailablity() {
    PacmanAgent pacman = new PacmanAgent();
    assertEquals("pacman", pacman.getName());
  }
}
