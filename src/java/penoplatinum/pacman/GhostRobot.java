package penoplatinum.pacman;

import penoplatinum.driver.GhostDriver;
import java.util.ArrayList;
import penoplatinum.grid.GridView;
import penoplatinum.grid.Sector;
import penoplatinum.modelprocessor.BarcodeBlackModelProcessor;
import penoplatinum.modelprocessor.GridUpdateProcessor;
import penoplatinum.modelprocessor.HistogramModelProcessor;
import penoplatinum.modelprocessor.IRModelProcessor;
import penoplatinum.modelprocessor.InboxProcessor;
import penoplatinum.modelprocessor.LightColorModelProcessor;
import penoplatinum.modelprocessor.LineModelProcessor;
import penoplatinum.modelprocessor.ModelProcessor;
import penoplatinum.modelprocessor.WallDetectionModelProcessor;
import penoplatinum.modelprocessor.WallDetectorProcessor;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.ReferencePosition;
import penoplatinum.simulator.Robot;
import penoplatinum.simulator.RobotAPI;
import penoplatinum.simulator.RobotAgent;
import penoplatinum.simulator.mini.Bearing;
import penoplatinum.simulator.mini.Navigator;

public class GhostRobot implements Robot {

  private GhostModel model;
  private RobotAPI api;   // provided from the outside
  private GhostDriver driver;
  private Navigator navigator;
//  private int[] sweepAngles = new int[]{-105, -90, -75, -30, 0, 30, 75, 90, 105};
  private int[] sweepAngles = new int[]{-90, 0, 90};
  private ArrayList<Integer> sweepAnglesList = new ArrayList<Integer>();
  private boolean waitingForSweep = false;
  private RobotAgent agent;
  private ReferencePosition initialReference = new ReferencePosition();
  private GridUpdateProcessor gridUpdateProcessor;

  public GhostRobot(String name) {

    this.setupModel(name);

    for (int i = 0; i < sweepAngles.length; i++) {
      sweepAnglesList.add(sweepAngles[i]);
    }

  }

  public GhostRobot(String name, GridView view) {
    this(name);
    this.model.displayGridOn(view);
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
            new IRModelProcessor(
            new BarcodeBlackModelProcessor(
            new LineModelProcessor(
            new WallDetectionModelProcessor(
            new InboxProcessor(
            new WallDetectorProcessor(
            new GridUpdateProcessor()))))))));
    this.model.setProcessor(processors);

  }

  @Override
  public GhostRobot useRobotAPI(RobotAPI api) {
    this.api = api;
    this.driver = new GhostDriver(this.model, this.api);
    api.setReferencePoint(initialReference);
    this.api.setSpeed(Model.M3, 250); // set sonar speed to double of default
    this.api.setSpeed(Model.M2, 500); // set sonar speed to double of default
    this.api.setSpeed(Model.M1, 500); // set sonar speed to double of default
    return this;
  }

  public void useNavigator(Navigator nav) {
    this.navigator = nav;
    nav.setModel(model);
  }

  // 
  // 
  /**
   * incoming communication from other ghosts, used by RobotAgent to deliver
   * incoming messages from the other ghosts
   * This is thread safe
   * @param cmd 
   */
  public void processCommand(String cmd) {
    this.model.addIncomingMessage(cmd);
  }
  // the external tick...

  public void step() {


    // poll other sensors and update model
    this.model.updateSensorValues(this.api.getSensorValues());
    this.model.setTotalTurnedAngle(api.getRelativePosition(initialReference).getAngle());

    // Send dashboard info
    
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

    this.driver.perform(this.navigator.nextAction());


    model.printGridStats();
    // send outgoing messages
    this.sendMessages();
    System.gc();
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
  public GhostRobot useCommunicationAgent(RobotAgent agent) {
    this.agent = agent;

    agent.setRobot(this);

    agent.run();
    return this;
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

  @Override
  public String getName() {
    return model.getAgent().getName();
  }

  

}