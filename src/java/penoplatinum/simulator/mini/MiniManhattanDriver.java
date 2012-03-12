package penoplatinum.simulator.mini;

import java.util.List;
import java.util.ArrayList;

import penoplatinum.pacman.GhostAction;

import penoplatinum.simulator.Model;
import penoplatinum.simulator.RobotAPI;

import penoplatinum.driver.Driver;


public class MiniManhattanDriver implements Driver {
  private MiniGhostModel model;   // TODO: change GhostModel to minimal interface
  private RobotAPI   api;         //       needed by a ManhattanDriver

  private int todo   = GhostAction.NONE;
  private int action = GhostAction.NONE;
  
  public Driver setModel(Model model) {
    this.model = (MiniGhostModel)model;
    return this;
  }

  public Driver useRobotAPI(RobotAPI api) {
    this.api = api;
    return this;
  }
  
  // simple implementation, performing one action per step
  public boolean isBusy() {
    // a dangling feedback-update
    if( this.action != GhostAction.NONE ) {
      this.postProcessPreviousStep();
    }
    return this.todo != GhostAction.NONE;
  }
  
  private void log(String msg) {
    System.out.printf( "[%10s] %2d,%2d / Driver : %s\n", 
                       this.model.getAgent().getName(),
                       this.model.getAgent().getLeft(),
                       this.model.getAgent().getTop(),
                       msg );
  }

  // simple implementation, performing one action per step
  public void step() {
    this.postProcessPreviousStep();
    // at this point, the real-world and the Model are in sync
    this.model.getGrid().refresh();
    this.performNextStep();
  }

  // update the Model after successfully completing a requested action
  private void postProcessPreviousStep() {
    switch(this.action) {
      case GhostAction.FORWARD:
        this.log( "moving model forward..." );
        this.model.moveForward();
        break;
      case GhostAction.TURN_LEFT:
        this.log( "turning model left..." );
        this.model.turnLeft();
        break;
      case GhostAction.TURN_RIGHT:
        this.log( "turning model right..." );
        this.model.turnRight();
        break;
      default:
        this.model.clearLastMovement();
    }
    this.action = GhostAction.NONE;
  }
  
  private void performNextStep() {
    if( this.todo == GhostAction.NONE ) { return; }

    // get the next action from the list and execute it through the RobotAPI
    this.action = this.todo;
    this.log("performing next step : " + this.action );
    switch(this.action) {
      case GhostAction.FORWARD:
        this.log( "going forward..." );
        if( ! this.api.move( 0.2 ) ) {
          // THIS SHOULDN'T HAPPEN, it happens when we want to move forward 
          // and an agent has moved into our targetted spot while we were
          // turning --> solution: don't do combined actions, but navigate
          // only one step at a time, including turning
          // if we can't perform this action, so don't let it be processed
          // on the Model and therefore reset it here
          this.action = GhostAction.RESET;
        }
        break;
      case GhostAction.TURN_LEFT:
        this.log( "turning left..." );
        this.api.turn(-90);
        break;
      case GhostAction.TURN_RIGHT:
        this.log( "turn right..." );
        this.api.turn(90);
        break;
    }
    this.todo = GhostAction.NONE;
  }
  
  public void perform(int action) {
    this.todo = action;
    this.step();
  }
}
