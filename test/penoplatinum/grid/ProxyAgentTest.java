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

public class ProxyAgentTest extends TestCase {

  public ProxyAgentTest(String name) {
    super(name);
  }

  public void testAvailablity() {
    ProxyAgent proxy = new ProxyAgent("Banana");
    assertEquals("Banana", proxy.getName());
  }
}
