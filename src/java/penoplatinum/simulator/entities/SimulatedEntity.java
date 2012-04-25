package penoplatinum.simulator.entities;

import java.util.Random;
import penoplatinum.map.MapUtil;
import penoplatinum.robot.Robot;
import penoplatinum.robot.SimulationRobotAPI;
import penoplatinum.simulator.RobotEntity;
import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.sensors.Motor;
import penoplatinum.simulator.view.ViewRobot;
import penoplatinum.util.Point;

public class SimulatedEntity implements RobotEntity {

  public final double LENGTH_ROBOT = 10.0;
  public static final double LIGHTSENSOR_DISTANCE = 5.0; // 10cm from centers
  private static final int NUMBER_OF_MOTORS = 3;
  
  private double positionX;       // the position of the robot in the world
  private double positionY;       //   expressed in X,Y coordinates
  private double direction;       //   and a direction it's facing
  private Point initialPosition;
  private int initialBearing;
  // the motorSpeeds and the sensorValues
  private int[] sensorValues;
  private Sensor[] sensors;
  private Robot robot;       // the actual robot
  private ViewRobot viewRobot;   // 
  private Simulator simulator;
  
  
  private Motor[] motors = new Motor[NUMBER_OF_MOTORS];
  private double[] previousTacho = new double[NUMBER_OF_MOTORS];  
  private Random random = new Random(987453231);

  protected SimulatedEntity(Robot robot, int numberOfSensors) {
    this.robot = robot;
    this.robot.useRobotAPI(new SimulationRobotAPI().setSimulatedEntity(this));
    this.viewRobot = (new SimulatedViewRobot(this));

    sensorValues = new int[numberOfSensors];
    sensors = new Sensor[numberOfSensors];
  }

  public void useSimulator(Simulator simulator) {
    this.simulator = simulator;
    for (Sensor s : this.sensors) {
      s.useSimulator(simulator);
    }
  }

  public void setupMotor(Motor motor, int motorPort) {
    this.motors[motorPort] = motor;  // these two need to be running
  }

  public Motor getMotor(int motorIndex) {
    return this.motors[motorIndex];
  }

  public void setSensor(int sensorPort, Sensor sensor) {
    this.sensors[sensorPort] = sensor;
    sensor.useSimulatedEntity(this);
    sensor.useSimulator(simulator);
  }

  /**
   * A robot is put on the map - as in the real world - on a certain place
   * and in a given direction.
   * The Simulator also instruments the robot with a RobotAPI and sets up
   * the GatewayClient to interact with the robot.
   */
  public SimulatedEntity putRobotAt(int x, int y, int direction) {
    this.positionX = x;
    this.positionY = y;
    this.direction = direction;
    return this;
  }

  @Override
  public double getPosX() {
    return positionX;
  }

  @Override
  public double getPosY() {
    return positionY;
  }

  /**
   * The direction of the current robot.
   * 0° is north. Counterclockwise
   * @return the direction
   */
  @Override
  public double getDir() {
    return direction;
  }

  /**
   * Our internal representation of the bearing uses zero pointing north.
   * Math functions use zero pointing east.
   * We also only want an angle from 0 to 359.
   */
  public int getAngle() {
    return (int) ((this.direction + 90) % 360);
  }

  @Override
  public ViewRobot getViewRobot() {
    return this.viewRobot;
  }

  public Robot getRobot() {
    return this.robot;
  }

  public int[] getSensorValues() {
    return this.sensorValues;
  }

  // performs the next step in the movement currently executed by the robot
  @Override
  public void step() {
    // let all motors know that another timeslice has passed

    this.motors[EntityConfig.MOTOR_LEFT].tick(simulator.TIME_SLICE);
    this.motors[EntityConfig.MOTOR_RIGHT].tick(simulator.TIME_SLICE);
    this.motors[EntityConfig.MOTOR_SONAR].tick(simulator.TIME_SLICE);

    // based on the motor's (new) angle's determine the displacement
    double changeLeft = this.motors[EntityConfig.MOTOR_LEFT].getFullAngleTurned() - previousTacho[EntityConfig.MOTOR_LEFT];
    double changeRight = this.motors[EntityConfig.MOTOR_RIGHT].getFullAngleTurned() - previousTacho[EntityConfig.MOTOR_RIGHT];
    previousTacho[EntityConfig.MOTOR_LEFT] = this.motors[EntityConfig.MOTOR_LEFT].getFullAngleTurned();
    previousTacho[EntityConfig.MOTOR_RIGHT] = this.motors[EntityConfig.MOTOR_RIGHT].getFullAngleTurned();

    moveEntity((changeRight+changeLeft)/2);
    turnEntity((changeLeft-changeRight) /2);

    // based on the new location, determine the value of the different sensors
    this.updateSensorValues();
    this.getRobot().step();
  }

  private void moveEntity(double distance) {
    // we're moving in one direction 
    double error = 0.00;//0.05;
    double afwijking = 0;// 0.1;
    double d = EntityConfig.WHEEL_SIZE / 360 * distance;
    d *= 1 + (random.nextDouble() - 0.5 + afwijking) * error;
    double dx = Math.cos(Math.toRadians(this.getAngle())) * d;
    double dy = Math.sin(Math.toRadians(this.getAngle())) * d;
    if (MapUtil.hasTile(simulator.getMap(), this.positionX + dx, this.positionY - dy)) {
      if (!MapUtil.goesThroughWallX(simulator.getMap(), this, dx)) {
        this.positionX += dx;
      }
      if (!MapUtil.goesThroughWallY(simulator.getMap(), this, dy)) {
        this.positionY -= dy;
      }
    }
  }

  private void turnEntity(double angleTacho) {
    // we're turning
    double error = 0.00; //0.05;
    double afwijking = 0.3;
    double angle = EntityConfig.WHEEL_SIZE / 360 * angleTacho;
    angle = angle * (1 + (random.nextDouble() - 0.5 + afwijking) * error);
    double dr = (angle / (Math.PI * EntityConfig.WHEEL_BASE)) * 360;
    this.direction += dr;
  }

  /**
   * based on the robot's position, determine the values for the different
   * sensors.
   * TODO: extract the robot's physical configuration into separate object
   *       this is shared with the SensorConfig in a way (for now)
   */
  private void updateSensorValues() {
    for (int i = 0; i < sensorValues.length; i++) {
      sensorValues[i] = sensors[i].getValue();
    }
  }

  public Point getCurrentTileCoordinates() {
    // determine tile coordinates we're on
    int left = (int) (this.positionX / simulator.getTileSize()) + 1;
    int top = (int) (this.positionY / simulator.getTileSize()) + 1;
    return new Point(left, top);
  }

  public Point getCurrentOnTileCoordinates() {
    // determine tile coordinates on the tile we're on
    int left = (int) (this.positionX % simulator.getTileSize());
    int top = (int) (this.positionY % simulator.getTileSize());
    return new Point(left, top);
  }

  public double getDirection() {
    return this.direction;
  }

  public Point getInitialPosition() {
    return initialPosition;
  }

  public void setInitialPosition(Point initialPosition) {
    this.initialPosition = initialPosition;
  }

  public int getInitialBearing() {
    return initialBearing;
  }

  public void setInitialBearing(int initialBearing) {
    this.initialBearing = initialBearing;
  }
}