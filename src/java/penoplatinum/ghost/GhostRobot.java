package penoplatinum.ghost;

import java.util.ArrayList;
import penoplatinum.Utils;
import penoplatinum.modelprocessor.HistogramModelProcessor;
import penoplatinum.modelprocessor.LightColorModelProcessor;
import penoplatinum.modelprocessor.LineModelProcessor;
import penoplatinum.modelprocessor.ModelProcessor;
import penoplatinum.modelprocessor.WallDetectionModelProcessor;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.ReferencePosition;
import penoplatinum.simulator.Robot;
import penoplatinum.simulator.RobotAPI;
import penoplatinum.simulator.RobotAgent;

public class GhostRobot implements Robot {

  private GhostModel model;
  private RobotAPI api;   // provided from the outside
  private GhostDriver driver;
  private GhostNavigator navigator;
  private RobotAgent communicationAgent;
  private int[] sweepAngles = new int[]{-105, -90, -75, -30, 0, 30, 75, 90, 105};
  private ArrayList<Integer> sweepAnglesList = new ArrayList<Integer>();
  private boolean waitingForSweep = false;
  private RobotAgent agent;
  private ReferencePosition initialReference = new ReferencePosition();

  public GhostRobot(String name) {
    this.setupModel(name);

    for (int i = 0; i < sweepAngles.length; i++) {
      sweepAnglesList.add(sweepAngles[i]);
    }

  }

  public GhostRobot(String name, GridView view) {
    this(name);
    //this.model.displayOn(view);
  }

  public GhostRobot useProxy(ProxyAgent proxy) {
    return this;
  }

  private void setupModel(String name) {
    this.model = new GhostModel(name);
    ModelProcessor processors =
            new HistogramModelProcessor(
            new LightColorModelProcessor(
            //new FrontPushModelProcessor(
            //new SonarModelProcessor(
            //new GapModelProcessor(
            //new ProximityModelProcessor(
            //new LightCorruptionModelProcessor(
            //new BarcodeModelProcessor(
            new LineModelProcessor(
            new WallDetectionModelProcessor(
            new InboxProcessor(
            new WallDetectorProcessor(
            new GridUpdateProcessor()))))));
    this.model.setProcessor(processors);
  }

  @Override
  public void useRobotAPI(RobotAPI api) {
    this.api = api;
    this.driver = new GhostDriver(this.model, this.api);
    api.setReferencePoint(initialReference);
    this.api.setSpeed(Model.M3, 125); // set sonar speed to double of default
    this.api.setSpeed(Model.M2, 500); // set sonar speed to double of default
    this.api.setSpeed(Model.M1, 500); // set sonar speed to double of default
  }

  public void useNavigator(GhostNavigator nav) {
    this.navigator = nav;
  }

  // incoming communication from other ghosts, used by RobotAgent to deliver
  // incoming messages from the other ghosts
  public void processCommand(String cmd) {
    this.model.addIncomingMessage(cmd);
  }

  // the external tick...
  public void step() {
    // poll other sensors and update model
    this.model.updateSensorValues(this.api.getSensorValues());
    this.model.setTotalTurnedAngle(api.getRelativePosition(initialReference).getAngle());

    // let the driver do his thing
    if (this.driver.isBusy()) {
      this.driver.step();
      return;
    }

    // we want obstacle-information based on Sonar-values
    // as long as this is in progress, we wait
    if (this.api.sweepInProgress()) {
      return;
    }

    // if the sweep is ready ...
    if (this.waitingForSweep) {
      this.model.updateSonarValues(this.api.getSweepResult(), sweepAnglesList);
      this.waitingForSweep = false;
    } else {
      this.api.sweep(sweepAngles);
      this.waitingForSweep = true;
      return; // to wait for results
    }


    // NOTE: wallmodelprocessor hasn't run yet at this point, 
    //    so the model still contains old wall information!!!
    // ask navigator what to do and ...
    // let de driver drive, manhattan style ;-)
    this.model.updateSensorValues(this.api.getSensorValues()); // Double update
    this.driver.perform(this.navigator.nextActions());

    // send outgoing messages
    this.sendMessages();
  }

  private void sendMessages() {
    if (this.agent == null) {
      return;
    }
    for (String msg : this.model.getOutgoingMessages()) {
      this.agent.send(msg);
    }
    this.model.clearOutbox();
  }

  public Boolean reachedGoal() {
    return false;
  }

  public void stop() {
    this.api.stop();
  }

  @Override
  public void useCommunicationAgent(RobotAgent agent) {
    this.agent = agent;
  }

  @Override
  public String getModelState() {
    return "NOT IMPLEMENTED";
  }

  @Override
  public String getNavigatorState() {
    return "NOT IMPLEMENTED";
  }

  @Override
  public Model getModel() {
    return model;
  }

  public GhostModel getGhostModel() {
    return model;
  }
}