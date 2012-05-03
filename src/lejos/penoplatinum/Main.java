package penoplatinum;

import penoplatinum.util.Utils;
import java.io.PrintStream;
import penoplatinum.bluetooth.QueuedPacketTransporter;
import penoplatinum.bluetooth.RobotBluetoothGatewayClient;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.driver.GhostDriver;
import penoplatinum.fulltests.LineRobot;
import penoplatinum.navigator.GhostNavigator;

public class Main {

  public static void main(String[] args) throws Exception {
    LineRobot robot = new LineRobot();
    //robot.useNavigator(new LeftFollowingGhostNavigator(robot.getGhostModel()));
    robot.useNavigator(new GhostNavigator());
    robot.useDriver(new GhostDriver());

    final AngieEventLoop angie = new AngieEventLoop(robot);
    RobotBluetoothConnection conn = new RobotBluetoothConnection();
    conn.initializeConnection();
    Utils.EnableRemoteLogging(conn);

    final RobotBluetoothGatewayClient robotBluetoothAgent = new RobotBluetoothGatewayClient();
    robot.useGatewayClient(robotBluetoothAgent.useConnection(conn));
    Runnable runnable = new Runnable() {

      public void run() {
        Utils.Log("Started!");
        angie.runEventLoop();
      }
    };

    runnable.run();
  }

  private static void initializeAgent(final AngieEventLoop angie) {
    RobotBluetoothConnection connection = new RobotBluetoothConnection();
    connection.initializeConnection();
    Utils.EnableRemoteLogging(connection);

    if (0 == 0) {
      return;
    }
    final QueuedPacketTransporter transporter = new QueuedPacketTransporter(connection);
    connection.RegisterTransporter(transporter, 123);
    final PrintStream stream = new PrintStream(transporter.getSendStream());
    Runnable communication = new Runnable() {

      public void run() {
        try {
          while (true) {
            String state = angie.fetchState();
            stream.println(state);

            transporter.SendPacket(123);
            Utils.Sleep(30);
          }
        } catch (Exception e) {
          Utils.Log("Comm crashed!");
        }
      }
    };
    Thread t = new Thread(communication);
    t.start();
  }
}