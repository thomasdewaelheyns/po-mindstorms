package penoplatinum.driver.action;

/**
 * MoveDriverActionTest
 * 
 * Tests MoveDriverAction
 * 
 * @author: Team Platinum
 */

import junit.framework.*;
import static org.mockito.Mockito.*;

import penoplatinum.robot.RobotAPI;

import penoplatinum.model.Model;

import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SensorModelPart;


public class MoveDriverActionTest extends TestCase {

  private final static double DISTANCE = 0.04;

  private Model model;
  private SensorModelPart sensors;
  

  public MoveDriverActionTest(String name) { 
    super(name);
  }
 
  // public interface
 
  public void testIsBusyAfterCreation() {
    MoveDriverAction action = this.createAction();
    assertTrue(action.isBusy());
  }
  
  public void testCanBeInterrupted() {
    MoveDriverAction action = this.createAction();
    assertTrue(action.canBeInterrupted());
  }

  public void testInterrupts() {
    MoveDriverAction action = this.createAction();
    assertFalse(action.interrupts());
  }

  public void testWork() {
    MoveDriverAction action = this.createAction();
    RobotAPI api = this.mockRobotAPI();
    when(this.sensors.isMoving()).thenReturn(true, false);
    assertTrue(action.isBusy());
    verify(this.sensors, never()).isMoving(); // action isn't started yet
    action.work(api);
    assertTrue(action.isBusy());
    verify(this.sensors).isMoving();          // started, sensors are polled
    assertFalse(action.isBusy());
    verify(api).move(DISTANCE);
  }
  
  // private construction helpers
  
  private MoveDriverAction createAction() {
    this.sensors = mock(SensorModelPart.class);
    this.model   = mock(Model.class);
    when(this.model.getPart(ModelPartRegistry.SENSOR_MODEL_PART))
      .thenReturn(this.sensors);
    MoveDriverAction action = new MoveDriverAction(this.model);
    action.set(DISTANCE);
    return action;
  }
  
  private RobotAPI mockRobotAPI() {
    return mock(RobotAPI.class);
  }
}
