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
import java.util.List;
import java.util.ArrayList;

class Simulator {
  // the Simulator can run until different goals are reached

  public static final int GOAL_ONE_LAP = 1;
  public static final int GOAL_SET_BY_ROBOT = 2;
  private int goal = GOAL_SET_BY_ROBOT;
  // distance to the lightsensor-position
  private static final double LIGHTSENSOR_DISTANCE = 10.0; // 10cm from center
  private static final double LENGTH_ROBOT = 10.0;
  private static final double WHEEL_SIZE = 17.5; // circumf. in cm
  private static final double WHEEL_BASE = 16.0; // wheeldist. in cm
  // determines how much time is passed with every step of the simulator
  private double timeSlice = 0.008;
  // used for statistics
  private long startTime;                 // start time in millis
  private List<Point> visitedTiles = new ArrayList<Point>();
  private long lastStatisticsReport = 0;  // time of last stat report
  private static double totalMovement = 0;
  // a view to display the simulation, by default it does nothing
  private SimulationView view = new SilentSimulationView();
  private Map map;                // the map that the robot will run on
  private SimulationRobotAPI robotAPI;    // the API used to access hardware
  private SimulationRobotAgent robotAgent;  // the communication layer
  private Robot robot;            // the actual robot
  private double positionX;       // the position of the robot in the world
  private double positionY;       //   expressed in X,Y coordinates
  private double direction;       //   and a direction it's facing
  // the motorSpeeds and the sensorValues
  private double[] sensorValues = new double[Model.SENSORVALUES_NUM];
  private Motor[] motors = new Motor[3];
  private int prevLeft = 0;
  private int prevRight = 0;
  private int prevSonar = 0;

  // main constructor, no arguments, Simulator is selfcontained
  public Simulator() {
    this.setupVirtualRobot();
    this.setupSimulationEnvironment();
  }

  // setup internal representation of the robot
  private void setupVirtualRobot() {
    this.setupMotors();
  }

