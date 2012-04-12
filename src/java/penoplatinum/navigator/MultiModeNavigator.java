package penoplatinum.navigator;

/**
 * MultiModeNavigator
 * 
 * Implements the Navigator interface, adding the possibility to implement
 * multiple modi operandi.
 * To complete the implementation, the actual goal needs to be implemented, 
 * along with the logic to reach it using a plan. This is done by implementing
 * the NavigatorMode interface and adding the mode to this Navigator.
 * Each time a Mode has reached its goal, the next Mode is activated.
 * 
 * @author: Team Platinum
 */

import java.util.List;
import java.util.ArrayList;

import penoplatinum.model.Model;

import penoplatinum.navigator.action.NavigatorAction;


public class MultiModeNavigator implements Navigator {
  // this navigator supports multiple modi operandi. the list is activated
  // in sequence
  private List<NavigatorMode> modes = new ArrayList<NavigatorMode>();

  // a plan consists of a series of NavigatorActions. this list is created
  // by the createNewPlan method on the NavigatorMode
  private List<NavigatorAction> plan = new ArrayList<NavigatorAction>();


  // overriding classes can add Modes to define their logic
  protected void firstUse(NavigatorMode mode) {
    if(modes.size() > 0) {
      throw new RuntimeException( "Already have at least one mode. Can't " +
                                  "perform another one first." );
    }
    this.thenUse(mode);
  }

  protected void thenUse(NavigatorMode mode) {
    this.modes.add(mode);
  }

  // by default we don't use a Model, but an actual implementation can 
  // override this method to extract a ModelPart
  public Navigator useModel(Model model) { return this; }

  // first we check if our previous/current action was successfully performed
  // next we give the next instruction in our plan
  public final Navigator instruct(Driver driver) {
    this.processFeedback(driver);
    this.provideNextInstruction(driver);
  }

  public final boolean reachedGoal() {
    return this.getCurrentMode().reachedGoal();
  }

  private void processFeedback(Driver driver) {
    // if the driver completed our last instruction successfully, we update
    // our information to reflect our new position.
    if( driver.completedLastInstruction() ) {
      this.getCurrentAction().complete();
    } else {
      // if the previous instruction wasn't completed successfully, we also
      // discard our current plan, because at least this step failed.
      this.discardCurrentPlan();
    }
  }
  
  private void discardCurrentPlan() {
    this.plan.clear();
  }
  
  private void provideNextInstruction(Driver driver) {
    if( this.reachedGoal() ) { return; } // we're done
    NavigatorAction action = this.getNextAction();
    if( action == null ) { this.createNewPlan(); }
    action = this.getNextAction();
    if( action == null ) {
      throw new RuntimeException( "Cloudn't create a plan ?!" );
    }
    action.instruct(driver);
  }
  
  private NavigatorAction getNextAction() {
    this.discardCurrentAction();
    return this.getCurrentAction();
  }
  
  private void discardCurrentAction() {
    if( this.plan.size < 1 ) { return; }
    this.plan.remove(0);
  }
  
  private NavigatorAction getCurrentAction() {
    if( this.plan.size < 1 ) { return null; }
    return this.plan.get(0);
  }

  private void createNewPlan() {
    if( this.getCurrentMode().reachedGoal() ) {
      this.discardCurrentMode();
    }
    this.plan = this.getCurrentMode().createNewPlan();
  }
  
  private NavigatorMode getCurrentMode() {
    if( this.modes.size() < 1 ) {
      throw new RuntimeException( "No current NavigatorMode!" );
    }
    return this.modes.getAt(0);
  }

  private void discardCurrentMode() {
    if( this.modes.size() > 1 ) {
      this.modes.remove(0);
    }
  }
}
