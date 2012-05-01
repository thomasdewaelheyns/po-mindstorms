package penoplatinum.driver;

/**
 * ManhattanDriver
 * 
 * Drives in a Manhattan/Taxi-cab way on fixed size sectors and only in 
 * straight corners. A forward Move, moves 1 sector and turns are
 * expressed in straight corner Rotations.
 *
 * It can be extended through a Behaviour system. A Behaviour consists of a
 * check for a certain condition and a method to retrieve next actions to 
 * perform when the condition is met.
 *
 * @author: Team Platinum
 */

import java.util.List;
import java.util.ArrayList;
import penoplatinum.driver.behaviour.DriverBehaviour;
import penoplatinum.driver.action.DriverAction;
import penoplatinum.driver.action.IdleDriverAction;
import penoplatinum.driver.action.MoveDriverAction;
import penoplatinum.driver.action.TurnDriverAction;
import penoplatinum.robot.AdvancedRobot;


public class ManhattanDriver implements Driver {
  // a reference to the AdvancedRobot we're driving
  private AdvancedRobot advancedRobot;

  // the size of one sector, the atomic unit of movement of this Driver
  // in meter
  private double sectorSize;

  // the behaviours that extend our basic movement actions
  private List<DriverBehaviour> behaviours = new ArrayList<DriverBehaviour>();

  // internal basic movement actions
  private IdleDriverAction IDLE;
  private MoveDriverAction MOVE;
  private TurnDriverAction TURN_LEFT;
  private TurnDriverAction TURN_RIGHT;

  // although they're called actions, they in fact implement a strategy 
  // pattern; this is the current action/strategy
  private DriverAction currentAction;

  // internal flag to keep track if a given instruction is interrupted
  private boolean instructionIsInterrupted;

  
  public ManhattanDriver(double sectorSize) {
    this.sectorSize = sectorSize;
    this.setupIdleAction();
    this.goIdle();
  }
  
  private void setupIdleAction() {
    this.IDLE = new IdleDriverAction();
  }
  
  protected ManhattanDriver addBehaviour(DriverBehaviour behaviour) {
    this.behaviours.add(behaviour);
    return this;
  }

  public ManhattanDriver drive(AdvancedRobot advancedRobot) {
    this.advancedRobot = advancedRobot;
    this.setupMovementActions();
    return this;
  }
  
  private void setupMovementActions() {
    this.MOVE = new MoveDriverAction(this.advancedRobot.getModel()).set(this.sectorSize);
    this.TURN_LEFT = new TurnDriverAction(this.advancedRobot.getModel()).set(90);
    this.TURN_RIGHT = new TurnDriverAction(this.advancedRobot.getModel()).set(-90);
  }

  // movement methods are honoured directly and change the current strategy
  public ManhattanDriver move() {
    this.perform(MOVE.clone());
    return this;
  }
  
  public ManhattanDriver turnLeft() {
    this.perform(TURN_LEFT.clone());
    return this;
  }

  public ManhattanDriver turnRight() {
    this.perform(TURN_RIGHT.clone());
    return this;
  }
  
  // changing state/action is done through this method
  // it makes sure that the internal state of the Driver is consistent
  private void perform(DriverAction nextAction) {
    this.instructionIsInterrupted = false;
    this.currentAction = nextAction;
  }

  // changing state/action is done through this method
  // it makes sure that the internal state of the Driver is consistent
  private void interrupt(DriverAction nextAction) {
    this.instructionIsInterrupted = nextAction.interrupts();
    this.currentAction = nextAction;
  }
  
  // changing state/action is done through this method
  // it makes sure that the internal state of the Driver is consistent
  private void goIdle() {
    this.currentAction = IDLE;
  }

  // return the busy state of our current action
  public boolean isBusy() {
    if(this.currentAction == null){
      return false;
    }
    return this.currentAction.isBusy();
  }

  // when we can proceed with our actions, we first check if none of the 
  // behaviours force us to change our currentAction, but only if the current
  // action allows to be interrupted
  public ManhattanDriver proceed() {
    if( this.currentAction.canBeInterrupted() ) {
      this.applyBehaviours();
    }
    // when we're done we go idle...
    if( ! this.currentAction.isBusy() ) {
      this.goIdle();
    } else {
      this.currentAction.work(this.advancedRobot.getRobotAPI());
    }
    return this;
  }

  // loops over the different behaviours, checking if they require action and
  // performing the action they provide.
  private void applyBehaviours() {
    for( DriverBehaviour behaviour : this.behaviours ) {
      if( behaviour.requiresAction(this.advancedRobot.getModel(), 
                                   this.currentAction) )
      {
        this.interrupt(behaviour.getNextAction());
        break; // we don't allow behaviours to override each other
               // the first behaviour that is triggered is honoured
      }
    }
  }
  
  // so, did we finish what we were instructed ?
  // only if we are doing nothing and we weren't interrupted
  public boolean completedLastInstruction() {
    return (! this.isBusy()) && (! this.instructionIsInterrupted);
  }
}
