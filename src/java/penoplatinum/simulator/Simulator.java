package penoplatinum.simulator;

/**
 * Simulator
 * 
 * Accepts a robot, map and view and simulates the how the robot would run
 * this map (in a perfect world). Its main use is to test the theory behind
 * Navigator implementations, without the extra step onto the robot.
 * 
 * Future Improvements: Add support for multiple robots
 * 
 * @author: Team Platinum
 */
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.map.Map;
import penoplatinum.simulator.view.SilentSimulationView;
import penoplatinum.simulator.view.SimulationView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import penoplatinum.Config;
import penoplatinum.map.MapUtil;
import penoplatinum.util.Utils;

public class Simulator {

  // the Simulator can run until different goals are reached
  public static final double TIME_SLICE = 0.008;

  // a view to display the simulation, by default it does nothing
  SimulationView view = new SilentSimulationView();
  private Map map;                // the map that the robot will run on
  private List<RobotEntity> robotEntities = new ArrayList<RobotEntity>();
  
  private RobotEntity pacmanEntity;
  private ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<String>();
  private Runnable stepRunnable;


  public Simulator addSimulatedEntity(SimulatedEntity entity) {
    this.robotEntities.add(entity);
    this.view.addRobot(entity.getViewRobot());
    entity.useSimulator(this);
    return this;
  }
  
  /**
   * On a SimulationView, the Simulator can visually show what happens during
   * the simulation.
   */
  public Simulator displayOn(SimulationView view) {
    this.view = view;
    for (RobotEntity s : robotEntities) {
      view.addRobot(s.getViewRobot());
    }
    if (pacmanEntity != null) {
      view.addRobot(pacmanEntity.getViewRobot());
    }
    return this;
  }

  /**
   * A map can be provided. This will determine what information the 
   * Simulator will send to the robot's sensors, using the SimulationRobotAPI.
   */
  public Simulator useMap(Map map) {
    this.map = map;
    return this;
  }

  // determine the distance to the first obstacle in direct line of sight 
  // under a given angle
  public int getFreeDistance(double x, double y, int angle) {
    // find distance to first wall in line of sight
    return MapUtil.findHitDistance(this.map, angle, x, y);
  }

  /**
   * Allows the end-user to send commands through the communication layer
   * to the Robot. In the real world this is done through the GatewayClient,
   * which here is being provided and controlled by the Simulator.
   */
  public Simulator send(String cmd) {
    //robotEntities.robotAgent.receive(cmd);
    return this;
  }

  /**
   * This processes status-feedback from the GatewayClient, extracted from the
   * Model and Navigator.
   */
  public Simulator receive(String status) {
    // this is normally used by the PC client to implement a View of the 
    // Robot's mind.
    return this;
  }

  // at the end of a step, refresh the visual representation of our world
  private void refreshView() {
    this.view.updateRobots();
  }

  /**
   * This starts the actual simulation, which will start the robot and the 
   * agent.
   */
  public void run() {
    initRun();
    while (true) {
      this.step();
    }
  }
  
  public void run(long stepCount){
    initRun();
    while(stepCount-->0){
      this.step();
      Utils.Sleep(Config.SIMULATOR_WAIT);
    }
  }

  private void initRun() {
    for (int i = 0; i < this.robotEntities.size(); i++) {
      if (this.robotEntities.get(i) instanceof SimulatedEntity) {
        SimulatedEntity ent = (SimulatedEntity) this.robotEntities.get(i);
        ent.setInitialPosition(MapUtil.getCurrentTileCoordinates(ent.getPosX(), ent.getPosY(), getTileSize()));
        ent.setInitialBearing((4 - (int) (ent.getDirection() / 90) + 1) % 4);
      }
    }
    this.view.showMap(this.map);
  }

  private void step() {
    for (RobotEntity robotEntity : robotEntities) {
      robotEntity.step();
    }
    if (stepRunnable != null) {
      stepRunnable.run();
    }
    refreshView();
  }

  public RobotEntity getPacMan() {
    return this.pacmanEntity;
  }

  public void setPacmanEntity(RobotEntity pacmanEntity) {
    this.pacmanEntity = pacmanEntity;
    view.addRobot(pacmanEntity.getViewRobot());
  }

  public int getTileSize() {
    return map.getFirst().getSize();
  }

  public void useStepRunnable(Runnable runnable) {
    stepRunnable = runnable;
  }

  public SimulationView getView() {
    return view;
  }

  public Map getMap() {
    return map;
  }

  public SimulatedEntity getSimulatedEntityByName(String name) {
    for (int i = 0; i < this.robotEntities.size(); i++) {
      if (this.robotEntities.get(i) instanceof SimulatedEntity) {
        SimulatedEntity ent = (SimulatedEntity) this.robotEntities.get(i);
        if (ent.getRobot().getName().equals(name)) {
          return ent;
        }
      }
    }
    return null;
  }
}