  // this needs to be in sync with the "reality" ;-)
  // TODO: externalize the speed configuration of the different motors
  private void setupMotors() {
    this.motors[Model.M1] = new Motor().setLabel("L");  // these two need to be running
    this.motors[Model.M2] = new Motor().setLabel("R");  // at the same speed
    this.motors[Model.M3] = new Motor().setLabel("S");  // this is the sonar
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

  public Simulator setSpeed(int motor, int speed) {
    this.motors[motor].setSpeed(speed);
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
    this.robot.useRobotAPI(this.robotAPI);

    // we connect our SimulationRobotAgent to the robot
    this.robotAgent.setRobot(this.robot);

    return this;
  }

  // called by the implementation of the RobotAPI
  public Simulator moveRobot(double movement) {
    movement *= 100;
    // calculate the tacho count we need to do to reach this movement
    int tacho = (int) (movement / WHEEL_SIZE * 360);
    this.motors[Model.M1].rotateBy(tacho);
    this.motors[Model.M2].rotateBy(tacho);
    return this;
  }

  // called by the implementation of the RobotAPI
  public Simulator turnRobot(int angle) {
    // calculate anmount of tacho needed to perform a turn by angle
    double dist = Math.PI * WHEEL_BASE / 360 * angle;
    int tacho = (int) (dist / WHEEL_SIZE * 360);

    // let both motor's rotate the same tacho but in opposite direction
    this.motors[Model.M1].rotateBy(tacho);
    this.motors[Model.M2].rotateBy(tacho * -1);
    return this;
  }

  // called by the implementation of the RobotAPI
  public Simulator stopRobot() {
    this.motors[Model.M1].stop();
    this.motors[Model.M2].stop();
    return this;
  }

  // low-level access method to a motor
  public Simulator rotateMotorTo(int motor, int tacho) {
    this.motors[motor].rotateTo(tacho);
    return this;
  }

  // at the end of a step, refresh the visual representation of our world
  private void refreshView() {
    this.view.updateRobot((int)this.positionX, (int)this.positionY, (int)this.direction,
            this.robot.getModel().getDistances(),
            this.robot.getModel().getAngles());
  }

  // performs the next step in the movement currently executed by the robot
  private void step() {
    // let all motors know that another timeslice has passed

    this.motors[Model.M1].tick(this.timeSlice);
    this.motors[Model.M2].tick(this.timeSlice);
    this.motors[Model.M3].tick(this.timeSlice);

    // based on the motor's (new) angle's determine the displacement
    int changeLeft = this.motors[Model.M1].getValue() - this.prevLeft;
    int changeRight = this.motors[Model.M2].getValue() - this.prevRight;

    if (changeLeft == changeRight) {
      // we're moving in one direction 
      double d = WHEEL_SIZE / 360 * changeRight;
      double dx = Math.cos(Math.toRadians(this.getAngle())) * d;
      double dy = Math.sin(Math.toRadians(this.getAngle())) * d;
      if (hasTile(this.positionX + dx, this.positionY + dy)) {
        if (!goesThroughWallX(this.positionX, this.positionY, dx)) {
          System.out.print(this.positionX+" "+ this.positionY+" "+dx+" ");
          this.positionX += dx;
        }
        if (!goesThroughWallY(this.positionX, this.positionY, dy)) {
          this.positionY -= dy;
        }
      }
      this.trackMovementStatistics(d);
    } else if (changeLeft == changeRight * -1) {
      // we're turning
      double d = WHEEL_SIZE / 360 * changeLeft;
      double dr = (d / (Math.PI * WHEEL_BASE)) * 360;
      this.direction += dr;
    } else {
      // hell froze over
      System.err.println("ERROR: inconsistent motor behaviour.");
      System.err.println(changeLeft + ", " + changeRight);
    }

    // keep track of the (new) current motor angles
    this.prevLeft = this.motors[Model.M1].getValue();
    this.prevRight = this.motors[Model.M2].getValue();
    this.prevSonar = this.motors[Model.M3].getValue();

    // based on the new location, determine the value of the different sensors
    this.updateSensorValues();

    // always refresh our SimulationView
    this.refreshView();
  }

  private void trackMovementStatistics(double movement) {
    this.totalMovement += movement;
    Point tile = this.getCurrentTileCoordinates();
    if (!this.visitedTiles.contains(tile)) {
      this.visitedTiles.add(tile);
    }
    // report the statistics every 2 seconds
    long current = System.currentTimeMillis();
    if (current - this.lastStatisticsReport > 2000) {
      this.lastStatisticsReport = current;
      //this.reportMovementStatistics();
    }
  }

  private void reportMovementStatistics() {
    this.view.log("");
    this.view.log("Total Distance = " + this.totalMovement + "cm");
    this.view.log("Visited Tiles  = " + this.visitedTiles.size());
    this.view.log("Fitness        = " + this.getFitness());
  }

  public double getFitness() {
    return this.totalMovement / this.visitedTiles.size();
  }

  /**
   * based on the robot's position, determine the values for the different
   * sensors.
   * TODO: extract the robot's physical configuration into separate object
   *       this is shared with the Model in a way (for now)
   */
  private void updateSensorValues() {
    this.updateMotors();
    this.updateFrontPushSensors();
    this.updateSonar();
    this.updateLightSensor();
  }

  private void updateFrontPushSensors() {
    this.calculateBumperSensor(45, LENGTH_ROBOT, Model.S1);
    this.calculateBumperSensor(315, LENGTH_ROBOT, Model.S2);
  }

  private void updateSonar() {
    int angle = (int) this.sensorValues[Model.M3] + this.getAngle();
    int minimum = this.getFreeDistance((angle + 360) % 360);
    // TODO: reintroduce ? - removed to find Sonar detection bug (xtof)
    // this "abuses" our ability to make many sonar checks at once ?!
    // for (int i = -15; i < 16; i++) {
    //   int distance = this.getFreeDistance((angle+i+360)%360);
    //   minimum = Math.min(minimum, distance);
    // }
    this.sensorValues[Model.S3] = minimum;
  }

  private void updateMotors() {
    this.sensorValues[Model.M1] = this.motors[Model.M1].getValue();
    this.sensorValues[Model.M2] = this.motors[Model.M2].getValue();
    this.sensorValues[Model.M3] = this.motors[Model.M3].getValue();
    this.sensorValues[Model.MS1] = getMotorState(motors[0]);
    this.sensorValues[Model.MS2] = getMotorState(motors[1]);
    this.sensorValues[Model.MS3] = getMotorState(motors[2]);
  }

  private int getMotorState(Motor m) {
    if (!m.isMoving()) {
      return Model.MOTORSTATE_STOPPED;
    }
    if (m.getDirection() == Motor.FORWARD) {
      return Model.MOTORSTATE_FORWARD;
    }
    if (m.getDirection() == Motor.BACKWARD) {
      return Model.MOTORSTATE_BACKWARD;
    }

    throw new RuntimeException("I M P O S S I B L E !");
  }

  private void updateLightSensor() {
    // determine position of light-sensor
    Point pos = this.getCurrentOnTileCoordinates();
    double rads = Math.toRadians(direction);
    int x = (int) (pos.getX() - LIGHTSENSOR_DISTANCE * Math.sin(rads));
    int y = (int) (pos.getY() - LIGHTSENSOR_DISTANCE * Math.cos(rads));

    // if we go beyond the boundaries of this tile, move to the next and
    // adapt the x,y coordinates on the new tile
    int dx = 0, dy = 0;
    if (x < 0) {
      dx = -1;
      x += Tile.SIZE;
    }
    if (x > Tile.SIZE) {
      dx = +1;
      x -= Tile.SIZE;
    }
    if (y < 0) {
      dy = -1;
      y += Tile.SIZE;
    }
    if (y > Tile.SIZE) {
      dy = +1;
      y -= Tile.SIZE;
    }
    // get correct tile
    Point tilePos = this.getCurrentTileCoordinates();
    Tile tile = this.map.get((int) tilePos.getX() + dx, (int) tilePos.getY() + dy);

    int color = tile.getColorAt(x, y);

    // TODO: add random abberations
    this.sensorValues[Model.S4] =
            color == Tile.WHITE ? 100 : (color == Tile.BLACK ? 0 : 70);
  }

  private void calculateBumperSensor(int angle, double lengthRobot, int sensorPort) {
    angle = (this.getAngle() + angle) % 360;
    int distance = this.getFreeDistance(angle);

    if (distance < lengthRobot / 2) {
      this.sensorValues[sensorPort] = 50;
    } else {
      this.sensorValues[sensorPort] = 0;
    }
  }

  // determine the distance to the first obstacle in direct line of sight 
  // under a given angle
  private int getFreeDistance(int angle) {
    int distance = 0;

    Point tile = this.getCurrentTileCoordinates();
    Point pos = this.getCurrentOnTileCoordinates();

    // find distance to first wall in line of sight
    return this.findHitDistance(angle,
            (int) tile.getX(), (int) tile.getY(),
            (int) pos.getX(), (int) pos.getY());
  }

  private Tile getCurrentTile() {
    Point tile = this.getCurrentTileCoordinates();
    return this.map.get((int) tile.getX(), (int) tile.getY());
  }

  public Point getCurrentTileCoordinates() {
    // determine tile coordinates we're on
    int left = (int) (this.positionX / Tile.SIZE)+ 1;
    int top = (int) (this.positionY / Tile.SIZE) + 1;
    return new Point(left, top);
  }

  public Point getCurrentOnTileCoordinates() {
    // determine tile coordinates on the tile we're on
    int left = (int) (this.positionX % Tile.SIZE);
    int top = (int) (this.positionY % Tile.SIZE);
    return new Point(left, top);
  }

  /**
   * Our internal representation of the baring uses zero pointing north.
   * Math functions use zero pointing east.
   * We also only want an angle from 0 to 359.
   */
  private int getAngle() {
    return (int) ((this.direction + 90) % 360);
  }

  /**
   * determines the distance to the first hit wall at the current baring.
   * if the hit is not on a wall on the current tile, we follow the baring
   * to the next tile and recursively try to find the hist-distance
   */
  private int findHitDistance(int angle, int left, int top, int x, int y) {
    // determine the point on the (virtual) wall on the current tile, where
    // the robot would hit at this baring
    double dist = 0;
    int baring;
    Tile tile;
    Point hit;
    do {
      hit = Tile.findHitPoint(x, y, angle, Tile.SIZE);

      // distance from the starting point to the hit-point on this tile
      dist += Tile.getDistance(x, y, hit);

      // if we don't have a wall on this tile at this baring, move to the next
      // at the same baring, starting at the hit point on the tile
      // FIXME: throws OutOfBoundException, because we appear to be moving
      //        through walls.
      tile = this.map.get(left, top);
      baring = Tile.getHitWall(hit, Tile.SIZE);

      left = left + Baring.moveLeft(baring);
      top = top + Baring.moveTop(baring);
      x = hit.x == 0 ? Tile.SIZE : (hit.x == Tile.SIZE ? 0 : hit.x);
      y = hit.y == 0 ? Tile.SIZE : (hit.y == Tile.SIZE ? 0 : hit.y);
    } while (!tile.hasWall(baring));

    return (int) Math.round(dist);
  }

  /**
   * Allows the end-user to send commands through the communication layer
   * to the Robot. In the real world this is done through the RobotAgent,
   * which here is being provided and controlled by the Simulator.
   */
  public Simulator send(String cmd) {
    this.robotAgent.receive(cmd);
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

  /**
   * This starts the actual simulation, which will start the robot and the 
   * agent.
   */
  public Simulator run() {
    this.view.showMap(this.map);
    this.startTime = System.currentTimeMillis();
    this.robotAgent.run();
    while (!this.reachedGoal()) {
      this.step();
      this.robot.step();
    }
    this.view.log("");
    this.reportMovementStatistics();
    return this;
  }

  // once our robot has visited all tiles on the map, we're done.
  private Boolean reachedGoal() {
    switch (this.goal) {
      case Simulator.GOAL_ONE_LAP:
        return this.visitedTiles.size() >= this.map.getTileCount();
      case Simulator.GOAL_SET_BY_ROBOT:
      default:
        return this.robot.reachedGoal();
    }
  }

  public double[] getSensorValues() {
    return this.sensorValues;
  }

  public boolean sonarMotorIsMoving() {
    return this.motors[Model.M3].getValue() != this.prevSonar;
  }

  private boolean hasTile(double positionX, double positionY) {
    int x = (int) positionX / Tile.SIZE + 1;
    int y = (int) positionY / Tile.SIZE + 1;
    return map.exists(x, y);
  }

  private boolean goesThroughWallX(double positionX, double positionY, double dx) {
    double posXOnTile = positionX % Tile.SIZE;
    int tileX = (int) positionX / Tile.SIZE + 1;
    int tileY = (int) positionY / Tile.SIZE + 1;
    System.out.println(posXOnTile+" "+dx+" "+tileX+" "+tileY+" "+this.map.get(tileX, tileY).hasWall(Baring.W)+" "+this.map.get(tileX, tileY).hasWall(Baring.E));
    return (this.map.get(tileX, tileY).hasWall(Baring.W)
            && dx < 0 && (posXOnTile + dx < LENGTH_ROBOT))
            || (this.map.get(tileX, tileY).hasWall(Baring.E)
            && dx > 0 && (posXOnTile + dx > Tile.SIZE - LENGTH_ROBOT));
  }

  private boolean goesThroughWallY(double positionX, double positionY, double dy) {
    double posYOnTile = positionY % Tile.SIZE;
    int tileX = (int) positionX / Tile.SIZE + 1;
    int tileY = (int) positionY / Tile.SIZE + 1;

    return (this.map.get(tileX, tileY).hasWall(Baring.N)
            && dy > 0 && (posYOnTile - dy < LENGTH_ROBOT))
            || (this.map.get(tileX, tileY).hasWall(Baring.S)
            && dy < 0 && (posYOnTile - dy > Tile.SIZE - LENGTH_ROBOT));
  }
}
