package penoplatinum.fulltests.line;

import penoplatinum.driver.Driver;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.model.Model;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.processor.LightModelProcessor;
import penoplatinum.model.processor.LineModelProcessor;
import penoplatinum.model.processor.ModelProcessor;
import penoplatinum.navigator.Navigator;
import penoplatinum.reporter.Reporter;
import penoplatinum.robot.AdvancedRobot;
import penoplatinum.robot.RobotAPI;

public class LineRobot implements AdvancedRobot {

  private Driver driver;
  private Navigator navigator;
  private RobotAPI api;
  private Model model;
  private int state = 0;
  private float originalAngle;

  private void linkComponents() {
    if (this.driver != null) {
      this.driver.drive(this);
    }
    if (this.navigator != null) {
      this.navigator.useModel(this.model);
    }
  }

  @Override
  public LineRobot useDriver(Driver driver) {
    this.driver = driver;
    linkComponents();
    return this;
  }

  @Override
  public Driver getDriver() {
    return this.driver;
  }

  @Override
  public LineRobot useNavigator(Navigator navigator) {
    this.navigator = navigator;
    linkComponents();
    return this;
  }

  @Override
  public Navigator getNavigator() {
    return this.navigator;
  }

  @Override
  public LineRobot useGatewayClient(GatewayClient agent) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public GatewayClient getGatewayClient() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public LineRobot useReporter(Reporter reporter) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Model getModel() {
    return this.model;
  }

  public void setModel(Model model) {
    this.model = model;
    this.model.setProcessor(getProcessors());
  }

  @Override
  public LineRobot useRobotAPI(RobotAPI api) {
    this.api = api;
    this.originalAngle = this.api.getRelativeAngle(0);
    return this;
  }

  protected float getOriginalAngle() {
    return this.originalAngle;
  }

  @Override
  public RobotAPI getRobotAPI() {
    return this.api;
  }

  @Override
  public void processCommand(String cmd) {
    //do nothing
  }

  @Override
  public void step() {
    // poll other sensors and update model
    SensorModelPart.from(this.model).updateSensorValues(this.api.getSensorValues());
    SensorModelPart.from(this.model).setTotalTurnedAngle(this.api.getRelativeAngle(getOriginalAngle()));

    this.model.refresh();
    if (driver.isBusy()) {  //driver
      driver.proceed();
      return;
    }
    navigator.finish(driver);
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
    return "Hello, i am dumb but have a driver.";
  }

  protected ModelProcessor getProcessors() {
    return new LightModelProcessor(
            new LineModelProcessor());
  }
}
