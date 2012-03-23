package penoplatinum.simulator;

/**
 * SimulatedGatewayClient
 * 
 * Implements the GatewayClient interface for use in the Simulator.
 * On the real Robot, this implementation contains logic to send everything
 * over the bluetooth connection to the actual Gateway. Here we short-cut this
 * step and directly implement the logic of the Gateway.
 * 
 * @author: Team Platinum
 */

import org.apache.log4j.Logger;

import penoplatinum.Config;
import penoplatinum.gateway.MQ;


public class SimulatedGatewayClient implements RobotAgent {

  private Logger logger;

  private Robot robot;
  private Simulator simulator;
  private MQ mq;
  private boolean connected = false;

  public void setRobot(Robot robot) {
    this.robot = robot;
    this.logger = Logger.getLogger( "SimulatedGatewayClient/"
                                  + this.robot.getName() );
  }

  public void run() {
    this.logger.debug( "Starting..." );

    mq = new MQ() {
      protected void handleIncomingMessage(String message) {
        receive(message);
      }
    };
    try {
      mq.connectToMQServer(Config.MQ_SERVER)
        .follow(Config.GHOST_CHANNEL);
      this.connected = true;
    } catch(Exception ex) {
      this.logger.error( "Failed to connect and/or follow MQ channel.\n" + ex );
      this.connected = false;
    }
  }

  public void receive(String cmd) {
    this.robot.processCommand(cmd);
  }

  public void send(String msg) {
    if( ! this.connected ) { return; }
    try {
      mq.sendMessage(msg);
    } catch (Exception ex) {
      this.logger.error("Failed to send msg to MQ : " + msg + "\n" + ex);      
    }
  }
}
