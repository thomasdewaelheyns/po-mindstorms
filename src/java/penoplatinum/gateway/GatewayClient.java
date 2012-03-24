package penoplatinum.gateway;

/**
 * GatewayClient interface
 * 
 * Defines a client-interface for communication with the Gateway (on the PC).
 * 
 * @author: Team Platinum
 */

import penoplatinum.simulator.Robot;


public interface GatewayClient {

  // sets the robot this client works for
  public GatewayClient setRobot( Robot robot );

  // starts the agent's event loop
  public void run();

  /**
   * Method executed when new data is received from the Gateway. We only receive
   * one stream of information.
   */
  public void receive( String data );
  
  /**
   * used to send data to the Gateway, indicating the type/transporter/channel
   * it should be passed through.
   */
  public void send( String data, int channel );
}
