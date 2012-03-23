package penoplatinum.pacman;

import penoplatinum.model.GhostModel;
import penoplatinum.driver.GhostDriver;
import java.util.ArrayList;
import penoplatinum.model.GridModelPart;
import penoplatinum.util.Utils;
import penoplatinum.driver.Driver;
import penoplatinum.grid.GridView;
import penoplatinum.grid.Sector;
import penoplatinum.model.processor.AgentWallsUpdateProcessor;
import penoplatinum.model.processor.BarcodeBlackModelProcessor;
import penoplatinum.model.processor.GhostProtocolModelProcessor;
import penoplatinum.model.processor.GridRecalcModelProcessor;
import penoplatinum.model.processor.NewSectorsUpdateProcessor;
import penoplatinum.model.processor.HistogramModelProcessor;
import penoplatinum.model.processor.IRModelProcessor;
import penoplatinum.model.processor.InboxProcessor;
import penoplatinum.model.processor.LightColorModelProcessor;
import penoplatinum.model.processor.LineModelProcessor;
import penoplatinum.model.processor.MergeGridModelProcessor;
import penoplatinum.model.processor.ModelProcessor;
import penoplatinum.model.processor.WallDetectionModelProcessor;
import penoplatinum.model.processor.WallDetectorProcessor;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.ReferencePosition;
import penoplatinum.simulator.Robot;
import penoplatinum.simulator.RobotAPI;
import penoplatinum.simulator.RobotAgent;
import penoplatinum.simulator.Navigator;
import penoplatinum.simulator.mini.MessageHandler;
import penoplatinum.simulator.mini.Queue;

public class GhostRobot implements Robot {

  protected GhostModel model;
  protected RobotAPI api;   // provided from the outside
  private Driver driver;
  private Navigator navigator;
//  private int[] sweepAngles = new int[]{-105, -90, -75, -30, 0, 30, 75, 90, 105};
  private int[] sweepAngles = new int[]{-90, 0, 90};
  private ArrayList<Integer> sweepAnglesList = new ArrayList<Integer>();
  private boolean waitingForSweep = false;
  private RobotAgent agent;
  private ReferencePosition initialReference = new ReferencePosition();
  private DashboardAgent dashboardAgent;

  public GhostRobot(String name) {

    this.setupModel(name);

    for (int i = 0; i < sweepAngles.length; i++) {
      sweepAnglesList.add(sweepAngles[i]);
    }

    useDriver(new GhostDriver()); // default
  }

  public GhostRobot(String name, GridView view) {
    this(name);
    GridModelPart gridPart = this.model.getGridPart();
    gridPart.displayGridOn(view);
  }

  protected void setupModel(String name) {
    this.model = new GhostModel(name);

    


    ModelProcessor processors =
            new LightColorModelProcessor(
            new HistogramModelProcessor(
            new BarcodeBlackModelProcessor(
            new LineModelProcessor(
            new WallDetectionModelProcessor(
            new WallDetectorProcessor(
            new InboxProcessor(
            new AgentWallsUpdateProcessor(
            new NewSectorsUpdateProcessor(
            new IRModelProcessor(
            new GridRecalcModelProcessor(
            new GhostProtocolModelProcessor(
            new MergeGridModelProcessor()))))))))))));
    this.model.setProcessor(processors);

    // --- Set initial model state ---


    // Set the implementation of the ghost protocol to use

    model.getMessagePart().setProtocol(new GhostProtocolHandler(model, new GhostProtocolModelCommandHandler(model)));
    final Queue queue = new Queue();
    queue.subscribe(new MessageHandler() {

      @Override
      public void useQueue(Queue queue) {
      }

      @Override
      public void receive(String msg) {
        model.getMessagePart().queueOutgoingMessage(msg);
      }
    });
    model.getMessagePart().getProtocol().useQueue(queue);



  }

  @Override
  public GhostRobot useRobotAPI(RobotAPI api) {
    this.api = api;

    api.setReferencePoint(initialReference);
    this.api.setSpeed(Model.M3, 250); // set sonar speed to double of default
    this.api.setSpeed(Model.M2, 500); // set sonar speed to double of default
    this.api.setSpeed(Model.M1, 500); // set sonar speed to double of default
    linkComponents();
    return this;
  }

  public GhostRobot useNavigator(Navigator nav) {
    this.navigator = nav;
    linkComponents();
    return this;
  }

  public GhostRobot useDriver(Driver driver) {
    this.driver = driver;
    linkComponents();
    return this;
  }

  /**
   * This function links the driver, navigator and api correctly
   */
  protected void linkComponents() {
    if (driver != null) {
      driver.useRobotAPI(api);
      driver.useModel(model);

    }
    if (navigator != null) {
      navigator.setModel(model);
    }

  }

  public GhostRobot useDashboardAgent(DashboardAgent agent) {
    this.dashboardAgent = agent;
    agent.setRobot(this);
    agent.setModel(this.model);
    return this;
  }

  /**
   * incoming communication from other ghosts, used by RobotAgent to deliver
   * incoming messages from the other ghosts
   * This is thread safe
   * @param cmd 
   */
  public void processCommand(String cmd) {
    this.model.getMessagePart().addIncomingMessage(cmd);
  }

  public void step() {
    // poll other sensors and update model
    this.model.getSensorPart().updateSensorValues(this.api.getSensorValues());
    this.model.process();

    this.model.getSensorPart().setTotalTurnedAngle(api.getRelativePosition(initialReference).getAngle());

    // Send dashboard info
    if (dashboardAgent != null) {
      dashboardAgent.sendModelDeltas();
    }


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
      this.model.getSonarPart().updateSonarValues(this.api.getSweepResult(), sweepAnglesList);
      this.model.process(); // TODO: double call
      this.api.setSweeping(false);
      this.waitingForSweep = false;
    } else {
      this.api.setSweeping(true);
      this.api.sweep(sweepAngles);
      this.waitingForSweep = true;
      return; // to wait for results
    }



    // ask navigator what to do and ...
    // let de driver drive, manhattan style ;-)

    this.driver.perform(this.navigator.nextAction());


//    model.printGridStats();
    // send outgoing messages
    this.sendMessages();
    if (dashboardAgent != null) {


      dashboardAgent.sendGrid("myGrid", model.getGridPart().getGrid());


      // Send changed sectors
      // TODO: this will probably not work since the changes were cleared previously
      ArrayList<Sector> changed = model.getGridPart().getChangedSectors();
      for (int i = 0; i < changed.size(); i++) {
        Sector current = changed.get(i);

        // for each changed sector
        if (dashboardAgent != null) {
          dashboardAgent.sendSectorWalls(model.getGridPart().getAgent().getName(), "myGrid", current);
        }
      }
    }
    System.gc();
  }

  private void sendMessages() {
    if (this.agent == null) {
      return;
    }
    for (String msg : this.model.getMessagePart().getOutgoingMessages()) {
      this.agent.send(msg);
    }
    this.model.getMessagePart().clearOutbox();
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
    return model.getGridPart().getAgent().getName();
  }
}