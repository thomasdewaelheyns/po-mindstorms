package penoplatinum.bluetooth;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import penoplatinum.agent.MQ;
import penoplatinum.simulator.Robot;
import penoplatinum.simulator.RobotAgent;
import penoplatinum.simulator.Simulator;

/**
 * SimulationRobotAgent
 * 
 * Implements the RobotAgent interface for use in the SimulationEnvironment.
 * It provides additional methods for the Simulator to set up links between
 * them.
 * 
 * @author: Team Platinum
 */
public class RobotBluetoothAgent implements RobotAgent {

  private Robot robot;
  private Simulator simulator;
  private MQ mq;
  private IConnection conn;
  private QueuedPacketTransporter t;

  public RobotBluetoothAgent() {
    /**/
  }

  public RobotBluetoothAgent useConnection(IConnection conn) {
    this.conn = conn;
    this.t = new QueuedPacketTransporter(conn);
    conn.RegisterTransporter(t, 7897987);
    return this;
  }

  @Override
  public void setRobot(Robot robot) {
    this.robot = robot;
  }

  @Override
  public void run() {
    if (conn == null) {
      throw new RuntimeException();
    }

    CallbackPacketTransporter t = new CallbackPacketTransporter(conn, new IPacketHandler() {

      public void receive(int packetID, byte[] dgram) {
        RobotBluetoothAgent.this.receive(new String(dgram));
      }
    });
    
    conn.RegisterTransporter(t, 987874288);



  }

  @Override
  public void receive(String cmd) {
    //Warning: this is called asynchronously!!
    this.robot.processCommand(cmd);
  }

  @Override
  public void send(String msg) {
    try {
      mq.sendMessage(msg);
    } catch (IOException ex) {
      Logger.getLogger(RobotBluetoothAgent.class.getName()).log(Level.SEVERE, null, ex);
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
