package penoplatinum.simulator;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import penoplatinum.agent.MQ;

/**
 * SimulationRobotAgent
 * 
 * Implements the RobotAgent interface for use in the SimulationEnvironment.
 * It provides additional methods for the Simulator to set up links between
 * them.
 * 
 * @author: Team Platinum
 */
public class SimulationRobotAgent implements RobotAgent {

  private Robot robot;
  private Simulator simulator;
  private MQ mq;
  private String robotName;

  
  
  public SimulationRobotAgent(String robotName) {

    this.robotName = robotName;
    mq = new MQ() {

      @Override
      protected void handleIncomingMessage(String sender, String message) {
        receive(message);
      }
    };
    try {
      mq.setMyName(robotName).connectToMQServer().follow("ghost-protocol");
      // TODO: remove hard coded data
    } catch (IOException ex) {
      Logger.getLogger(SimulationRobotAgent.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InterruptedException ex) {
      Logger.getLogger(SimulationRobotAgent.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void setRobot(Robot robot) {
    this.robot = robot;
  }

  public void run() {
    // in the Simulator, we don't use threading
    System.out.println("Agent:> Starting logging...");

  }

  public void receive(String cmd) {
    //Warning: this is called asynchronously!!
    this.robot.processCommand(cmd);
  }

  public void send(String msg) {
    try {
      mq.sendMessage(msg);
    } catch (IOException ex) {
      Logger.getLogger(SimulationRobotAgent.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    //this.simulator.receive(msg); //TODO: unused
  }

  // this method is not part of the RobotAgent interface, but is used by
  // the simulator to replace the outgoing bluetooth communication stack
  public void setSimulator(Simulator simulator) {
    this.simulator = simulator;
    this.send("SimulationAgent Ready");
  }
}
