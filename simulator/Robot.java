/**
 * Robot Interface
 * 
 * Defines all the required methods a robot needs to implement to allow for
 * the framework to work with this robot.
 * It offers access to all components of the robot, allowing the construction
 * of completely custom robots.
 * 
 * Author: Team Platinum
 */

public interface Robot {

  /**
   * we allow the RobotAPI to be set externally. this allows for reusing the
   * same robot, both in the real world as in the Simulator
   */
  public void useRobotAPI( RobotAPI api );

  /**
   * to allow external Communication to be processed by the Robot, a generic
   * String-based command processing.
   */
  public void processCommand( String cmd );

  /**
   * the reverse for the command processing consists of access to the internal
   * status of the Model and Navigator.
   */
  public String getModelState();
  public String getNavigatorState();

  /**
   * the actual method to start the robot's actions.
   */ 
  public void run();
  
  /**
   * a method to stop the robot (immediately)
   */ 
  public void stop();
}
