package penoplatinum.simulator.mini;

/**
 * GhostRobot
 * 
 * Implementation of a Robot, implementing a complete Ghost
 * 
 * @author: Team Platinum
 */

import java.util.Arrays;

import penoplatinum.modelprocessor.ModelProcessor;

import penoplatinum.simulator.Model;
import penoplatinum.simulator.Robot;
import penoplatinum.simulator.RobotAgent;
import penoplatinum.simulator.RobotAPI;

import penoplatinum.simulator.Navigator;

import penoplatinum.driver.Driver;

import penoplatinum.grid.GridView;

public class MiniGhostRobot implements Robot {
  private Model      model;
  private RobotAPI   api;   // provided from the outside
  private Driver     driver;
  private Navigator  navigator;
  private RobotAgent communicationAgent;
  private String     name;
  
  private boolean waitingForSweep = false;
  
  public MiniGhostRobot(String name) {
    this.setupModel(name);
  }

  public MiniGhostRobot(String name, GridView view) {
    this.setupModel(name);
    this.model.displayGridOn(view);
  }
  
  public String getName() {
    return this.name;
  }
  
  private void setupModel(String name) {
    this.name = name;
    this.model = new MiniGhostModel(name);
    ModelProcessor processors =
      new MiniInboxProcessor        (
      new MiniWallDetectorProcessor (
      new MiniGridUpdateProcessor   (
    )));
    this.model.setProcessor(processors);
  }

  public Model getModel() {
    return this.model;
  }
  
  public String getModelState() {
    // TODO
    return "";
  }
  
  public String getNavigatorState() {
    // TODO
    return "";
  }

  public Robot useRobotAPI( RobotAPI api ) {
    this.api       = api;
    this.driver    = new MiniManhattanDriver()
                      .setModel(this.model)
                      .useRobotAPI(this.api);
    this.navigator = new MiniGhostNavigator().setModel(this.model);
    return this;
  }

  public Robot useCommunicationAgent( RobotAgent agent ) {
    this.communicationAgent = agent;
    return this;
  }

  // incoming communication from other ghosts, used by RobotAgent to deliver
  // incoming messages from the other ghosts
  public void processCommand(String cmd) {
    this.model.addIncomingMessage(cmd);
  }

  private void log(String msg) {
    System.out.printf( "[%10s] %2d,%2d / Robot  : %s\n", 
                       this.model.getAgent().getName(),
                       this.model.getAgent().getLeft(),
                       this.model.getAgent().getTop(),
                       msg );
  }
  
  // one step in the event-loop of the Robot
  public void step() {
    // poll other sensors and update model
    this.model.updateSensorValues(this.api.getSensorValues());

    // let the driver do his thing
    if( this.driver.isBusy() ) {
      this.driver.step();
      return;
    }
    
    // we want obstacle-information based on Sonar-values
    // as long as this is in progress, we wait
    if( this.api.sweepInProgress() ) { 
      this.log( "waiting for sweep in progress" );
      return;
    }
    
    // if the sweep is ready ...
    if( this.waitingForSweep ) {
      this.model.updateSonarValues( this.api.getSweepResult(),
                                    Arrays.asList(-90, 0, 90) );
      this.waitingForSweep = false;
    } else {
      this.api.sweep( new int[] { -90, 0, 90 } );
      this.waitingForSweep = true;
      return; // to wait for results
    }
    
    // ask navigator what to do and ...
    // let de driver drive, manhattan style ;-)
    this.driver.perform(this.navigator.nextAction());
    
    // send outgoing messages
    this.sendMessages();
  }
  
  private void sendMessages() {
    if( this.communicationAgent == null ) { return; }
    for( String msg : this.model.getOutgoingMessages() ) {
      this.communicationAgent.send(msg);
    }
    this.model.clearOutbox();
  }
  
  public Boolean reachedGoal() {
    return this.navigator.reachedGoal();
  }
  
  public void stop() {
    this.api.stop();
  }
}
