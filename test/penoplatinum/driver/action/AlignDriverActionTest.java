package penoplatinum.driver.action;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import penoplatinum.driver.action.subaction.SubAction;
import penoplatinum.driver.action.subaction.TurnToAlign;
import penoplatinum.model.Model;
import penoplatinum.model.part.LightModelPart;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.robot.RobotAPI;
import penoplatinum.util.LightColor;

public class AlignDriverActionTest extends TestCase {
  private SensorModelPart sensorMock = mock(SensorModelPart.class);
  private LightModelPart lightMock = mock(LightModelPart.class);
  private Model modelMock = mock(Model.class);
  private RobotAPI mockAPI = mock(RobotAPI.class);
  
  
  @Before
  public void setUp() {
    when(modelMock.getPart(ModelPartRegistry.LIGHT_MODEL_PART)).thenReturn(lightMock);
    when(modelMock.getPart(ModelPartRegistry.SENSOR_MODEL_PART)).thenReturn(sensorMock);
  }

  /**
   * Test of getSensorPart method, of class AlignDriverAction.
   */
  @Test
  public void testGetSensorPart() {
    AlignDriverAction instance = new AlignDriverAction(modelMock);
    assertEquals(sensorMock, instance.getSensorPart());
  }

  /**
   * Test of getLight method, of class AlignDriverAction.
   */
  @Test
  public void testGetLight() {
    AlignDriverAction instance = new AlignDriverAction(modelMock);
    assertEquals(lightMock, instance.getLightPart());
  }

  /**
   * Test of isBusy method, of class AlignDriverAction.
   */
  @Test
  public void testIsBusy() {
    AlignDriverAction instance = new AlignDriverAction(modelMock);
    assertEquals(true, instance.isBusy());
    
    instance.work(mockAPI); //Start findLeftWhiteLine, busy
    verify(mockAPI).turn(anyInt());
    assertEquals(true, instance.isBusy());
  }

  /**
   * Test of canBeInterrupted method, of class AlignDriverAction.
   */
  @Test
  public void testCanBeInterrupted() {
    AlignDriverAction instance = new AlignDriverAction(modelMock);
    assertEquals(false, instance.canBeInterrupted());
  }

  /**
   * Test of interrupts method, of class AlignDriverAction.
   */
  @Test
  public void testInterrupts() {
    AlignDriverAction instance = new AlignDriverAction(modelMock);
    assertEquals(false, instance.interrupts());
  }

  /**
   * Test of work method, of class AlignDriverAction.
   */
  @Test
  public void testWork() {
    SubAction action = mock(SubAction.class);
    AlignDriverAction instance = new AlignDriverAction(modelMock, action);
    when(action.isBusy()).thenReturn(Boolean.TRUE);
    assertEquals(instance, instance.work(mockAPI));
    verify(action, times(1)).isBusy();
    verify(action, times(1)).work(mockAPI);
    when(action.isBusy()).thenReturn(Boolean.FALSE);
    instance.work(mockAPI);
    verify(action, times(2)).isBusy();
    verify(action, times(1)).work(mockAPI);
    verify(action).getNextSubAction();
    assertFalse(instance.isBusy());
  }
}
