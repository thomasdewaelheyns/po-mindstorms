package penoplatinum.robot;

/**
 * Robot Interface
 * 
 * Defines all the required methods a robot needs to implement to allow for
 * the framework to work with this robot. This uses only the basic input/output.
 * 
 * @author: Team Platinum
 */

import penoplatinum.model.Model;
import penoplatinum.driver.Driver;


public interface Robot {
  // a robot consists of:
  // - a RobotAPI, offering access to the sensors and actuators
  public Robot useRobotAPI(RobotAPI api);
  public RobotAPI getRobotAPI();

  // in this method, the robot performs one step of its event loop. in this
  // step, it should poll its sensors, update its model and ask the 
  // navigator what to do next, calling the RobotAPI
  public void step();

  // indicates that the robot reached its goal and doesn't do anything anymore
  public boolean reachedGoal();

  // a method to stop the robot (immediately)
  public void stop();

  // gets the name of the robot
  public String getName();
}
