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

import java.awt.Color;
import java.lang.System;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
 
class Simulator {
  private static int tileSize = 80;  // our tiles are 80cm
  private static int scale    = 2;   // 1cm = 2px
  
  // distance to the lightsensor-position
  private final int LIGHTSENSOR_DISTANCE = 10;
  
  private static double movementStep = 0.25;  // steps of 1/4 cm
  private long   startTime;                   // start time in millis
  private List<Point> visitedTiles = new ArrayList<Point>();
  private long   lastStatisticsReport = 0;    // time of last stat report

  private static double totalMovement = 0;

  // a view to display the simulation, by default it does nothing
  private SimulationView view = new SilentSimulationView();
  private Map map;                // the map that the robot will run on
  private SimulationRobotAPI   robotAPI;    // the API used to access hardware
  private SimulationRobotAgent robotAgent;  // the communication layer

  private Robot robot;            // the actual robot

  private double positionX;       // the position of the robot in the world
  private double positionY;       //   expressed in X,Y coordinates
  private double direction;       //   and a direction its facing
  
  // the motorSpeeds and the sensorValues
  private double[] sensorValues = new double[7];

  private int steps;              // the number of steps still to do
  private double dx, dy, dr;      // the difference for x, y and rotation
  private int dSonar;
  private int stepsSonar;
  
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
    double rads = Math.toRadians(this.getAngle());

    // convert from distance in meters to 1/25 cm
    int distance = (int)(movement * ( 100.0 / this.movementStep ) );
    int direction = 1;
    if( distance < 0 ) {
      direction = -1;
      distance *= -1;
    }

    this.dx    = Math.cos(rads) * this.movementStep * direction;
    this.dy    = Math.sin(rads) * this.movementStep * direction;
    this.steps = distance;
    
