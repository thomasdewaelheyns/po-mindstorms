package penoplatinum.simulator;

import java.awt.Point;

public class SimulatedEntity {

  public final double LENGTH_ROBOT = 10.0;
  
  private static final double LIGHTSENSOR_DISTANCE = 10.0; // 10cm from center
  private static final double BUMPER_LENGTH_ROBOT = 11.0;
  private static final double WHEEL_SIZE = 17.5; // circumf. in cm
  private static final double WHEEL_BASE = 16.0; // wheeldist. in cm
  private double positionX;       // the position of the robot in the world
  private double positionY;       //   expressed in X,Y coordinates
  private double direction;       //   and a direction it's facing
  private double totalMovement = 0;
  private long lastStatisticsReport = 0;  // time of last stat report
  // the motorSpeeds and the sensorValues
  private double[] sensorValues = new double[Model.SENSORVALUES_NUM];
  private Motor[] motors = new Motor[3];
  private int prevLeft = 0;
  private int prevRight = 0;
  private int prevSonar = 0;
  SimulationRobotAPI robotAPI;    // the API used to access hardware
  SimulationRobotAgent robotAgent;  // the communication layer
  Robot robot;            // the actual robot
  
  private Simulator simulator;

  public SimulatedEntity(SimulationRobotAPI robotAPI, SimulationRobotAgent robotAgent, Robot robot) {
    this.setupMotors();
    this.robotAPI = robotAPI;
    this.robotAgent = robotAgent;
    this.robot = robot;
    robotAPI.setSimulatedEntity(this);
    robot.useRobotAPI(robotAPI);
  }
  
  public void useSimulator(Simulator simulator){
    this.simulator = simulator;
  }

  public void setPostition(double positionX, double positionY, double direction) {
    this.positionX = positionX;
    this.positionY = positionY;
    this.direction = direction;
  }

  // this needs to be in sync with the "reality" ;-)
  // TODO: externalize the speed configuration of the different motors
  private void setupMotors() {
    this.motors[Model.M1] = new Motor().setLabel("L");  // these two need to be running
    this.motors[Model.M2] = new Motor().setLabel("R");  // at the same speed
    this.motors[Model.M3] = new Motor().setLabel("S");  // this is the sonar
  }

  public SimulatedEntity setSpeed(int motor, int speed) {
    this.motors[motor].setSpeed(speed);
    return this;
  }

