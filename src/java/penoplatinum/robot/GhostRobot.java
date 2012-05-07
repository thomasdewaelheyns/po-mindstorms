package penoplatinum.robot;

/**
 * GhostRobot
 *
 * Top-level aggregating and delegating implementation of THE ROBOT
 *
 * @author Team Platinum
 */

import penoplatinum.Config;

import penoplatinum.driver.Driver;
import penoplatinum.gateway.GatewayClient;

import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.grid.LinkedSector;
import penoplatinum.grid.agent.Agent;
import penoplatinum.grid.agent.BarcodeAgent;

import penoplatinum.grid.agent.GhostAgent;
import penoplatinum.model.Model;
import penoplatinum.model.GhostModel;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.MessageModelPart;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.part.SonarModelPart;
import penoplatinum.model.processor.*;

import penoplatinum.navigator.Navigator;

import penoplatinum.protocol.ExternalEventHandler;
import penoplatinum.protocol.GhostProtocolHandler;

import penoplatinum.reporter.Reporter;

import penoplatinum.util.Point;
import penoplatinum.util.Bearing;
import penoplatinum.util.FPS;

public class GhostRobot implements AdvancedRobot, ExternalEventHandler {

  private Model                model;
  private RobotAPI             robotAPI;
  private Driver               driver;
  private Navigator            navigator;
  private GatewayClient        gatewayClient;
  private Reporter             reporter;
  
  private FPS fps = new FPS();
  
  private String name;

  private int   sweepID = 0;
  private int[] angles = {-90, 0, 90};

  // overloaded constructor to allow providing a model
  public GhostRobot(String name, GhostModel model) {
    this.setupName(name);
    this.setupModel(model);
    this.setupProtocolHandling();
  }

  // a robot needs a name ... it should be unique :-)
  public GhostRobot(String name) {
    this(name, new GhostModel(name));
  }

  private void setupModel(GhostModel model) {
    this.model = model;
    this.model.setProcessor( new LightModelProcessor(
                             new FreeDistanceModelProcessor(
                             new LineModelProcessor(
                             new IRModelProcessor(
                             new BarcodeModelProcessor(
                             new InboxModelProcessor(
                             new WallDetectionModelProcessor(
                             new ImportWallsModelProcessor(
                             new UnknownSectorModelProcessor(
                             new ChangesModelProcessor(
                                             )))))))))));

    if( this.reporter != null && this.model != null ) {
      this.model.setReporter(this.reporter);
    }

    // TODO:

    // BarcodeWallsModelProcessor
    // IRModelProcessor
  }
  
  private void setupName(String name) {
    this.name = name;
  }

  public Model getModel() {
    return this.model;
  }

  
  private void setupProtocolHandling() {
    MessageModelPart.from(this.model)
      //  .setProtocolHandler(new BasicProtocolHandler());
      .setProtocolHandler(new GhostProtocolHandler().setName(getName())
                            .useExternalEventHandler(this));
  }

  public GhostRobot useRobotAPI(RobotAPI api) {
    this.robotAPI = api;
    this.robotAPI.setSpeed(Config.M3, Config.MOTOR_SPEED_SONAR);
    this.robotAPI.setSpeed(Config.M2, Config.MOTOR_SPEED_MOVE);
    this.robotAPI.setSpeed(Config.M1, Config.MOTOR_SPEED_MOVE);
    return this;
  }

  public RobotAPI getRobotAPI() {
    return this.robotAPI;
  }

  public GhostRobot useDriver(Driver driver) {
    this.driver = driver;
    if( this.driver != null ) {
      this.driver.drive(this);
    }
    return this;
  }

  public Driver getDriver() {
    return this.driver;
  }

  public GhostRobot useNavigator(Navigator navigator) {
    this.navigator = navigator;
    if( this.navigator != null ) {
      this.navigator.useModel(this.model);
    }
    return this;
  }

  public Navigator getNavigator() {
    return this.navigator;
  }

  public GhostRobot useGatewayClient(GatewayClient gatewayClient) {
    this.gatewayClient = gatewayClient;
    if( this.gatewayClient != null ) {
      this.gatewayClient.setRobot(this);
      MessageModelPart.from(this.model)
        .getProtocolHandler().useGatewayClient(this.gatewayClient);
      if( this.reporter != null ) {
        this.reporter.useGatewayClient(this.gatewayClient);
      }
    }
    return this;
  }

  public GatewayClient getGatewayClient() {
    return this.gatewayClient;
  }

  public GhostRobot useReporter(Reporter reporter) {
    this.reporter = reporter;
    if( this.reporter != null && this.gatewayClient != null ) {
      this.reporter.useGatewayClient(this.gatewayClient);
    }
    if( this.reporter != null && this.model != null ) {
      this.model.setReporter(this.reporter);
    }
    this.reporter.reportFor(this);
    return this;
  }

  // this method is used by the GateWayClient that has a reference to the
  // robot ... this should of course have been merged with the External-
  // EventHandling :-( too bad
  public void processCommand(String cmd) {
    MessageModelPart.from(this.model).addIncomingMessage(cmd);
  }

  private void sendMessages() {
    if( this.gatewayClient == null ) { return; }
    for( String msg : MessageModelPart.from(this.model).getOutgoingMessages()) {
      this.gatewayClient.send(msg, Config.BT_GHOST_PROTOCOL);
    }
  }

  public String getName() {
    return this.name;
  }

  public boolean reachedGoal() { return false; }

  public void stop() {
    this.robotAPI.stop();
  }

