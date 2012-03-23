package penoplatinum.simulator;

/**
 * GatewayClient interface
 * 
 * Defines a client-interface for communication with the Gateway on the PC.
 * 
 * @author: Team Platinum
 */

public interface RobotAgent {

  // sets the robot this client works for
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
