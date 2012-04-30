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

public class FindRightWhiteLineTest extends TestCase {
  private RobotAPI mockAPI = mock(RobotAPI.class);
  private AlignDriverAction mockAlign = mock(AlignDriverAction.class);
  private SensorModelPart mockSensor = mock(SensorModelPart.class);
  private LightModelPart mockLight = mock(LightModelPart.class);

  @Before
  public void setUp() {
    when(mockAlign.getSensorPart()).thenReturn(mockSensor);
    when(mockAlign.getLightPart()).thenReturn(mockLight);
  }

  /**
   * Test of isBusy method, of class FindRightWhiteLine.
   */
  @Test
  public void testIsBusy() {
    FindRightWhiteLine instance = new FindRightWhiteLine(mockAlign, null);
    assertEquals(true, instance.isBusy());
    instance.work(mockAPI);
    assertEquals(true, instance.isBusy());
    instance.work(mockAPI);
    assertEquals(false, instance.isBusy());
    
    instance = new FindRightWhiteLine(mockAlign, null);
    assertEquals(true, instance.isBusy());
    instance.work(mockAPI);
    assertEquals(true, instance.isBusy());
    when(mockSensor.getTotalTurnedAngle()).thenReturn(-20.0);
    when(mockLight.getCurrentLightColor()).thenReturn(LightColor.WHITE);
    instance.work(mockAPI);
    assertEquals(false, instance.isBusy());
  }

  /**
   * Test of work method, of class FindRightWhiteLine.
   */
  @Test
  public void testWork() {
    FindRightWhiteLine instance = new FindRightWhiteLine(mockAlign, null);
    instance.work(mockAPI);
    verify(mockAPI).turn(340);
    verifyNoMoreInteractions(mockAPI);
    instance.work(mockAPI);
  }

  /**
   * Test of getNextSubAction method, of class FindRightWhiteLine.
   */
  @Test
  public void testGetNextSubAction() {
    FindRightWhiteLine instance = new FindRightWhiteLine(mockAlign, null);
    instance.work(mockAPI);
    instance.work(mockAPI);
    assertEquals(null, instance.getNextSubAction());
    
    instance = new FindRightWhiteLine(mockAlign, null);
    instance.work(mockAPI);
    when(mockSensor.getTotalTurnedAngle()).thenReturn(-20.0);
    when(mockLight.getCurrentLightColor()).thenReturn(LightColor.WHITE);
    instance.work(mockAPI);
    assertEquals(TurnToAlign.class, instance.getNextSubAction().getClass());
  }
}
