package penoplatinum.simulator;

/**
 * Robot Interface
 * 
 * Defines all the required methods a robot needs to implement to allow for
 * the framework to work with this robot.
 * It offers access to all components of the robot, allowing the construction
 * of completely custom robots.
 * 
 * @author: Team Platinum
 */
 
import penoplatinum.driver.Driver;

import penoplatinum.simulator.Navigator;

import penoplatinum.gateway.GatewayClient;

import penoplatinum.model.Reporter;


public interface Robot {
  // a robot consists of:
  // - a RobotAPI, offering access to the sensors and actuators
  public Robot useRobotAPI(RobotAPI api);
  public RobotAPI getRobotAPI();
  
  // - a Driver, that knows how to move from one point to the other,
  public Robot useDriver(Driver driver);
  public Driver getDriver();
  
  // - a Navigator, that decides what point to move next to
  public Robot useNavigator(Navigator navigator);
  public Navigator getNavigator();

  // - a GatyewayClient, that provides access to the Gateway to send out msgs
  public Robot useGatewayClient(GatewayClient agent);
  public GatewayClient getGatewayClient();
  
  // - a Reporter, that interrogates the Robot and can send information out
  public Robot useReporter(Reporter reporter);
    
  // incoming commands are processed
  public void processCommand(String cmd);

  // accessor for the internal model of the Robot
  // TODO: Commented out to allow partial compilation
  // public Model getModel();

  // in this method, the robot performs one step of its event loop. in this
  // step, it should poll its sensors, update its model and ask the 
  // navigator what to do next, calling the RobotAPI
  public void step();

  // indicates that the robot reached its goal and doesn't do anything anymore
  public Boolean reachedGoal();

  // a method to stop the robot (immediately)
  public void stop();

  // gets the name of the robot
  public String getName();
}
