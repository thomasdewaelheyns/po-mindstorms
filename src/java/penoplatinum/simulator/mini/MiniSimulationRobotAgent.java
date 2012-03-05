package penoplatinum.simulator.mini;

import penoplatinum.simulator.Robot;
import penoplatinum.simulator.RobotAgent;

public class MiniSimulationRobotAgent implements RobotAgent, MessageHandler {
  private Queue queue;
  private Robot robot;
  
  // sets the robot this agent controls
  public void setRobot( Robot robot ) {
    this.robot = robot;
  }
  
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
}
