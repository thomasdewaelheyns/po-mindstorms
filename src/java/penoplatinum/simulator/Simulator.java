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
import penoplatinum.simulator.tiles.TileGeometry;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.simulator.view.SilentSimulationView;
import penoplatinum.simulator.view.SimulationView;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Simulator {
  // the Simulator can run until different goals are reached
  
  public static final double TIME_SLICE = 0.008;

  // a view to display the simulation, by default it does nothing
  SimulationView view = new SilentSimulationView();
  private Map map;                // the map that the robot will run on
  
  private List<RobotEntity> robotEntities = new ArrayList<RobotEntity>();
  private SimulatedEntity pacmanEntity;
  
  
  // main constructor, no arguments, Simulator is selfcontained
  public Simulator() {
  }
  
  public void addSimulatedEntity(SimulatedEntity r){
    robotEntities.add(r);
    view.addRobot(r.getViewRobot());
    r.useSimulator(this);
  }

  /**
   * On a SimulationView, the Simulator can visually show what happens during
   * the simulation.
   */
  public Simulator displayOn(SimulationView view) {
    this.view = view;
    for(RobotEntity s:robotEntities){
      view.addRobot(s.getViewRobot());
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
    int distance = 0;

    // find distance to first wall in line of sight
    return this.findHitDistance(angle,
            (int) tile.getX(), (int) tile.getY(),
            (int) pos.getX(), (int) pos.getY());
  }

  public Tile getCurrentTile(Point tile) {
    return this.map.get((int) tile.getX(), (int) tile.getY());
  }

  /**
   * determines the distance to the first hit wall at the current baring.
   * if the hit is not on a wall on the current tile, we follow the baring
   * to the next tile and recursively try to find the hist-distance
   */
  private int findHitDistance(int angle, int left, int top, double x, double y) {
    // determine the point on the (virtual) wall on the current tile, where
    // the robot would hit at this baring
    double dist = 0;
    int baring;
    Tile tile;
    Point hit;
    do {
      tile = this.map.get(left, top);
      
      hit = TileGeometry.findHitPoint(x, y, angle, tile.getSize());

      // distance from the starting point to the hit-point on this tile
      dist += TileGeometry.getDistance(x, y, hit);

      // if we don't have a wall on this tile at this baring, move to the next
      // at the same baring, starting at the hit point on the tile
      // FIXME: throws OutOfBoundException, because we appear to be moving
      //        through walls.
      baring = TileGeometry.getHitWall(hit, tile.getSize());      
      left = left + Baring.moveLeft(baring);
      top = top + Baring.moveTop(baring);
      x = hit.x == 0 ? tile.getSize() : (hit.x == tile.getSize() ? 0 : hit.x);
      y = hit.y == 0 ? tile.getSize() : (hit.y == tile.getSize() ? 0 : hit.y);
      
    } while (!tile.hasWall(baring));
    return (int) Math.round(dist);
  }

  /**
   * Allows the end-user to send commands through the communication layer
   * to the Robot. In the real world this is done through the RobotAgent,
   * which here is being provided and controlled by the Simulator.
   */
  public Simulator send(String cmd) {
    //robotEntities.robotAgent.receive(cmd);
    return this;
  }

  /**
   * This processes status-feedback from the RobotAgent, extracted from the
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
    this.view.showMap(this.map);
    /*for(RobotEntity s:robotEntities){
      s.robotAgent.run();
    }*/
    while (true) {
      this.step();
      if(false){
        break;
      }
//      Utils.Sleep(3);
    }
    this.view.log("");
    return this;
  }

  private void step() {
    for(RobotEntity robotEntity : robotEntities){
      robotEntity.step();
    }
    refreshView();
    
  }

  boolean hasTile(double positionX, double positionY) {
    int x = (int) positionX / this.getTileSize()+ 1;
    int y = (int) positionY / this.getTileSize() + 1;
    return map.exists(x, y);
  }

  boolean goesThroughWallX(SimulatedEntity entity, double dx) {
    double positionX = entity.getPosX();
    double positionY = entity.getPosY();
    double LENGTH_ROBOT = entity.LENGTH_ROBOT;
    
    double posXOnTile = positionX % this.getTileSize();
    int tileX = (int) positionX / this.getTileSize() + 1;
    int tileY = (int) positionY / this.getTileSize() + 1;
    return (this.map.get(tileX, tileY).hasWall(Baring.W)
            && dx < 0 && (posXOnTile + dx < LENGTH_ROBOT))
            || (this.map.get(tileX, tileY).hasWall(Baring.E)
            && dx > 0 && (posXOnTile + dx > this.getTileSize() - LENGTH_ROBOT));
  }

  boolean goesThroughWallY(SimulatedEntity entity, double dy) {
    double positionX = entity.getPosX();
    double positionY = entity.getPosY();
    double LENGTH_ROBOT = entity.LENGTH_ROBOT;
    
    double posYOnTile = positionY % this.getTileSize();
    int tileX = (int) positionX / this.getTileSize()+ 1;
    int tileY = (int) positionY / this.getTileSize() + 1;

    return (this.map.get(tileX, tileY).hasWall(Baring.N)
            && dy > 0 && (posYOnTile - dy < LENGTH_ROBOT))
            || (this.map.get(tileX, tileY).hasWall(Baring.S)
            && dy < 0 && (posYOnTile - dy > this.getTileSize() - LENGTH_ROBOT));
  }

  public SimulatedEntity getPacMan() {
    return pacmanEntity;
  }
  
  public int getTileSize(){
    return map.getFirst().getSize();
  }
}