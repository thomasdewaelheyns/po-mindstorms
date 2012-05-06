package penoplatinum.driver.action;

/**
 * TurnDriverActionTest
 * 
 * Tests TurnDriverAction
 * 
 * @author: Team Platinum
 */

import junit.framework.*;
import static org.mockito.Mockito.*;

import penoplatinum.robot.RobotAPI;

import penoplatinum.model.Model;

import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SensorModelPart;


public class TurnDriverActionTest extends TestCase {

  private final static int ANGLE = 67;

  private Model model;
  private SensorModelPart sensors;
  

  public TurnDriverActionTest(String name) { 
    super(name);
  }
 
  // public interface
 
  public void testIsBusyAfterCreation() {
    TurnDriverAction action = this.createAction();
    assertTrue(action.isBusy());
  }
  
  public void testCanBeInterrupted() {
    TurnDriverAction action = this.createAction();
    assertFalse(action.canBeInterrupted());
  }

  public void testInterrupts() {
    TurnDriverAction action = this.createAction();
    assertFalse(action.interrupts());
  }

  public void testWork() {
    TurnDriverAction action = this.createAction();
    RobotAPI api = this.mockRobotAPI();
    when(this.sensors.isTurning()).thenReturn(true, false);
    assertTrue(action.isBusy());
    verify(this.sensors, never()).isTurning(); // action isn't started yet
    action.work(api);
    assertTrue(action.isBusy());
    verify(this.sensors).isTurning();          // started, sensors are polled
    assertFalse(action.isBusy());
    verify(api).turn(ANGLE);
  }
  
  // private construction helpers
  
  private TurnDriverAction createAction() {
    this.sensors = mock(SensorModelPart.class);
    this.model   = mock(Model.class);
    when(this.model.getPart(ModelPartRegistry.SENSOR_MODEL_PART))
      .thenReturn(this.sensors);
    TurnDriverAction action = new TurnDriverAction(this.model);
    action.set(ANGLE);
    return action;
  }
  
  private RobotAPI mockRobotAPI() {
    return mock(RobotAPI.class);
  }
}