    return this;
  }
  
  public Simulator turnMotorTo(int angle){
    this.dSonar = ( angle >= 0 ? 1 : -1 );
    this.stepsSonar = angle * this.dSonar;
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

          this.trackMovementStatistics();
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

    // update the sonar motor
    if( this.stepsSonar-- > 0 ){
      this.sensorValues[Model.M3] += this.dSonar;
    }
    // based on the new location, determine the value of the different sensors
    this.updateSensorValues();

    // always refresh our SimulationView
    this.refreshView();
  }
  
  private void trackMovementStatistics() {
    this.totalMovement += this.movementStep;
    Point tile = this.getCurrentTileCoordinates();
    if( ! this.visitedTiles.contains(tile) ) {
      this.visitedTiles.add(tile);
    }
    // report the statistics every 2 seconds
    long current = System.currentTimeMillis();
    if( current - this.lastStatisticsReport > 2000 ) {
      this.lastStatisticsReport = current;
      this.reportMovementStatistics();
    }
  }
  
  private void reportMovementStatistics() {
    this.view.log("");
    this.view.log( "Total Distance = " + this.totalMovement + "cm" );
    this.view.log( "Visited Tiles  = " + this.visitedTiles.size() );
    this.view.log( "Fitness        = " + this.getFitness() );
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
    this.updateFrontPushSensors();
    this.updateSonar();
    this.updateMotors();
    this.updateLightSensor();
  }
  
  private void updateFrontPushSensors() {
    int lengthRobot = 20;
    this.calculateBumperSensor(45,  lengthRobot, Model.S1);
    this.calculateBumperSensor(315, lengthRobot, Model.S2);
  }
  
  private void updateSonar() {
    int angle = ((int)this.sensorValues[Model.M3] + this.getAngle() + 360) % 360;
    int distance = this.getFreeDistance(angle);
    this.sensorValues[Model.S3] = distance;
  }
  
  private void updateMotors() {
    this.sensorValues[Model.M1] = this.lastChangeM1;
    this.sensorValues[Model.M2] = this.lastChangeM2;
  }
  
  private void updateLightSensor() {
    // determine tile coordinates we're on
    // TODO also used in getFreeFrontDistance Refactoring
    int left = (int) Math.floor(this.positionX / Simulator.tileSize) + 1;
    int top = (int) Math.floor(this.positionY / Simulator.tileSize) + 1;

    // determine position within tile
    int x = (int) (this.positionX % Simulator.tileSize)*2;
    int y = (int) (this.positionY % Simulator.tileSize)*2;

    // determine Light Sensor Position
    Tile currentTile = map.get(left, top);

    // TODO: check lines
    int yposition = (int) (y+LIGHTSENSOR_DISTANCE*-Math.cos(direction));
    int xposition = (int) (x+LIGHTSENSOR_DISTANCE*Math.sin(direction));

    System.out.println(""+ xposition +"     "  +  yposition + "");

    Color currentColor = getLineColor(xposition, yposition, currentTile);

    if(getRobotOnBarcode(xposition, yposition, currentTile)){
      currentColor = getBarcodeColor(xposition,yposition,currentTile);
    }

    if(currentColor == Color.WHITE){
      this.sensorValues[Model.S4]= 100;
    } else if(currentColor== Color.BLACK){
      this.sensorValues[Model.S4]=0;
    } else {
      this.sensorValues[Model.S4]=70;
    }
  }
  //return the Color from the Barcode
  public Color getBarcodeColor(int x, int y, Tile currentTile){
    // normalisatie.
    int relativePosition= 0;
    switch(currentTile.getBarcodeLocation()){
      case Baring.N:
        relativePosition = y/Board.BARCODE_PIXEL_WIDTH;
        break;
      case Baring.S:
        relativePosition = (Board.TILE_WIDTH_AND_LENGTH-y) 
                           / Board.BARCODE_PIXEL_WIDTH;
        break;
      case Baring.E:
        relativePosition = (Board.TILE_WIDTH_AND_LENGTH -x)
                           / Board.BARCODE_PIXEL_WIDTH;
        break;
      case Baring.W:
        relativePosition = x / Board.BARCODE_PIXEL_WIDTH;
        break;
    }
    return ((currentTile.getBarcode() & (1<<relativePosition) ) != 0 ?
            Color.BLACK : Color.WHITE );
  }
    
  // check if the robot is on a barcode
  public boolean getRobotOnBarcode(int x, int y,Tile currentTile ) {
    switch(currentTile.getBarcodeLocation()){
      case Baring.N: return y<28;
      case Baring.E: return x>132;
      case Baring.S: return y>132;
      case Baring.W: return x<28;
    }
    return false;  
  }

  public Color getLineColor(int x, int y, Tile currentTile) {
    // check WEST and EAST  
    Color currentColor = new Color(205, 165, 100);
    Set firstLineValues = new HashSet<Integer>();
    Set secondLineValues = new HashSet<Integer>();

    for (int i = 0; i < (Board.LINE_PIXEL_WIDTH - 1); i++) {
      firstLineValues.add(Board.LINE_ORIGIN + i);
    }

    for (int i = 0; i < (Board.LINE_PIXEL_WIDTH - 1); i++) {
      secondLineValues.add(Board.LINE_ORIGIN + Board.TILE_WIDTH_AND_LENGTH 
                           - ( 2 * Board.LINE_ORIGIN ) + i);
    }

    if (firstLineValues.contains(x)) {

      if (currentTile.hasLine(Baring.W)) {
        currentColor = currentTile.hasLine(Baring.W, Tile.WHITE) ? 
                       Color.WHITE : Color.BLACK;
        currentColor = resetColorForTurnY(currentTile, y, currentColor);
      }
    }
    if (secondLineValues.contains(x)) {
      if (currentTile.hasLine(Baring.E)) {
        currentColor = currentTile.hasLine(Baring.E, Tile.WHITE) ?
                       Color.WHITE : Color.BLACK;
        currentColor = resetColorForTurnY(currentTile, y, currentColor);
      }
    }
    if (firstLineValues.contains(y)) {
      if (currentTile.hasLine(Baring.N)) {
        currentColor = currentTile.hasLine(Baring.N, Tile.WHITE) ?
                       Color.WHITE : Color.BLACK;
        currentColor = resetColorForTurnX(currentTile, x, currentColor);
      }
    }
    if (secondLineValues.contains(y)) {
      if (currentTile.hasLine(Baring.S)) {
        currentColor = currentTile.hasLine(Baring.S, Tile.WHITE) ?
                       Color.WHITE : Color.BLACK;
        currentColor = resetColorForTurnX(currentTile, x, currentColor);
      }
    }
    return currentColor;
  }
    
  private Color resetColorForTurnX(Tile tile, int x, Color color) {
    if(   tile.hasLine(Baring.W) && x < 40
       || tile.hasLine(Baring.E) && x > 120)
    {
      return new Color(205, 165, 100);
    }
    return color;
  }

  private Color resetColorForTurnY(Tile tile, int y, Color color) {
    if(    tile.hasLine(Baring.N) && y < 40
        || tile.hasLine(Baring.S) && y > 120) {
      return new Color(205, 165, 100);
    }
    return color;
  }

  private void calculateBumperSensor(int angle, int lengthRobot, int sensorPort) {
    angle = ( this.getAngle() + angle ) % 360; 
    int distance = this.getFreeDistance(angle);

    if( distance < lengthRobot / 2 ) {
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

    // determine position within tile
    int x = (int)this.positionX % Simulator.tileSize;
    int y = (int)this.positionY % Simulator.tileSize;

    // find distance to first wall in line of sight
    distance = this.findHitDistance(angle, (int)tile.getX(), (int)tile.getY(),
                                    x, y);

    return distance;
  }

  private Point getCurrentTileCoordinates() {
    // determine tile coordinates we're on
    int left = (int)Math.floor(this.positionX / Simulator.tileSize) + 1;
    int top  = (int)Math.floor(this.positionY / Simulator.tileSize) + 1;
    return new Point(left,top);
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
      hit = Tile.findHitPoint(x, y, angle, Simulator.tileSize);

      // distance from the starting point to the hit-point on this tile
      dist += Tile.getDistance(x, y, hit);

      // if we don't have a wall on this tile at this baring, move to the next
      // at the same baring, starting at the hit point on the tile
      // FIXME: throws OutOfBoundException, because we appear to be moving
      //        through walls.
      tile = this.map.get(left, top);
      baring = Tile.getHitWall(hit, Simulator.tileSize);

      left = left + Baring.moveLeft(baring);
      top  = top  + Baring.moveTop(baring);
      x = hit.x == 0 ? Simulator.tileSize
        : (hit.x == Simulator.tileSize ? 0 : hit.x);
      y = hit.y == 0 ? Simulator.tileSize
        : (hit.y == Simulator.tileSize ? 0 : hit.y);
    } while(! tile.hasWall(baring));

    return (int)Math.round(dist);
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
    this.view.showMap(this.map);
    this.startTime = System.currentTimeMillis();
    this.robotAgent.run();
    while( ! this.robot.reachedGoal() && ! this.reachedGoal() ) {
        System.out.println("" + this.sensorValues[Model.S4]+"");
      this.robot.step();
      this.step();
    }
    this.view.log( "" );
    this.view.log( "Visited All Tiles:" );
    this.reportMovementStatistics();
    return this;
  }
  
  // once our robot has visited all tiles on the map, we're done.
  private Boolean reachedGoal() {
    return this.visitedTiles.size() >= this.map.getTileCount();
  }
  
  public double[] getSensorValues() {
    return this.sensorValues;
  }

  public boolean sonarMotorIsMoving(){
    return this.stepsSonar > 0;
  }
}
