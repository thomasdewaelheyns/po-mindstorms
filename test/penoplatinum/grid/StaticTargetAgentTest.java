package penoplatinum.grid;

/**
 * ModelTest
 * 
 * Tests Model class is available
 * 
 * @author: Team Platinum
 */
import penoplatinum.grid.agent.StaticTargetAgent;
import junit.framework.*;
import static org.mockito.Mockito.*;

public class StaticTargetAgentTest extends TestCase {

  public StaticTargetAgentTest(String name) {
    super(name);
  }

  public void testAvailablity() {
    StaticTargetAgent agent = new StaticTargetAgent();
    assertEquals("target", agent.getName());
  }
}
