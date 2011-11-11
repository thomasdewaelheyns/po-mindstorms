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
 * Author: Team Platinum
 */
 
import java.awt.Point;
 
class Simulator {
  private static int tileSize = 80;  // our tiles are 80cm
  private static int scale    = 2;   // 1cm = 2px
  
  private SimulationView view;    // a view to display the simulation
  private Map map;                // the map that the robot will run on
  private SimulationRobotAPI   robotAPI;    // the API used to access hardware
  private SimulationRobotAgent robotAgent;  // the communication layer

  private Robot robot;            // the actual robot

  private double positionX;       // the position of the robot in the world
  private double positionY;       //   expressed in X,Y coordinates
  private double direction;       //   and a direction its facing
  
  private int[] sensorValues = new int[7]; // the current state of the sensors

  private int steps;              // the number of steps still to do
  private double dx, dy, dr;      // the difference for x, y and rotation
  
  private int currentMovement;    // movement that is being stepped
  private int lastChangeM1 = 0;    // the last change on Motor 1
  private int lastChangeM2 = 0;    // the last change on Motor 2
  private int lastChangeM3 = 0;    // the last change on Motor 3

  // main constructor, no arguments, Simulator is selfcontained
  public Simulator() {
    this.setupSimulationEnvironment();
  }

  /**
   * The simulation environment provides an implementation of the RobotAPI
   * and RobotAgent which are wired to the Simulator. This allows any default
   * robot to be tested without modification.
   */
  private void setupSimulationEnvironment() {
    this.robotAPI = new SimulationRobotAPI();
    this.robotAPI.setSimulator(this);         // provide callback object);
    this.robotAgent = new SimulationRobotAgent();
    this.robotAgent.setSimulator(this);       // provide callback object);
  }

  /**
   * On a SimulationView, the Simulator can visually show what happens during
   * the simulation.
   */
  public Simulator displayOn(SimulationView view) {
    this.view = view;
    if( this.map != null ) {
      this.view.showMap(map);
    }
    return this;
  }

  /**
   * A map can be provided. This will determine what information the 
   * Simulator will send to the robot's sensors, using the SimulationRobotAPI.
   */
  public Simulator useMap(Map map) {
    this.map = map;
    if( this.view != null ) {
      this.view.showMap(map);
    }
    return this;
  }
  
  /**
   * A robot is put on the map - as in the real world - on a certain place
   * and in a given direction.
   * The Simulator also instruments the robot with a RobotAPI and sets up
   * the RobotAgent to interact with the robot.
   */
  public Simulator putRobotAt(Robot robot, int x, int y, int direction) {
    this.robot = robot;
    this.positionX = x;
    this.positionY = y;
    this.direction = direction;
    
    // we provide the robot with our SimulationAPI
    this.robot.useRobotAPI( this.robotAPI );

    // we connect our SimulationRobotAgent to the robot
    this.robotAgent.setRobot( this.robot );

    return this;
  }
  
  public Simulator moveRobot( double movement ) {
    this.currentMovement = Navigator.MOVE;
    
    // our direction 0 is pointing North
    double rads = Math.toRadians(this.direction + 90);

    // convert from distance in meters to cm
    int distance = (int)(movement * 100.0);
    int direction = 1;
    if( distance < 0 ) {
      direction = -1;
      distance *= -1;
    }

    this.dx    = Math.cos(rads) * direction;
    this.dy    = Math.sin(rads) * direction;
    this.steps = distance;
    
    return this;
  }
  
  public Simulator turnRobot( double angle ) {
    this.currentMovement = Navigator.TURN;
    
    // turn in steps of 3 degrees
    this.dr   = 3.0;
    this.steps = (int)(Math.abs(angle) / this.dr);
    // if the angle is negative, move clock-wise
    if( angle < 0 ) {
      this.dr = -3.0;
    }

    return this;
  }
  
  public Simulator stopRobot() {
    this.currentMovement = Navigator.STOP;
    return this;
  }

  private void refreshView() {
    this.view.updateRobot( (int)this.positionX, (int)this.positionY,
                           (int)this.direction );
  }
  
  /**
   * Performs the next step in the movement currently executed by the robot
   */ 
  private void step() {
    this.lastChangeM1 = 0;
    this.lastChangeM2 = 0;
    // process the next step in the movement that is currently being performed
    switch( this.currentMovement ) {
      case Navigator.MOVE:
        if( this.steps-- > 0 ) {
          this.positionX += this.dx;
          this.positionY -= this.dy;
          // TODO: fix this to be correct towards actual change
          this.lastChangeM1 = 1;
          this.lastChangeM2 = 1;
        }
        break;
      case Navigator.TURN:
        if( this.steps-- > 0 ) {
          this.direction = this.direction + this.dr;
          if( this.direction < 0 ) {
            this.direction += 360;
          }
          this.direction %= 360;
          // TODO: fix this to be correct towards dimensions of robot
          this.lastChangeM1 = 1;
          this.lastChangeM2 = -1;
        }
        break;
      case Navigator.STOP:
        this.currentMovement = Navigator.NONE;
        break;
      case Navigator.NONE:
      default:
        // do nothing
    }
    
    // based on the new location, determine the value of the different sensors
    this.updateSensorValues();

    // always refresh our SimulationView
    this.refreshView();
  }
  
