package penoplatinum.robot;

import penoplatinum.Config;
import penoplatinum.driver.Driver;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.model.GhostModel;
import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.MessageModelPart;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.part.SonarModelPart;
import penoplatinum.model.processor.ModelProcessor;
import penoplatinum.navigator.Navigator;
import penoplatinum.reporter.Reporter;

public class GhostRobot implements AdvancedRobot {

  /*
   * RobotParts
   */
  private Model model = new GhostModel();
  private RobotAPI robotAPI;
  private Driver driver;
  private Navigator navigator;
  private GatewayClient gatewayClient;
  private Reporter reporter;
  
  /*
   * Local variables
   */
  private float originalTurnedAngle;
  private int sweepID = 0;
  private int[] angles = {-90, 0, 90};

  public GhostRobot(ModelProcessor procs) {
    this.model.setProcessor(procs);
  }

  private void linkComponents() {
    if (this.driver != null) {
      this.driver.drive(this);
    }
    if (this.navigator != null) {
      this.navigator.useModel(model);
    }
    if (this.gatewayClient != null) {
      this.gatewayClient.setRobot(this);
    }
    if (this.reporter != null && this.gatewayClient != null) {
      this.reporter.useGatewayClient(this.gatewayClient);
    }
  }

  @Override
  public GhostRobot useRobotAPI(RobotAPI api) {
    this.robotAPI = api;
    //initialReference = this.api.getRelativeAngle(0);
    this.robotAPI.setSpeed(Config.M3, Config.MOTOR_SPEED_SONAR);
    this.robotAPI.setSpeed(Config.M2, Config.MOTOR_SPEED_MOVE);
    this.robotAPI.setSpeed(Config.M1, Config.MOTOR_SPEED_MOVE);
    originalTurnedAngle = this.robotAPI.getRelativeAngle(0);
    return this;
  }

  @Override
  public RobotAPI getRobotAPI() {
    return this.robotAPI;
  }

  @Override
  public GhostRobot useDriver(Driver driver) {
    this.driver = driver;
    linkComponents();
    return this;
  }

  @Override
  public Driver getDriver() {
    return this.driver;
  }

  @Override
  public GhostRobot useNavigator(Navigator navigator) {
    this.navigator = navigator;
    linkComponents();
    return this;
  }

  @Override
  public Navigator getNavigator() {
    return this.navigator;
  }

  @Override
  public GhostRobot useGatewayClient(GatewayClient gatewayClient) {
    this.gatewayClient = gatewayClient;
    linkComponents();
    return this;
  }

  @Override
  public GatewayClient getGatewayClient() {
    return this.gatewayClient;
  }

  @Override
  public GhostRobot useReporter(Reporter reporter) {
    this.reporter = reporter;
    linkComponents();
    return this;
  }

  @Override
  public Model getModel() {
    return model;
  }

  @Override
  public void processCommand(String cmd) {
    MessageModelPart.from(this.model).addIncomingMessage(cmd);
  }

  private void sendMessages() {
    if (this.gatewayClient == null) {
      return;
    }
    for (String msg : MessageModelPart.from(this.model).getOutgoingMessages()) {
      this.gatewayClient.send(msg, Config.BT_GHOST_PROTOCOL);
    }
  }

  @Override
  public String getName() {
    return MessageModelPart.from(this.model).getProtocolHandler().getName();
  }

  @Override
  public Boolean reachedGoal() {
    return false;
  }

  @Override
  public void stop() {
    this.robotAPI.stop();
    //GridModelPart.from(model).getMyAgent().deactivate();
  }

  @Override
  public void step() {
    if (initStep()) {       //get sensor and read bluetooth
      return;
    }
    if (driver.isBusy()) {  //driver
      driver.drive(this);
      return;
    }
    if (inCenterOfTile()) { //get new driveraction
      return;
    }
  }

  /**
   * What to do every step.
   * Abort when not activated
   * @return true is not yet activated.
   */
  private boolean initStep() {
    if (!GridModelPart.from(this.model).getMyAgent().isActive()) {
      // wait until we're active
      this.model.refresh();                                       // but we need to process incoming messages
      return true;
    }
    // poll other sensors and update model
    SensorModelPart.from(this.model).updateSensorValues(this.robotAPI.getSensorValues());
    SensorModelPart.from(this.model).setTotalTurnedAngle(this.robotAPI.getRelativeAngle(originalTurnedAngle));
    this.model.refresh();
    return false;
  }

  /**
   * What to do in the center of a tile when waiting:
   * 1) Get sweep information
   * 2) Instruct the driver
   * 3) Send updates
   * @return true when aborting
   */
  private boolean inCenterOfTile() {
    if (makeSweep()) {
      return true;
    }
    navigator.finish(driver);
    
    SonarModelPart.from(this.model).update(this.robotAPI.getSweepResult(), this.angles);
    this.sweepID = this.robotAPI.getSweepID();
    this.model.refresh(); // TODO: double call
    
    navigator.instruct(driver);
    sendMessages();
    if (this.reporter != null) {
      this.reporter.reportModelUpdate();
    }
    System.gc();
    return false;
  }

  /**
   * Only continues when a new sweep is available.
   * Otherwise make a new sweep, or wait.
   * @return true, if waiting
   *          false, when a new sweep is available
   */
  private boolean makeSweep() {
    // we want obstacle-information based on Sonar-values
    // as long as this is in progress, we wait
    if (this.robotAPI.isSweeping()) {
      return true;
    }

    // if the sweep is ready ...
    if (this.robotAPI.getSweepID() <= this.sweepID) {
      this.robotAPI.sweep(this.angles);
      return true;
    }
    return false;
  }
}
