package penoplatinum;

import penoplatinum.util.Utils;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.bluetooth.RobotBluetoothGatewayClient;
import penoplatinum.driver.ManhattanDriver;
import penoplatinum.driver.behaviour.BarcodeDriverBehaviour;
import penoplatinum.driver.behaviour.FrontProximityDriverBehaviour;
import penoplatinum.driver.behaviour.LineDriverBehaviour;
import penoplatinum.driver.behaviour.SideProximityDriverBehaviour;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.navigator.GhostNavigator;
import penoplatinum.navigator.Navigator;
import penoplatinum.reporter.DashboardReporter;
import penoplatinum.robot.GhostRobot;
import penoplatinum.util.FPS;

public class Main {

  public static void main(String[] args) throws Exception {
    GhostRobot robot = new GhostRobot("PLATINUM");
    ManhattanDriver manhattan = new ManhattanDriver(0.4).addBehaviour(new FrontProximityDriverBehaviour()).addBehaviour(new SideProximityDriverBehaviour()).addBehaviour(new BarcodeDriverBehaviour()).addBehaviour(new LineDriverBehaviour());
    robot.useDriver(manhattan);

    Navigator navigator = new GhostNavigator();
    robot.useNavigator(navigator);

    GatewayClient gateway = new RobotBluetoothGatewayClient();
    robot.useGatewayClient(gateway);

    //MessageModelPart.from(robot.getModel()).setProtocolHandler(new GhostProtocolHandler);

    robot.useReporter(new DashboardReporter());

    robot.handleActivation();

    RobotBluetoothConnection conn = new RobotBluetoothConnection();
    conn.initializeConnection();
    Utils.EnableRemoteLogging(conn);

    FPS fps = new FPS();
    AngieRobotAPI angie = new AngieRobotAPI();
    robot.useRobotAPI(angie);

    while (true) {
      fps.setCheckPoint();
      angie.getSonar().updateSonarMovement();
      angie.setFps(fps.getFps());
      robot.step();
      fps.endCheckPoint();
    }
  }
}