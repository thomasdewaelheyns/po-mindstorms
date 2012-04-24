package penoplatinum.simulator;

import junit.framework.TestCase;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SensorTest extends TestCase {
  
  @Test
  public void testAvailablity(){
    mock(Sensor.class);
  }
}