  /**
   * based on the robot's position, determine the values for the different
   * sensors.
   * TODO: extract the robot's physical configuration into separate object
   *       this is shared with the Model in a way (for now)
   */
  private void updateSensorValues() {
    this.updateFrontPushSensors();
    this.updateLightSensor();
  }
  
  private void updateFrontPushSensors() {
    int lengthRobot = 20;
    
    int distance = this.getFreeFrontDistance();

    if( distance < lengthRobot / 2 ) {
      this.sensorValues[Model.S1] = 50;
    } else {
      this.sensorValues[Model.S1] = 0;
    }
        
    this.sensorValues[Model.M1] = this.lastChangeM1;
    this.sensorValues[Model.M2] = this.lastChangeM2;
  }
  
  private void updateLightSensor() {
    // TODO: check lines
    // TODO: check barcodes
    // probably == a getColor() method
    
  }
  
  /**
   * Determine the distance to the first obstacle in direct line of sight
   */
  private int getFreeFrontDistance() {
    int distance = 0;
    
    // determine tile coordinates we're on
    int left = (int)Math.floor(this.positionX / Simulator.tileSize) + 1;
    int top  = (int)Math.floor(this.positionY / Simulator.tileSize) + 1;

    // determine position within tile
    int x = (int)this.positionX % Simulator.tileSize;
    int y = (int)this.positionY % Simulator.tileSize;

    // find distance to first wall in line of sight
    distance = (int)Math.round(this.findHitDistance(left, top, x, y, 0));

    System.out.println( (int)(x) + ", " + (int)(y) + " @ " + 
                        ((this.direction + 90) % 360) + " | " + distance );
    
    return distance;
  }
  
  /**
   * Our internal representation of the baring uses zero pointing north.
   * Math functions use zero pointing east.
   * We also only want an angle from 0 to 359.
   */
  private int getAngle() {
    return (int)( (this.direction + 90) % 360);    
  }
  
  /**
   * determines the distance to the first hit wall at the current baring.
   * if the hit is not on a wall on the current tile, we follow the baring
   * to the next tile and recusively try to find the hist-distance
   */
  private double findHitDistance(int left, int top, int x, int y, double d) {
    // determine the point on the (virtual) wall on the current tile, where
    // the robot would hit at this baring
    Point hit = Tile.findHitPoint(x,y, this.getAngle(), Simulator.tileSize);
    
    // distance from the starting point to the hit-point on this tile
    double dist = Tile.getDistance(x, y, hit);

    // if we don't have a wall on this tile at this baring, move to the next
    // at the same baring, starting at the hit point on the tile
    Tile tile   = this.map.get(left, top);
    int  baring = Tile.getHitWall(hit, Simulator.tileSize);
    if( ! tile.hasWall(baring) ) {
      int nextLeft = left + Baring.moveLeft(baring), 
          nextTop  = top  + Baring.moveTop(baring),
          nextX    = hit.x == 0 ? Simulator.tileSize : 
                   ( hit.x == Simulator.tileSize ? 0 : hit.x ),
          nextY    = hit.y == 0 ? Simulator.tileSize : 
                   ( hit.y == Simulator.tileSize ? 0 : hit.y );
      // recursively find more distance on the next tile
      dist = this.findHitDistance(nextLeft, nextTop, nextX, nextY, dist );
    }
    
    return d + dist;
  }

  /**
   * Allows the end-user to send commands throught the communication layer
   * to the Robot. In the real world this is done through the RobotAgent,
   * which here is being provided and controled by the Simulator.
   */
  public Simulator send( String cmd ) {
    this.robotAgent.receive( cmd );
    return this;
  }

  /**
   * This processes status-feedback from the RobotAgent, extracted from the
   * Model and Navigator.
   */ 
  public Simulator receive( String status ) {
    // this is normally used by the PC client to implement a View of the 
    // Robot's mind.
    return this;
  }
  
  /**
   * This starts the actual simulation, which will start the robot and the 
   * agent.
   */
  public Simulator run() {
    this.robotAgent.run();
    while( ! this.robot.reachedGoal() ) {
      this.robot.step();
      this.step();
    }
    return this;
  }
  
  public int[] getSensorValues() {
    return this.sensorValues;
  }

}
