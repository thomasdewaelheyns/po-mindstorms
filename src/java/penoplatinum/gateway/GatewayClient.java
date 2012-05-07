package penoplatinum.gateway;

/**
 * GatewayClient interface
 * 
 * Defines a client-interface for communication with the Gateway (on the PC).
 * 
 * @author: Team Platinum
 */

import penoplatinum.robot.Robot;


// the GatewayClient is used to send out messages to the Gateway. it also 
// requires the implementation of a receive method to handle incoming messages
public interface GatewayClient extends MessageReceiver {
  // sets the robot this client works for, this is used to dispatch incoming
  // commands
  public GatewayClient setRobot(Robot robot);

  // executed when new data is received from the Gateway. We only receive
  // one stream of information.
  // required by MessageReceiver
  public void receive(String data);
  
  // used to send data to the Gateway, indicating the type/transporter/channel
  // it should be passed through.
  public void send(String data, int channel);
}
