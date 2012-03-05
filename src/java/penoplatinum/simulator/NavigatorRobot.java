package penoplatinum.simulator;

import penoplatinum.modelprocessor.SonarModelProcessor;
import penoplatinum.modelprocessor.HistogramModelProcessor;
import penoplatinum.modelprocessor.ModelProcessor;

import penoplatinum.Utils;
import penoplatinum.modelprocessor.LineModelProcessor;
import penoplatinum.modelprocessor.WallDetectionModelProcessor;

/**
 * NavigatorRobot
 * 
 * General purpose Navigator-based Robot implementation.
 *  
 * @author: Team Platinum
 */
public class NavigatorRobot implements Robot {

  private RobotAPI api;
  private RobotAgent agent;
  private Navigator navigator;
  private Model model;
  private ReferencePosition stepReference = new ReferencePosition();
  private ReferencePosition initialReference = new ReferencePosition();

  public NavigatorRobot() {
    this.setupModel();
  }

  public NavigatorRobot(Navigator navigator) {
    this();
    this.useNavigator(navigator);
  }

  private void setupModel() {
    // setup a model with the required ModelProcessors
    this.model = new OriginalModel();
    ModelProcessor histoBuilder =
            new HistogramModelProcessor(
            //new FrontPushModelProcessor(
            new SonarModelProcessor(
            //new GapModelProcessor(
            //new ProximityModelProcessor(
            //new LightCorruptionModelProcessor(
            //new BarcodeModelProcessor(
            new LineModelProcessor(
            new WallDetectionModelProcessor(null))));

    this.model.setProcessor(histoBuilder);
  }

  public Robot useNavigator(Navigator navigator) {
    // setup the navigator using the same model
    this.navigator = navigator;
    this.navigator.setModel(this.model);
    return this;
  }

  public Robot useRobotAPI(RobotAPI api) {
    this.api = api;
    this.initAPI();
    api.setReferencePoint(initialReference);
    return this;
  }

  public Robot useCommunicationAgent(RobotAgent agent) {
    this.agent = agent;
    return this;
  }

  // method to perform initializing actions when an API is available
  private void initAPI() {
    if (this.api == null) {
      return;
    }
    this.api.setSpeed(Model.M3, 500); // set sonar speed to double of default
    this.api.setSpeed(Model.M2, 125); // set sonar speed to double of default
    this.api.setSpeed(Model.M1, 125); // set sonar speed to double of default
    api.setReferencePoint(stepReference);
  }

  public void processCommand(String cmd) {
    // no input required
  }

  public String getModelState() {
    return this.model.toString();
  }

  public Model getModel() {
    return this.model;
  }

  public String getNavigatorState() {
    return this.navigator.toString();
  }
  ExtendedVector sendDelta = new ExtendedVector();

  /**
   * This implements one step in the Event Loop. The Robot polls its sensors
   * updates its model and asks the navigator what to do.
   */
  public void step() {
    if (this.navigator == null) {
      Utils.Log("WARNING: no navigator set");
    }
    // get sensor data and update the model (motors are sensors too)
    this.model.updateSensorValues(this.api.getSensorValues());



    // Update the robot's estimated position in the model
    ExtendedVector delta = api.getRelativePosition(stepReference);
    api.setReferencePoint(stepReference);
    model.setPositionX(model.getPositionX() + delta.getX());
    model.setPositionY(model.getPositionY() + delta.getY());
    model.setDirection(model.getDirection() + delta.getAngle());

    sendDelta.add(delta);


    // ask the navigator what to do next
    switch (this.navigator.nextAction()) {
      case Navigator.MOVE:
        this.api.move(this.navigator.getDistance());
        break;
      case Navigator.TURN:
        this.api.turn((int) this.navigator.getAngle());
        break;
      case Navigator.STOP:
        this.api.stop();
        break;
      case Navigator.NONE:
      default:
      // do nothing
    }


//    if (Math.abs(sendDelta.getX()) > 20 || Math.abs(sendDelta.getY()) > 20 || Math.abs(sendDelta.getAngle()) > 20) {
//      agent.send("p" + (int) model.getPositionX() + "," + (int) model.getPositionY() + "," + (int) model.getDirection());
//      sendDelta.zero();
//    }


//    Utils.Log(model.getTotalTurnedAngle() + "");
    //agent.send(getStatusMessage());

  }

  public String getStatusMessage() {
    return this.getModelState() + "," + this.getNavigatorState() + "," + "-1";
  }

  public Boolean reachedGoal() {
    return this.navigator.reachedGoal();
  }

  public void stop() {
    this.api.stop();
  }
}
