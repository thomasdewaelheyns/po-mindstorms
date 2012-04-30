package penoplatinum.driver.action.subaction;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import penoplatinum.driver.action.AlignDriverAction;
import penoplatinum.model.part.LightModelPart;
import penoplatinum.model.part.SensorModelPart;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import penoplatinum.robot.RobotAPI;
import penoplatinum.util.LightColor;

public class FindLeftWhiteLineTest extends TestCase {
  private AlignDriverAction mockAlign = mock(AlignDriverAction.class);
  private SensorModelPart mockSensor = mock(SensorModelPart.class);
  private LightModelPart mockLight = mock(LightModelPart.class);
  private RobotAPI mockAPI = mock(RobotAPI.class);
  
  @Before
  public void setUp() {
    when(mockAlign.getSensorPart()).thenReturn(mockSensor);
    when(mockAlign.getLightPart()).thenReturn(mockLight);
  }

  /**
   * Test of isBusy method, of class FindLeftWhiteLine.
   */
  @Test
  public void testIsBusy() {
    FindLeftWhiteLine instance = new FindLeftWhiteLine(mockAlign);
    assertEquals(true, instance.isBusy());
    instance.work(mockAPI);
    assertEquals(true, instance.isBusy());
    when(mockLight.getCurrentLightColor()).thenReturn(LightColor.WHITE);
    instance.work(mockAPI);
    assertEquals(false, instance.isBusy());
  }

  /**
   * Test of work method, of class FindLeftWhiteLine.
   */
  @Test
  public void testWork() {
    FindLeftWhiteLine instance = new FindLeftWhiteLine(mockAlign);
    instance.work(mockAPI);
    verify(mockAPI).turn(anyInt());
  }

  /**
   * Test of getNextSubAction method, of class FindLeftWhiteLine.
   */
  @Test
  public void testGetNextSubAction() {
    FindLeftWhiteLine instance = new FindLeftWhiteLine(mockAlign); // Turn and find line
    instance.work(mockAPI);
    when(mockLight.getCurrentLightColor()).thenReturn(LightColor.WHITE);
    when(mockSensor.isTurning()).thenReturn(Boolean.TRUE);
    when(mockSensor.isMoving()).thenReturn(Boolean.TRUE);
    instance.work(mockAPI);
    assertEquals(FindRightWhiteLine.class, instance.getNextSubAction().getClass());
    
    instance = new FindLeftWhiteLine(mockAlign); //Turn and don't find line, stop moving
    instance.work(mockAPI);
    when(mockLight.getCurrentLightColor()).thenReturn(LightColor.BLACK);
    when(mockSensor.isTurning()).thenReturn(Boolean.FALSE);
    when(mockSensor.isMoving()).thenReturn(Boolean.FALSE);
    when(mockLight.getCurrentLightColor()).thenReturn(LightColor.WHITE);
    instance.work(mockAPI);
    assertEquals(ReturnSubAction.class, instance.getNextSubAction().getClass());
  }
}
