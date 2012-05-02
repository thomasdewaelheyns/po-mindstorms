package penoplatinum.fullTests.gatewayclient;

import penoplatinum.Config;
import penoplatinum.driver.Driver;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.processor.BarcodeModelProcessor;
import penoplatinum.model.processor.LightModelProcessor;
import penoplatinum.model.processor.LineModelProcessor;
import penoplatinum.navigator.Navigator;
import penoplatinum.reporter.Reporter;
import penoplatinum.robot.AdvancedRobot;
import penoplatinum.robot.RobotAPI;
import penoplatinum.util.Point;

public class GateRobot implements AdvancedRobot {

  private Driver driver;
  private Navigator navigator;
  private RobotAPI api;
  private Model model;
  private GatewayClient gatewayClient;
  private int state = 0;
  private float originalAngle;

  public GateRobot() {
    this.model = new GateModel();
    this.model.setProcessor(new LightModelProcessor(
            new LineModelProcessor(
            new BarcodeModelProcessor())));
  }

  private void linkComponents() {
    if (this.driver != null) {
      this.driver.drive(this);
    }
    if (this.navigator != null) {
      this.navigator.useModel(this.model);
    }
  }

  @Override
  public GateRobot useDriver(Driver driver) {
    this.driver = driver;
    linkComponents();
    return this;
  }

  @Override
  public Driver getDriver() {
    return this.driver;
  }

  @Override
  public GateRobot useNavigator(Navigator navigator) {
    this.navigator = navigator;
    linkComponents();
    return this;
  }

  @Override
  public Navigator getNavigator() {
    return this.navigator;
  }

  @Override
  public GateRobot useGatewayClient(GatewayClient gatewayClient) {
    this.gatewayClient = gatewayClient;
    this.gatewayClient.setRobot(this);
    return this;
  }

  @Override
  public GatewayClient getGatewayClient() {
    return this.gatewayClient;
  }

  @Override
  public GateRobot useReporter(Reporter reporter) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Model getModel() {
    return this.model;
  }

  public void setModel(Model model) {
    this.model = model;
  }

  @Override
  public GateRobot useRobotAPI(RobotAPI api) {
    this.api = api;
    this.originalAngle = this.api.getRelativeAngle(0);
    return this;
  }

  @Override
  public RobotAPI getRobotAPI() {
    return this.api;
  }

  @Override
  public void processCommand(String cmd) {
    System.out.println("I have mail!" + cmd);
  }

  @Override
  public void step() {
    // poll other sensors and update model
    SensorModelPart.from(this.model).updateSensorValues(this.api.getSensorValues());
    SensorModelPart.from(this.model).setTotalTurnedAngle(this.api.getRelativeAngle(originalAngle));

    this.model.refresh();
    if (driver.isBusy()) {  //driver
      driver.proceed();
      return;
    }
    GridModelPart gridPart = GridModelPart.from(this.model);
    Point p = gridPart.getMyPosition();
    System.out.println("Sending mail: "+p);
    this.gatewayClient.send("Position: " + p, Config.BT_GHOST_PROTOCOL);
    navigator.instruct(driver);
  }

  @Override
  public Boolean reachedGoal() {
    return state == -1;
  }

  @Override
  public void stop() {
    this.api.stop();
    state = -1;
  }

  @Override
  public String getName() {
    return "Hello, I can read!";
  }
}
