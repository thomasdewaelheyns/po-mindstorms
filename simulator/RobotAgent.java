/**
 * RobotAgent interface
 * 
 * Defines the interface for RobotAgents. RobotAgents are threads that are 
 * running alongside the Robot to collect status-information from the Model
 * and Navigator and send this back to the PC. They also receive commands from
 * the PC and pass these to the robot they control.
 * 
 * Author: Team Platinum
 */

public interface RobotAgent {

  // sets the robot this agent controls
  public void setRobot( Robot robot );
  
  // starts the agent's event loop
  public void run();

  /**
   * method that is called when new commands are received, typically by the
   * bluetooth stack, but also by a simulation environment
   */
  public void receive( String cmd );
  
  /**
   * method used to send a collected status back to the PC, implemented by
   * the bluetooth stack, but this can also be a simple simulation environment
   */
  public void send( String status );
}
