package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class TickableTest extends TestCase {
  @Test
  public void testAvailablity(){
    mock(Tickable.class);
  }
}
