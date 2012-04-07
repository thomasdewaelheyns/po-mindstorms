package penoplatinum.driver.action;

/**
 * IdleDriverActionTest
 * 
 * Tests IdleDriverAction
 * 
 * @author: Team Platinum
 */

import junit.framework.*;
import static org.mockito.Mockito.*;

import penoplatinum.robot.RobotAPI;

import penoplatinum.model.Model;

import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SensorModelPart;


public class IdleDriverActionTest extends TestCase {

  public IdleDriverActionTest(String name) { 
    super(name);
  }
 
  // public interface
 
  public void testIsNotBusy() {
    IdleDriverAction action = this.createAction();
    assertFalse(action.isBusy());
  }
  
  public void testCanBeInterrupted() {
    IdleDriverAction action = this.createAction();
    assertTrue(action.canBeInterrupted());
  }

  public void testDoesNotInterrupt() {
    IdleDriverAction action = this.createAction();
    assertFalse(action.interrupts());
  }

  public void testWork() {
    IdleDriverAction action = this.createAction();
    RobotAPI api = this.mockRobotAPI();
    action.work(api);
    verifyZeroInteractions(api);
  }
  
  // private construction helpers
  
  private IdleDriverAction createAction() {
    return new IdleDriverAction();
  }
  
  private RobotAPI mockRobotAPI() {
    return mock(RobotAPI.class);
  }
}
