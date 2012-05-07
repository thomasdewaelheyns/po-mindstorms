package penoplatinum.grid.agent;

/**
 * ModelTest
 * 
 * Tests Model class is available
 * 
 * @author: Team Platinum
 */
import junit.framework.*;

public class StaticTargetAgentTest extends TestCase {

  public StaticTargetAgentTest(String name) {
    super(name);
  }

  public void testAvailablity() {
    StaticTargetAgent agent = new StaticTargetAgent();
    assertEquals("target", agent.getName());
  }
}
