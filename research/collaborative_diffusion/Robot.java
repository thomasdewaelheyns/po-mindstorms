// copy from original Robot.java
// modified !!!
public interface Robot {
  /**
   * we allow the RobotAPI to be set externally. this allows for reusing the
   * same robot, both in the real world as in the Simulator
   */
  public Robot useRobotAPI( RobotAPI api );

  /**
   * to allow external Communication, where the robot can send information,
   * a RobotAgent is provided.
   */
  public Robot useCommunicationAgent( RobotAgent agent );

  /**
   * to allow external Communication to be processed by the Robot, a generic
   * String-based command processing. this method will be called by the same
   * RobotAgent used to perform other communication related tasks.
   */
  public void processCommand( String cmd );

  /**
   * the reverse for the command processing consists of access to the internal
   * status of the Model and Navigator.
   */
  public String getModelState();
  public String getNavigatorState();

  /**
   * in this method, the robot performs one step of its event loop. in this
   * step, it should poll its sensors, update its model and ask the 
   * navigator what to do next, calling the RobotAPI
   */ 
  public void step();

  /**
   * indicates that the robot reached its goal and doesn't do anything anymore
   */
  public Boolean reachedGoal();
  
  /**
   * a method to stop the robot (immediately)
   */ 
  public void stop();
}
