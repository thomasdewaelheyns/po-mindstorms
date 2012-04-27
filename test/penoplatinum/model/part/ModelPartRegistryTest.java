package penoplatinum.model.part;

/**
 * ModelPartRegistryTest
 * 
 * Tests ModelPartRegistry hasn't changed 
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class ModelPartRegistryTest extends TestCase {

  public ModelPartRegistryTest(String name) { 
    super(name);
  }

  public void testAvailablity() {
    assertEquals(0, ModelPartRegistry.SENSOR_MODEL_PART  );
    assertEquals(1, ModelPartRegistry.WALLS_MODEL_PART   );
    assertEquals(2, ModelPartRegistry.BARCODE_MODEL_PART );
    assertEquals(3, ModelPartRegistry.LIGHT_MODEL_PART   );
    assertEquals(4, ModelPartRegistry.GRID_MODEL_PART    );
    assertEquals(5, ModelPartRegistry.MESSAGE_MODEL_PART );
    assertEquals(6, ModelPartRegistry.SONAR_MODEL_PART   );
	}
}
