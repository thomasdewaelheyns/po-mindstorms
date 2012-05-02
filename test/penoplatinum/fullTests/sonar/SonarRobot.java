package penoplatinum.fullTests.sonar;

import penoplatinum.Config;
import penoplatinum.fullTests.gatewayclient.GateRobot;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.part.SonarModelPart;
import penoplatinum.model.processor.BarcodeModelProcessor;
import penoplatinum.model.processor.FreeDistanceModelProcessor;
import penoplatinum.model.processor.LightModelProcessor;
import penoplatinum.model.processor.LineModelProcessor;
import penoplatinum.model.processor.ModelProcessor;
import penoplatinum.model.processor.WallDetectionModelProcessor;
import penoplatinum.util.Point;

public class SonarRobot extends GateRobot {

  private int sweepID;
  private int[] angles = new int[]{90, 0, -90};

  @Override
  protected ModelProcessor getProcessors() {
    return new LightModelProcessor(
            new LineModelProcessor(
            new BarcodeModelProcessor(
            new FreeDistanceModelProcessor(
            new WallDetectionModelProcessor()))));
  }

  @Override
  public String getName() {
    return "Hello, I can hear!";
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
    if (makeSweep()) {
      return;
    }
    getNavigator().finish(getDriver());
    
    SonarModelPart.from(getModel()).update(getRobotAPI().getSweepResult(), this.angles);
    this.sweepID = getRobotAPI().getSweepID();
    getModel().refresh(); // TODO: double call
    
    GridModelPart gridPart = GridModelPart.from(getModel());
    Point p = gridPart.getMyPosition();
    System.out.println("Sending mail: " + p);
    getGatewayClient().send("Position: " + p, Config.BT_GHOST_PROTOCOL);
    getNavigator().instruct(getDriver());
  }

  private boolean makeSweep() {
    // we want obstacle-information based on Sonar-values
    // as long as this is in progress, we wait
    if (getRobotAPI().isSweeping()) {
      return true;
    }

    // if the sweep is ready ...
    if (getRobotAPI().getSweepID() <= this.sweepID) {
      getRobotAPI().sweep(this.angles);
      return true;
    }
    return false;
  }
}