  private long prevCount = 0;
  // we are ...
  // 1) inactive
  // 2) still driving
  // 3) in the center of a tile
  public void step() {
    // TODO: move this to single instance
    MessageModelPart.from(model).getProtocolHandler().handleStart();

    this.startProfiling();
    
    if( ! this.isActive() ) { 
      this.model.refresh();
      return;
    }

    this.updateSensors();

    if( driver.isBusy() ) {
      this.driver.proceed(); 
      this.stopProfiling();
      return;
    }

    this.stopProfiling();

    this.inCenterOfTile();
  }
  
  private boolean isActive() {
    return GridModelPart.from(this.model).getMyAgent().isActive();  
  }

  private void startProfiling() {
    this.fps.setCheckPoint();
  }

  private void stopProfiling() {
    this.fps.endCheckPoint();
    SensorModelPart.from(this.model).setFPS(this.fps.getFps());
  }

  private void updateSensors() {
    // poll other sensors...
    SensorModelPart.from(this.model)
      .updateSensorValues(this.robotAPI.getSensorValues());
    SensorModelPart.from(this.model)
      .setTotalTurnedAngle(this.robotAPI.getRelativeAngle(0));
    // and update model      
    this.model.refresh();
  }

  /**
   * What to do in the center of a tile when waiting:
   * 1) Get sweep information
   * 2) Instruct the driver
   * 3) Send updates
   * @return true when aborting
   */
  private void inCenterOfTile() {
    if( ! this.newSweepIsReady() ) { return; }
    this.navigator.finish(driver);
    SonarModelPart.from(this.model).update(this.robotAPI.getSweepResult(), this.angles);
    this.sweepID = this.robotAPI.getSweepID();
    this.model.refresh();
    this.navigator.instruct(driver);
    this.sendMessages();
    if( this.reporter != null ) { this.reporter.reportModelUpdate(); }
    this.manageMemory();
  }
  
  /**
   * Only continues when a new sweep is available.
   * Otherwise make a new sweep, or wait.
   * @return true, if waiting
   *          false, when a new sweep is available
   */
  private boolean newSweepIsReady() {
    // we want obstacle-information based on Sonar-values
    // as long as this is in progress, we wait
    if( this.robotAPI.isSweeping() ) { return false; }

    // if we already know this sweep and we're not sweeping, we need to start
    // sweeping.
    if( this.robotAPI.getSweepID() <= this.sweepID ) {
      this.robotAPI.sweep(this.angles);
      return false;
    }

    // new sweep is ready ...
    return true;
  }

  private void manageMemory() {
    /*System.gc(); System.gc(); System.gc(); System.gc();

    Runtime       runtime = Runtime.getRuntime();
    NumberFormat  format  = NumberFormat.getInstance();
    StringBuilder sb      = new StringBuilder();
    long maxMemory        = runtime.maxMemory(),
         allocatedMemory  = runtime.totalMemory(),
         freeMemory       = runtime.freeMemory();

    sb.append("memory free      : " + format.format(freeMemory / 1024) + "\n");
    sb.append("       allocated : " + format.format(allocatedMemory / 1024) + "\n");
    sb.append("       max       : " + format.format(maxMemory / 1024) + "\n");
    sb.append("       total free: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "\n");
    /**/
    //System.out.println(sb);
  }
  
  // ExternalEventHandler
  
  public void handleActivation() {
    GridModelPart.from(this.model).getMyAgent().activate();
  }

  public void handleSectorInfo(String agentName, Point position, boolean knowsN, boolean n, boolean knowsE, boolean e, boolean knowsS, boolean s, boolean knowsW, boolean w) {
    GridModelPart gridModel = GridModelPart.from(this.model);
    Grid g = gridModel.getGridOf(agentName);
    Sector sec = g.getSectorAt(position);
    if(sec == null){
      sec = new LinkedSector();
      g.add(sec, position);
    }
    
    if(knowsN){
      if(n){ sec.setWall(Bearing.N); } else{ sec.setNoWall(Bearing.N); }
    }
    if(knowsE){
      if(e){ sec.setWall(Bearing.E); } else{ sec.setNoWall(Bearing.E); }
    }
    if(knowsS){
      if(s){ sec.setWall(Bearing.S); } else{ sec.setNoWall(Bearing.S); }
    }
    if(knowsW){
      if(w){ sec.setWall(Bearing.W); } else{ sec.setNoWall(Bearing.W); }
    }
  }

  public void handleNewAgent(String agentName) {
    GridModelPart gridModel = GridModelPart.from(this.model);
    Grid g = gridModel.getGridOf(agentName);
    Agent agent = new GhostAgent(agentName);
    g.add(agent, new Point(0, 0), Bearing.N);
    
  }

  public void handleAgentInfo(String agentName, Point position, int value, Bearing bearing) {
    GridModelPart gridModel = GridModelPart.from(this.model);
    Grid g = gridModel.getGridOf(agentName);
    Sector sec = g.getSectorAt(position);
    if(sec == null){
      sec = new LinkedSector();
      g.add(sec, position);
    }
    if(value == 0){
      Agent agent = g.getAgent(agentName);
      g.moveTo(agent, position, bearing);
    } else{
      //Utils.Log("Barcode found: "+value+", "+bearing);
      BarcodeAgent barcode = BarcodeAgent.getBarcodeAgent(value);
      g.add(barcode, position, bearing);
    }
  }

  public void handleTargetInfo(String agentName, Point position) {
    GridModelPart gridPart = GridModelPart.from(this.model);
    gridPart.setPacman(gridPart.getGridOf(agentName), position);
  }

  public void handleSendGridInformation() {}
  public void handleCaptured(String agentName) {}
  public void handleRemoveAgent(String agentName) {}
}