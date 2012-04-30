package penoplatinum.driver.action.subaction;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import penoplatinum.driver.action.AlignDriverAction;
import penoplatinum.model.part.SensorModelPart;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import penoplatinum.robot.RobotAPI;

public class TurnToAlignTest extends TestCase {
  private AlignDriverAction mockAlign = mock(AlignDriverAction.class);
  private SensorModelPart mockSensor = mock(SensorModelPart.class);
  private RobotAPI mockAPI = mock(RobotAPI.class);
  
  @Before
  public void setUp() {
    when(mockAlign.getSensorPart()).thenReturn(mockSensor);
  }

  /**
   * Test of isBusy method, of class TurnToAlign.
   */
  @Test
  public void testIsBusy() {
    TurnToAlign instance = new TurnToAlign(mockAlign, 50);
    assertEquals(true, instance.isBusy());
    instance.work(mockAPI);
    assertEquals(false, instance.isBusy());
    when(mockSensor.isTurning()).thenReturn(true);
    when(mockSensor.isMoving()).thenReturn(true);
    assertEquals(true, instance.isBusy());    
  }

  /**
   * Test of work method, of class TurnToAlign.
   */
  @Test
  public void testWork() {
    TurnToAlign instance = new TurnToAlign(mockAlign, 50);
    instance.work(mockAPI);
    verify(mockAPI).turn(50);
    verifyNoMoreInteractions(mockAPI);
    instance.work(mockAPI);
  }

  /**
   * Test of getNextSubAction method, of class TurnToAlign.
   */
  @Test
  public void testGetNextSubAction() {
    TurnToAlign instance = new TurnToAlign(mockAlign, 50);
    assertEquals(null, instance.getNextSubAction());
  }
}
