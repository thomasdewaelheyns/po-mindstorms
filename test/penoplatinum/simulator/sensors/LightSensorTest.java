package penoplatinum.simulator.sensors;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Simulator;

public class LightSensorTest extends TestCase {
  /**
   * Test of getValue method, of class LightSensor.
   */
  @Test
  public void testGetValue() {
    System.out.println("getValue");
    LightSensor instance = new LightSensor();
    LightSensor spy = spy(instance);
    for(int i = 0; i < LightSensor.LIGHTBUFFER_SIZE; i++){
      doReturn(i).when(spy).getActualValue();
      assertEquals(LightSensor.BROWN, spy.getValue());
    }
    for(int i = 0; i < LightSensor.LIGHTBUFFER_SIZE; i++){
      doReturn(i).when(spy).getActualValue();
      assertEquals(i, spy.getValue());
    }
  }

  /**
   * Test of getActualValue method, of class LightSensor.
   */
  @Test
  public void testGetActualValue() {
    System.out.println("getActualValue");
    LightSensor instance = new LightSensor();
    Simulator sim = mock(Simulator.class);
    when(sim.getTileSize()).thenReturn(40);
    
    SimulatedEntity entity = mock(SimulatedEntity.class);
    when(entity.getDir()).thenReturn(0.0);
    when(entity.getPosX()).thenReturn(20.0);
    when(entity.getPosY()).thenReturn(20.0);
    
    assertEquals(expResult, instance.getActualValue());
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of useSimulator method, of class LightSensor.
   */
  @Test
  public void testUseSimulator() {
    System.out.println("useSimulator");
    Simulator sim = null;
    LightSensor instance = new LightSensor();
    instance.useSimulator(sim);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of useSimulatedEntity method, of class LightSensor.
   */
  @Test
  public void testUseSimulatedEntity() {
    System.out.println("useSimulatedEntity");
    SimulatedEntity simEntity = null;
    LightSensor instance = new LightSensor();
    instance.useSimulatedEntity(simEntity);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
}

