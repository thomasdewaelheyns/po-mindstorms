package penoplatinum.grid.agent;

/**
 * ModelTest
 * 
 * Tests Model class is available
 * 
 * @author: Team Platinum
 */
import junit.framework.*;
import static org.mockito.Mockito.*;

public class AgentTest extends TestCase {

  public AgentTest(String name) {
    super(name);
  }

  public void testAvailablity() {
    mock(Agent.class);
  }
}
