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

import penoplatinum.driver.Driver;

import penoplatinum.model.Model;

import penoplatinum.navigator.action.NavigatorAction;
import penoplatinum.navigator.mode.NavigatorMode;


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
    this.modes.get(0).activate();
  }

  protected void thenUse(NavigatorMode mode) {
    this.modes.add(mode);
  }

  // by default we don't use a Model, but an actual implementation can 
  // override this method to extract a ModelPart
  public Navigator useModel(Model model) { return this; }
  
  public final MultiModeNavigator finish(Driver driver) {
    // if the driver completed our last instruction successfully, we update
    // our information to reflect our new position.
    if( driver.completedLastInstruction() ) {
      // because this cannot happen without us providing this instruction
      // there must be a plan with a current action when this happens
      this.getCurrentAction().complete();
      // and then we can discard it
      this.discardCurrentAction();      
    } else {
      // if the previous instruction wasn't completed successfully, we also
      // discard our current plan, because at least this step failed.
      // this is also the initialization point on the first instruct() call
      this.discardCurrentPlan();
    }
    return this;
  }

  // first we check if our previous/current action was successfully performed
  // next we give the next instruction in our plan
  public final MultiModeNavigator instruct(Driver driver) {
    //System.out.println("INSTRUCT");
    if( ! this.reachedGoal() ) { 
      this.getCurrentAction().instruct(driver);
    }
    return this;
  }
  
  private NavigatorAction getCurrentAction() {
    //System.out.println("GET CURRENT ACTION" );
    if( this.plan.size() == 0 ) {
      this.createNewPlan();
    }
    return this.plan.get(0);
  }

  private void createNewPlan() {
    //System.out.println("CREATE NEW PLAN");
    // switch to next mode once the current one has reached its goal
    while( this.getCurrentMode().reachedGoal() ) {
      this.discardCurrentMode();
      this.modes.get(0).activate();
    }
    this.plan = this.getCurrentMode().createNewPlan();
    if( this.plan.size() == 0 ) {
      throw new RuntimeException( "Couldn't create a plan ?!" );
    }
  }

  public final boolean reachedGoal() {
    boolean reached = this.getCurrentMode().reachedGoal(),
            nonext  = this.noNextAction();
    //System.out.println( "REACHED : " + reached + " " + nonext );
    return reached && nonext;
    // return this.getCurrentMode().reachedGoal() && this.noNextAction();
  }

  // when we're at the last action of the last mode, there is no next action
  private boolean noNextAction() {
    //System.out.print( this.plan.size() + " / " + this.modes.size() + " " );
    return this.plan.size() < 1 && this.modes.size() < 2;
  }


  private void discardCurrentAction() {
    //System.out.print("DISCARD : " + this.plan.size() + " -> ");
    if( this.plan.size() > 0 ) { this.plan.remove(0); }
    //System.out.println( this.plan.size() );
  }
  
  private void discardCurrentPlan() {
    //System.out.println("DISCARD PLAN" );
    this.plan.clear();
  }
  
  private NavigatorMode getCurrentMode() {
    //System.out.println( "GET CURRENT MODE" );
    if( this.modes.size() < 1 ) {
      throw new RuntimeException( "No current NavigatorMode !" );
    }
    return this.modes.get(0);
  }

  private void discardCurrentMode() {
    //System.out.println("DISCARD CURRENT MODE" );    
    if( this.modes.size() > 1 ) {
      System.out.println("DISCARD CURRENT MODE" );
      this.modes.remove(0);
    }
  }
}
