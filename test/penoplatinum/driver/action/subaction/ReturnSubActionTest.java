package penoplatinum.driver.action.subaction;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import penoplatinum.driver.action.AlignDriverAction;
import penoplatinum.model.part.SensorModelPart;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import penoplatinum.robot.RobotAPI;

public class ReturnSubActionTest extends TestCase {
  private AlignDriverAction mockAlign = mock(AlignDriverAction.class);
  private SensorModelPart mockSensor = mock(SensorModelPart.class);
  private RobotAPI mockAPI = mock(RobotAPI.class);

  @Before
  public void setUp() {
    when(mockAlign.getSensorPart()).thenReturn(mockSensor);
  }

  /**
   * Test of isBusy method, of class ReturnSubAction.
   */
  @Test
  public void testIsBusy() {
    ReturnSubAction instance = new ReturnSubAction(mockAlign, 50);
    assertEquals(true, instance.isBusy());
    instance.work(mockAPI);
    assertEquals(false, instance.isBusy());
    when(mockSensor.isTurning()).thenReturn(Boolean.TRUE);
    when(mockSensor.isMoving()).thenReturn(Boolean.TRUE);
    assertEquals(true, instance.isBusy());
  }

  /**
   * Test of getNextSubAction method, of class ReturnSubAction.
   */
  @Test
  public void testGetNextSubAction() {
    ReturnSubAction instance = new ReturnSubAction(mockAlign, 20);
    assertEquals(null, instance.getNextSubAction());
  }

  /**
   * Test of work method, of class ReturnSubAction.
   */
  @Test
  public void testWork() {
    when(mockSensor.getTotalTurnedAngle()).thenReturn(33.0);
    ReturnSubAction instance = new ReturnSubAction(mockAlign, 20);
    instance.work(mockAPI);
    verify(mockAPI, times(1)).turn(-13);
    verifyNoMoreInteractions(mockAPI);
    instance.work(mockAPI);
    instance.work(mockAPI);
    instance.work(mockAPI);
  }
}
