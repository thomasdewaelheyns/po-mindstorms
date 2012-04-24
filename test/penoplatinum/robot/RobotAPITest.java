package penoplatinum.robot;

/**
 * RobotTest
 * 
 * Tests Robot interface is available
 * 
 * @author: Team Platinum
 */
import junit.framework.*;
import static org.mockito.Mockito.*;

public class RobotAPITest extends TestCase {

  public RobotAPITest(String name) {
    super(name);
  }

  public void testAvailablity() {
    mock(RobotAPI.class);
  }
}