  /**
   * A robot is put on the map - as in the real world - on a certain place
   * and in a given direction.
   * The Simulator also instruments the robot with a RobotAPI and sets up
   * the RobotAgent to interact with the robot.
   */
  public SimulatedEntity putRobotAt(Robot robot, int x, int y, int direction) {
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
  public SimulatedEntity moveRobot(double movement) {
    movement *= 100;
    // calculate the tacho count we need to do to reach this movement
    int tacho = (int) (movement / WHEEL_SIZE * 360);
    this.motors[Model.M1].rotateBy(tacho);
    this.motors[Model.M2].rotateBy(tacho);
    return this;
  }

  // called by the implementation of the RobotAPI
  public SimulatedEntity turnRobot(int angle) {
    // calculate anmount of tacho needed to perform a turn by angle
    double dist = Math.PI * WHEEL_BASE / 360 * angle;
    int tacho = (int) (dist / WHEEL_SIZE * 360);

    // let both motor's rotate the same tacho but in opposite direction
    this.motors[Model.M1].rotateBy(tacho);
    this.motors[Model.M2].rotateBy(tacho * -1);
    return this;
  }

  // called by the implementation of the RobotAPI
  public SimulatedEntity stopRobot() {
    this.motors[Model.M1].stop();
    this.motors[Model.M2].stop();
    return this;
  }

  // low-level access method to a motor
  public SimulatedEntity rotateMotorTo(int motor, int tacho) {
    this.motors[motor].rotateTo(tacho);
    return this;
  }

  /**
   * Our internal representation of the baring uses zero pointing north.
   * Math functions use zero pointing east.
   * We also only want an angle from 0 to 359.
   */
  private int getAngle() {
    return (int) ((this.direction + 90) % 360);
  }

  public double[] getSensorValues() {
    return this.sensorValues;
  }
  
  public boolean sonarMotorIsMoving() {
    return this.motors[Model.M3].getValue() != this.prevSonar;
  }


  private void trackMovementStatistics(double movement) {
    this.totalMovement += movement;
    // report the statistics every 2 seconds
    long current = System.currentTimeMillis();
    if (current - this.lastStatisticsReport > 2000) {
      this.lastStatisticsReport = current;
      //this.reportMovementStatistics();
    }
  }

  // performs the next step in the movement currently executed by the robot
  void step() {
    // let all motors know that another timeslice has passed

    this.motors[Model.M1].tick(simulator.TIME_SLICE);
    this.motors[Model.M2].tick(simulator.TIME_SLICE);
    this.motors[Model.M3].tick(simulator.TIME_SLICE);

    // based on the motor's (new) angle's determine the displacement
    int changeLeft = this.motors[Model.M1].getValue() - this.prevLeft;
    int changeRight = this.motors[Model.M2].getValue() - this.prevRight;

    if (changeLeft == changeRight) {
      // we're moving in one direction 
      double d = WHEEL_SIZE / 360 * changeRight;
      double dx = Math.cos(Math.toRadians(this.getAngle())) * d;
      double dy = Math.sin(Math.toRadians(this.getAngle())) * d;
      if (simulator.hasTile(this.positionX + dx, this.positionY + dy)) {
        if (!simulator.goesThroughWallX(this, dx)) {
          this.positionX += dx;
        }
        if (!simulator.goesThroughWallY(this, dy)) {
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
    //simulator.refreshView();
    
    robot.step();
  }

  private void reportMovementStatistics() {
    simulator.view.log("");
    simulator.view.log("Total Distance = " + this.totalMovement + "cm");
    //simulator.view.log("Visited Tiles  = " + this.visitedTiles.size());
    //this.view.log("Fitness        = " + this.getFitness());
  }/*/

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
    this.calculateBumperSensor(45, Model.S1);
    this.calculateBumperSensor(315, Model.S2);
  }

  private void updateSonar() {
    int angle = (int) this.sensorValues[Model.M3] + this.getAngle();
    Point tile = getCurrentTileCoordinates();
    Point pos = getCurrentOnTileCoordinates();
    int minimum = simulator.getFreeDistance(tile, pos, (angle + 360) % 360);
    // TODO: reintroduce ? - removed to find Sonar detection bug (xtof)
    // this "abuses" our ability to make many sonar checks at once ?!
    // for (int i = -15; i < 16; i++) {
    //   int distance = this.getFreeDistance((angle+i+360)%360);
    //   minimum = Math.min(minimum, distance);
    // }
    this.sensorValues[Model.S3] = minimum > 90 ? 255 : minimum;
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
    Point pos = getCurrentOnTileCoordinates();
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
    if (x >= Tile.SIZE) {
      dx = +1;
      x -= Tile.SIZE;
    }
    if (y < 0) {
      dy = -1;
      y += Tile.SIZE;
    }
    if (y >= Tile.SIZE) {
      dy = +1;
      y -= Tile.SIZE;
    }
    // get correct tile
    Point tilePos = getCurrentTileCoordinates();
    Tile tile = simulator.getCurrentTile(tilePos);

    int color = tile.getColorAt(x, y);

    // TODO: add random abberations
    this.sensorValues[Model.S4] =
            color == Tile.WHITE ? 100 : (color == Tile.BLACK ? 0 : 70);
  }

  private void calculateBumperSensor(int angle, int sensorPort) {
    angle = (this.getAngle() + angle) % 360;
    Point tile = getCurrentTileCoordinates();
    Point pos = getCurrentOnTileCoordinates();
    int distance = simulator.getFreeDistance(tile, pos, angle);

    if (distance < BUMPER_LENGTH_ROBOT) {
      this.sensorValues[sensorPort] = 50;
    } else {
      this.sensorValues[sensorPort] = 0;
    }
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

  double getPosX() {
    return positionX;
  }
  
  double getPosY() {
    return positionY;
  }
  
  double getDir(){
    return direction;
  }
  
  ViewRobot getRobotView(){
    return new ViewRobot(this);
  }
}
