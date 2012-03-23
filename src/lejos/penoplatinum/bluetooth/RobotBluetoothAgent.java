package penoplatinum.bluetooth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import penoplatinum.util.Utils;
import penoplatinum..gateway.GatewayConfig;
import penoplatinum.simulator.Robot;
import penoplatinum.simulator.RobotAgent;

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
  private IConnection conn;
  private CallbackPacketTransporter t;

  public RobotBluetoothAgent() {
  }

  public RobotBluetoothAgent useConnection(IConnection conn) {
    this.conn = conn;
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

    this.t = new CallbackPacketTransporter(conn, new IPacketHandler() {

      public void receive(int packetID, byte[] dgram) {
        RobotBluetoothAgent.this.receive(new String(dgram));
      }
    });

    conn.RegisterTransporter(t, AgentConfig.MQRelayPacket);



  }

  @Override
  public void receive(String cmd) {
    this.robot.processCommand(cmd);
  }

  @Override
  public void send(String msg) {
    byte[] buf = null;
    buf = getBytes(msg);

    try {
      t.getSendStream().write(buf, 0, buf.length);
      t.SendPacket(AgentConfig.MQRelayPacket);
    } catch (IOException ex) {
      Utils.Log("Send error!");
    }


  }

  public byte[] getBytes(String s) {
    char[] characters;
    characters = s.toCharArray();
    byte[] b = new byte[characters.length];
    for (int i = 0; i < characters.length; i++) {
      b[i] = (byte) characters[i];
    }
    return b;
  }
}
