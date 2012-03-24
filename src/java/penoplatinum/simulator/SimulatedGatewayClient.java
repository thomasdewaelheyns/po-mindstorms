package penoplatinum.simulator;

/**
 * SimulatedGatewayClient
 * 
 * Implements the GatewayClient interface for use in the Simulator.
 * On the real Robot, this implementation contains logic to send everything
 * over the bluetooth connection to the actual Gateway. Here we short-cut this
 * step and directly integrate with the Gateway, using a simulated connection.
 * 
 * @author: Team Platinum
 */

import org.apache.log4j.Logger;

import penoplatinum.Config;

import penoplatinum.gateway.MQ;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.gateway.Connection;
import penoplatinum.gateway.Queue;
import penoplatinum.gateway.Gateway;
import penoplatinum.gateway.MessageReceiver;


public class SimulatedGatewayClient implements GatewayClient, MessageReceiver {

  // our own logger
  private Logger logger;

  // the robot we're the GatewayClient for
  private Robot robot;

  // we have a (simulated)connection and a (Simulated)MQ
  private Connection connection;
  private Queue queue;

  // we instantiate a local copy of the Gateway
  private Gateway gateway;

  private boolean connected = false;
  

  public SimulatedGatewayClient setRobot(Robot robot) {
    this.robot = robot;
    this.logger = Logger.getLogger( "SimulatedGatewayClient/"
                                  + this.robot.getName() );
    // now we can really construct our GatewayClient
    this.setupConnection();
    this.setupQueue();
    this.setupGateway();
    
    return this;
  }
  
  private void setupConnection() {
    this.connection = new SimulatedConnection(this);
  }
  
  private void setupQueue() {
    if( Config.USE_LOCAL_MQ ) {
      this.setupLocalMQ();
    } else {
      this.setupRemoteMQ();
    }
  }

  private void setupLocalMQ() {
    this.queue = SimulatedMQ.getInstance().subscribe(this);
  }
  
  private void setupRemoteMQ() {
    try {
      this.queue = new MQ().connectToMQServer(Config.MQ_SERVER)
                           .follow(Config.GHOST_CHANNEL);
    } catch(Exception ex) {
      this.logger.error( "Failed to connect and/or follow MQ channel." + ex );
      this.connected = false;
    }
    this.connected = true;
  }
  
  private void setupGateway() {
    this.gateway = new Gateway().useConnection(this.connection)
                                .useQueue(this.queue);
  }

  public void run() {
     // we don't start a thread or a loop ... everything runs synchronously ?!
  }

  public void receive(String cmd) {
    this.robot.processCommand(cmd);
  }

  public void send(String msg, int channel) {
    if( ! this.connected ) { return; } // TODO: log something
    this.connection.send(msg, channel);
    // this message is now stored on the connection, time to activate the
    // gateway and have it processed
    this.gateway.start(); // this will return because the SimulatedConnection
                          // doesn't block when there are no messages
  }
}
