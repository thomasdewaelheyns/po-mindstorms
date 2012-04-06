package penoplatinum.driver.action;

/**
 * CombinedDriverActionTest
 * 
 * Tests CombinedDriverAction
 * 
 * @author: Team Platinum
 */

import junit.framework.*;
import static org.mockito.Mockito.*;

import penoplatinum.robot.RobotAPI;


public class CombinedDriverActionTest extends TestCase {

  public CombinedDriverActionTest(String name) { 
    super(name);
  }
 
  // public interface
 
  public void testDoubleFirstPerform() {
    CombinedDriverAction combined = this.createCombinedDriverAction();

    DriverAction mockedAction1 = this.mockDriverAction();
    DriverAction mockedAction2 = this.mockDriverAction();

    combined.firstPerform(mockedAction1);
    try {
      combined.firstPerform(mockedAction2);
      fail( "Two firstPerfom() calls should throw exception." );
    } catch( Exception e ) {}
  }
  
  public void testNormalProcedure() {
    CombinedDriverAction combined = this.createCombinedDriverAction();

    DriverAction mockedAction1 = this.mockDriverAction();
    when(mockedAction1.isBusy()).thenReturn( true, true, false );

    DriverAction mockedAction2 = this.mockDriverAction();
    when(mockedAction2.isBusy()).thenReturn( true, true, true, true, false );

    combined.firstPerform(mockedAction1)
            .thenPerform(mockedAction2);

    RobotAPI api = this.mockRobotAPI();

    // each action gets two steps

    // step 1 (1 of 2 for action1)
    combined.work(api);
    assertTrue(combined.isBusy());
    // action1 should have been checked for business and asked to work
    // only once by the work() call, the isBusy() call is short-cutted due
    // to multiple available actions
    verify(mockedAction1).isBusy();
    verify(mockedAction1).work(api);
    // action2 should be untouched
    verifyZeroInteractions(mockedAction2);

    // step 2 (2 of 2 for action1)
    combined.work(api);
    assertTrue(combined.isBusy());
    // action1 should have been checked for business and asked to work
    verify(mockedAction1, times(2)).isBusy();
    verify(mockedAction1, times(2)).work(api);
    // action2 should be untouched
    verifyZeroInteractions(mockedAction2);

    // step 3 (1 of 2 for action2)
    combined.work(api);
    assertTrue(combined.isBusy());
    // action1 should have been checked for business, which failed, so we 
    // should have moved on to the next action
    verify(mockedAction1, times(3)).isBusy();
    // action2 should be asked to work, but not checked for business, because
    // a newly activated subaction still has to start and therefore IS busy
    // our check on isBusy() is going through to the second action, which is 
    // now the last action
    verify(mockedAction2).isBusy();
    verify(mockedAction2, times(1)).work(api);

    // step 3 (2 of 2 for action2)
    combined.work(api);
    assertTrue(combined.isBusy());
    // action2 should be checked for business and asked to work some more
    // +1 time for our isBusy assertion
    verify(mockedAction2, times(3)).isBusy();
    verify(mockedAction2, times(2)).work(api);

    // step 4 (action2 is over...)
    combined.work(api);
    assertFalse(combined.isBusy());
    // action2 should be checked for business and asked to work some more
    // +1 time for our isBusy assertion
    verify(mockedAction2, times(5)).isBusy();
    verify(mockedAction2, times(3)).work(api);

    // step 5,6,7,8,9,10 (action2 is over...)
    combined.work(api);  combined.work(api); combined.work(api);
    combined.work(api);  combined.work(api); combined.work(api);
    assertFalse(combined.isBusy());
    verify(mockedAction2, times(12)).isBusy();
    verify(mockedAction2, times(9)).work(api);
  }

  public void testCanBeInterrupted() {
    CombinedDriverAction combined = this.createCombinedDriverAction();

    DriverAction mockedAction1 = this.mockDriverAction();
    when(mockedAction1.isBusy()).thenReturn( true, false );
    when(mockedAction1.canBeInterrupted()).thenReturn(false);

    DriverAction mockedAction2 = this.mockDriverAction();
    when(mockedAction2.isBusy()).thenReturn( true, false );
    when(mockedAction2.canBeInterrupted()).thenReturn(true);

    combined.firstPerform(mockedAction1)
            .thenPerform(mockedAction2);

    RobotAPI api = this.mockRobotAPI();

    combined.work(api);
    assertFalse("first action can't be interrupted", combined.canBeInterrupted());
    combined.work(api);
    assertTrue("second action can be interrupted", combined.canBeInterrupted());
  }

  public void testInterrupts() {
    CombinedDriverAction combined = this.createCombinedDriverAction();
    combined.makeInterrupting();
    assertTrue(combined.interrupts());
  }
  
  // private construction helpers

  private CombinedDriverAction createCombinedDriverAction() {
    return new CombinedDriverAction();
  }
  
  private DriverAction mockDriverAction() {
    DriverAction mockedDriverAction = mock(DriverAction.class);
    return mockedDriverAction;
  }
  
  private RobotAPI mockRobotAPI() {
    RobotAPI mockedRobotAPI = mock(RobotAPI.class);
    return mockedRobotAPI;
  }
  
}
