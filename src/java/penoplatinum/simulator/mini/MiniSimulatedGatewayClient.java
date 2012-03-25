package penoplatinum.simulator.mini;

import penoplatinum.gateway.GatewayClient;
import penoplatinum.simulator.Robot;

public class MiniSimulatedGatewayClient implements GatewayClient, MessageHandler {
  private Queue queue;
  private Robot robot;
  
  // starts the agent's event loop
  public void run() {
    // not needed in simulation mode
  }

  // MessageHandler method set a queue to deliver MessagesTo
  public void useQueue(Queue queue) {
    this.queue = queue;
  }

  // MessageHandler method used receive messages
  public void receive(String msg) {
    this.robot.processCommand(msg);
  }
  
  // method to send information out
  public void send(String msg) {
    this.queue.send(msg);
  }

  @Override
  public GatewayClient setRobot(Robot robot) {
    return this;
  }

  @Override
  public void send(String data, int channel) {
  }

}
