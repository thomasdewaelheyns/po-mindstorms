package penoplatinum.driver.action;

/**
 * CombinedDriverAction
 *
 * Combines multiple actions into one, managing their execution as if it 
 * were one action for the Driver.
 * 
 * @author: Team Platinum
 */

import java.util.List;
import java.util.ArrayList;


import penoplatinum.robot.RobotAPI;

import penoplatinum.model.Model;


public class CombinedDriverAction implements DriverAction {
  
  private List<DriverAction> actions = new ArrayList<DriverAction>();

  private boolean interrupts = false;

  public CombinedDriverAction firstPerform(DriverAction action) {
    if(actions.size() > 0) {
      throw new RuntimeException( "Already have at least one action. Can't " +
                                  "perform another one first." );
    }
    return this.thenPerform(action);
  }
  
  public CombinedDriverAction thenPerform(DriverAction action) {
    this.actions.add(action);
    return this;
  }
  
  public CombinedDriverAction makeInterrupting() {
    this.interrupts = true;
    return this;
  }

  public boolean isBusy() {
    // as long as we have more than the last action, we're busy
    // the last action determines when we're no longer busy
    return this.actions.size() > 1 ? true : this.getCurrentAction().isBusy();
  }
  
  public boolean canBeInterrupted() {
    return this.getCurrentAction().canBeInterrupted();
  }

  public boolean interrupts() {
    return this.interrupts;
  }

  // we pass on the work() call to each combined action in sequence, moving
  // to the next when the current is no longer busy.
  public CombinedDriverAction work(RobotAPI api) {
    if( ! this.getCurrentAction().isBusy() ) { this.getNextAction(); }
    this.getCurrentAction().work(api);
    return this;
  }

  private DriverAction getCurrentAction() {
    return this.actions.get(0);
  }

  // we pop each action of the list, until there is only one left
  private void getNextAction() {
    if( this.actions.size() > 1 ) { this.actions.remove(0); }
  }
}
