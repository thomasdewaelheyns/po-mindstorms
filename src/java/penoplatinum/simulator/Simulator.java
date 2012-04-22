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
import penoplatinum.simulator.entities.RemoteEntity;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.map.Map;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.simulator.view.SilentSimulationView;
import penoplatinum.simulator.view.SimulationView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import penoplatinum.map.MapUtil;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class Simulator {

  /**
   * 
   */
  public static Simulator Running_Instance;
  // the Simulator can run until different goals are reached
  public static final double TIME_SLICE = 0.008;
  // a view to display the simulation, by default it does nothing
  SimulationView view = new SilentSimulationView();
  private Map map;                // the map that the robot will run on
  private List<RobotEntity> robotEntities = new ArrayList<RobotEntity>();
  private HashMap<String, RemoteEntity> remoteEntities = new HashMap<String, RemoteEntity>();
  private RobotEntity pacmanEntity;
  private ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<String>();
  private Runnable stepRunnable;

  // main constructor, no arguments, Simulator is selfcontained
  public Simulator() {
  }

  public void addSimulatedEntity(SimulatedEntity r) {
    robotEntities.add(r);
    view.addRobot(r.getViewRobot());
    r.useSimulator(this);
  }

  public RemoteEntity addRemoteEntity(String entityName, int originX, int originY, int originBearing) {

    RemoteEntity ent = new RemoteEntity(entityName);
    view.addRobot(ent.getViewRobot());

    ent.useSimulator(this);

    robotEntities.add(ent);
    remoteEntities.put(entityName, ent);
    ent.setOrigin(originX, originY, originBearing);

    return ent;
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
  public int getFreeDistance(Point tile, Point pos, int angle) {
    // find distance to first wall in line of sight
    return MapUtil.findHitDistance(this.map, angle,
            tile.getX(), tile.getY(),
            pos.getX(), pos.getY());
  }

  public Tile getCurrentTile(Point tile) {
    return this.map.get(tile.getX(), tile.getY());
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
  public Simulator run() {
    Simulator.Running_Instance = this;

    for (int i = 0; i < this.robotEntities.size(); i++) {
      if (this.robotEntities.get(i) instanceof SimulatedEntity) {
        SimulatedEntity ent = (SimulatedEntity) this.robotEntities.get(i);
        ent.setInitialPosition(ent.getCurrentTileCoordinates());
        ent.setInitialBearing((4 - (int) (ent.getDirection() / 90) + 1) % 4);
      }
    }

    this.view.showMap(this.map);


    while (true) {
      this.step();
      if (false) {
        break;
      }
//      Utils.Sleep(3);
    }
    this.view.log("");
    return this;
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

  public boolean hasTile(double positionX, double positionY) {
    int x = (int) positionX / this.getTileSize() + 1;
    int y = (int) positionY / this.getTileSize() + 1;
    return map.exists(x, y);
  }

  public boolean goesThroughWallX(SimulatedEntity entity, double dx) {
    double positionX = entity.getPosX();
    double positionY = entity.getPosY();
    double LENGTH_ROBOT = entity.LENGTH_ROBOT;

    double posXOnTile = positionX % this.getTileSize();
    int tileX = (int) positionX / this.getTileSize() + 1;
    int tileY = (int) positionY / this.getTileSize() + 1;
    return (this.map.get(tileX, tileY).hasWall(Bearing.W)
            && dx < 0 && (posXOnTile + dx < LENGTH_ROBOT))
            || (this.map.get(tileX, tileY).hasWall(Bearing.E)
            && dx > 0 && (posXOnTile + dx > this.getTileSize() - LENGTH_ROBOT));
  }

  public boolean goesThroughWallY(SimulatedEntity entity, double dy) {
    double positionX = entity.getPosX();
    double positionY = entity.getPosY();
    double LENGTH_ROBOT = entity.LENGTH_ROBOT;

    double posYOnTile = positionY % this.getTileSize();
    int tileX = (int) positionX / this.getTileSize() + 1;
    int tileY = (int) positionY / this.getTileSize() + 1;

    return (this.map.get(tileX, tileY).hasWall(Bearing.N)
            && dy > 0 && (posYOnTile - dy < LENGTH_ROBOT))
            || (this.map.get(tileX, tileY).hasWall(Bearing.S)
            && dy < 0 && (posYOnTile - dy > this.getTileSize() - LENGTH_ROBOT));
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
