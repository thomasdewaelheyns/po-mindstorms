package penoplatinum.bluetooth;

import java.io.IOException;
import penoplatinum.Config;
import penoplatinum.util.Utils;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.robot.AdvancedRobot;
import penoplatinum.robot.Robot;

/**
 * RobotBluetoothGatewayClient
 * 
 * Implements the GatewayClient interface for use in the SimulationEnvironment.
 * It provides additional methods for the Simulator to set up links between
 * them.
 * 
 * @author: Team Platinum
 */
public class RobotBluetoothGatewayClient implements GatewayClient {

  private AdvancedRobot robot;
  private IConnection conn;
  private CallbackPacketTransporter t;

  public RobotBluetoothGatewayClient() {
  }

  public RobotBluetoothGatewayClient useConnection(IConnection conn) {
    this.conn = conn;
    return this;
  }

  @Override
  public RobotBluetoothGatewayClient setRobot(AdvancedRobot robot) {
    this.robot = robot;
    return this;
  }

  public void run() {
    if (conn == null) {
      throw new RuntimeException();
    }

    this.t = new CallbackPacketTransporter(conn, new IPacketHandler() {

      public void receive(int packetID, byte[] dgram) {
        RobotBluetoothGatewayClient.this.receive(new String(dgram));
      }
    });

    conn.RegisterTransporter(t, Config.BT_GHOST_PROTOCOL);
    
    conn.RegisterTransporter(t, Config.BT_AGENTS);
    conn.RegisterTransporter(t, Config.BT_MODEL);
    conn.RegisterTransporter(t, Config.BT_VALUES);
    conn.RegisterTransporter(t, Config.BT_WALLS);
  }

  @Override
  public void receive(String cmd) {
    this.robot.processCommand(cmd);
  }

  @Override
  public void send(String msg, int channel) {
    byte[] buf = null;
    buf = getBytes(msg);

    try {
      t.getSendStream().write(buf, 0, buf.length);
      t.SendPacket(channel);
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
