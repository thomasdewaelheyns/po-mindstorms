package penoplatinum.fullTests.gatewayclient;

import penoplatinum.Config;
import penoplatinum.fullTests.hill.HillRobot;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.processor.BarcodeModelProcessor;
import penoplatinum.model.processor.LightModelProcessor;
import penoplatinum.model.processor.LineModelProcessor;
import penoplatinum.model.processor.ModelProcessor;
import penoplatinum.util.Point;

public class GateRobot extends HillRobot {

  private GatewayClient gatewayClient;

  @Override
  protected ModelProcessor getProcessors() {
    return new LightModelProcessor(
            new LineModelProcessor(
            new BarcodeModelProcessor()));
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
  public void processCommand(String cmd) {
    System.out.println("I have mail!" + cmd);
  }

  @Override
  public void step() {
    // poll other sensors and update model
    SensorModelPart.from(getModel()).updateSensorValues(getRobotAPI().getSensorValues());
    SensorModelPart.from(getModel()).setTotalTurnedAngle(getRobotAPI().getRelativeAngle(getOriginalAngle()));

    getModel().refresh();
    if (getDriver().isBusy()) {  //driver
      getDriver().proceed();
      return;
    }
    getNavigator().finish(getDriver());
    GridModelPart gridPart = GridModelPart.from(getModel());
    Point p = gridPart.getMyPosition();
    System.out.println("Sending mail: " + p);
    this.gatewayClient.send("Position: " + p, Config.BT_GHOST_PROTOCOL);
    getNavigator().instruct(getDriver());
  }

  @Override
  public String getName() {
    return "Hello, I can speak!";
  }
}
