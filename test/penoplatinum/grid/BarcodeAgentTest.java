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

public class BarcodeAgentTest extends TestCase {
  
  public BarcodeAgentTest(String name) {
    super(name);
  }
  
  public void testAvailablity() {
    BarcodeAgent agent = BarcodeAgent.getBarcodeAgent(19);
    assertEquals(agent, BarcodeAgent.getBarcodeAgent(19));
    
  }
}
