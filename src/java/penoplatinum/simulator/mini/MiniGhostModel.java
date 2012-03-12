package penoplatinum.simulator.mini;

/**
 * GhostModel
 * 
 * Implementation of Model, providing all needed by a typical Ghost
 * 
 * @author: Team Platinum
 */

import java.util.List;
import java.util.ArrayList;

import penoplatinum.simulator.Line;

import penoplatinum.modelprocessor.Buffer;
import penoplatinum.modelprocessor.LightColor;

import penoplatinum.pacman.GhostAction;
import penoplatinum.pacman.GhostAgent;


// we're using commons collections HashedMap because HashMap isn't implemented
// on Lejos.
import org.apache.commons.collections.map.HashedMap;

import penoplatinum.modelprocessor.ModelProcessor;
import penoplatinum.grid.Sector;
import penoplatinum.grid.Agent;
import penoplatinum.grid.Grid;
import penoplatinum.grid.AggregatedGrid;
import penoplatinum.grid.GridView;
import penoplatinum.grid.DiffusionGridProcessor;

import penoplatinum.simulator.Model;
import penoplatinum.simulator.RobotAgent;

import penoplatinum.simulator.Bearing;

public class MiniGhostModel implements Model {
  // little bit of configuration
  private static final int SENSORVALUES_COUNT = 4; // TODO

  private List<Integer> sonarValues = new ArrayList<Integer>();
  private boolean newSonarValues = false;

  // two queue-like lists for in- and out-goinging messages
  private List<String> inbox  = new ArrayList<String>();
  private List<String> outbox = new ArrayList<String>();

  // ModelProcessors are a chain of Decorators
  private ModelProcessor processor;
  
  // Data specific to the GhostNavigator
  
  // the agent on the grid
  private Agent agent;
  // sector representing the current/prev current-sector
  private Sector currentSector = new Sector(); // THIS IS NOT a reference to the Grid
  private Sector prevSector    = new Sector(); // THIS IS NOT a reference to the Grid
  
  // This is our grid
  private Grid myGrid;
  // This is a map of the other robots. It consists of a key representing the
  // name of the other robot/ghost and a value that contains a Grid.
  private HashedMap otherGrids = new HashedMap();
  
  // we keep track of the last movement
  private int lastMovement = GhostAction.NONE;

  public MiniGhostModel(String name) {
    this.agent = new GhostAgent(name);
    this.setupGrid();
  }
  
  private void log(String msg) {
    System.out.printf( "[%10s] %2d,%2d / Model  : %s\n", 
                       this.getAgent().getName(),
                       this.getAgent().getLeft(),
                       this.getAgent().getTop(),
                       msg );
  }

  
  // we create a new Grid, add the first sector, the starting point
  private void setupGrid() {
    this.myGrid = new AggregatedGrid()
                    .setProcessor(new DiffusionGridProcessor())
                    .addSector(new Sector()
                                .setCoordinates(0,0)
                                .put(this.agent, Bearing.N));
  }
  
  // when running in a UI environment we can provide a View for the Grid
  public Model displayGridOn(GridView view) {
    this.myGrid.displayOn(view);
    return this;
  }
  
  public Grid getGrid() {
    return this.myGrid;
  }
  
  public Model setProcessor(ModelProcessor processor) {
    this.processor = processor;
    this.processor.setModel(this);
    return this;
  }

  // receive an update of the sensor values
  public Model updateSensorValues(int[] values) {
    // nothing required here for Navigator
    this.process();
    return this;
  }
  
  // triggers the processor(s) to start processing the sensordata and update
  // the grid
  private void process() {
    if( this.processor != null ) { this.processor.process(); }
  }

  public void updateSonarValues(List<Integer> distances, List<Integer> angles) {
    this.updateSonarValues(distances);
  }

  public void updateSonarValues(List<Integer> distances) {
    this.sonarValues = distances;
    this.newSonarValues = true;

    this.process();
  }

  public void addIncomingMessage(String msg) {
    this.inbox.add(msg);
  }
  
  public List<String> getIncomingMessages() {
    return this.inbox;
  }

  public List<String> getOutgoingMessages() {
    return this.outbox;
  }
  
  public void clearInbox() {
    this.inbox.clear();
  }

  public void clearOutbox() {
    this.outbox.clear();
  }
  
  public Agent getAgent() {
    return this.agent;
  }
  
  public boolean hasNewSonarValues() {
    return this.newSonarValues;
  }
  
  public void markSonarValuesProcessed() {
    this.newSonarValues = false;
  }
  
  public void updateSector(Sector newSector) {
    this.prevSector    = this.currentSector;
    this.currentSector = newSector;
  }
  
  public Sector getDetectedSector() {
    return this.currentSector;
  }

  public Sector getCurrentSector() {
    return this.agent.getSector();
  }
  
  public int getLeftFreeDistance() {
    return this.sonarValues.get(0);
  }

  public int getFrontFreeDistance() {
    return this.sonarValues.get(1);
  }

  public int getRightFreeDistance() {
    return this.sonarValues.get(2);
  }

  // Future Use: detect changes (e.g. keep track of change ratio)
  public boolean sectorHasChanged() {
    // TODO: make this more intelligent, going from unknown to known is not a
    //       negative change, but going from known/wall to known/nowall is a 
    //       bad sign
    return this.prevSector.getWalls() != this.currentSector.getWalls();
  }
  
  public String explain() {
    Boolean n = this.currentSector.hasWall(Bearing.N);
    Boolean e = this.currentSector.hasWall(Bearing.E);
    Boolean s = this.currentSector.hasWall(Bearing.S);
    Boolean w = this.currentSector.hasWall(Bearing.W);
    return "Agent @ " + this.agent.getLeft() + "," + this.agent.getTop() + "\n" + 
           "AgentBearing: " + this.agent.getBearing() + "\n" +
           "Detected Sector:\n" +
           "  N: " + ( n == null ? "?" : ( n ? "yes" : "" )) + "\n" + 
           "  E: " + ( e == null ? "?" : ( e ? "yes" : "" )) + "\n" + 
           "  S: " + ( s == null ? "?" : ( s ? "yes" : "" )) + "\n" + 
           "  W: " + ( w == null ? "?" : ( w ? "yes" : "" )) + "\n" + 
           "Free Left : " + this.getLeftFreeDistance() + "\n" + 
           "     Front: " + this.getFrontFreeDistance() + "\n" + 
           "     Right: " + this.getRightFreeDistance() + "\n";
  }
  
  public void moveForward() {
    this.agent.moveForward();
    this.lastMovement = GhostAction.FORWARD;
  }
  
  public void turnLeft() {
    this.agent.turnLeft();
    this.lastMovement = GhostAction.TURN_LEFT;
  }
  
  public void turnRight() {
    this.agent.turnRight();
    this.lastMovement = GhostAction.TURN_RIGHT;
  }
  
  public void clearLastMovement() {
    this.lastMovement = GhostAction.NONE;
  }
  
  public int getLastMovement() {
    this.log("inspecting last movement: " + this.lastMovement);
    return this.lastMovement;
  }
  
  public boolean isIsNextToPacman() {
    return false;
  }

  public void setPacManInNext(boolean b, int x, int y) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean isReadingBarcode() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setReadingBarcode(boolean b) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public LightColor getCurrentLightColor() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  public void receiveIncomingMessages(List<String> buffer) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  // TODO: move this ;-)
  public void updateDistances(List<Integer> distances, List<Integer> angles){}
  public List<Integer> getDistances() { return null; }
  public List<Integer> getAngles() { return null; }
  public int getSensorValue(int num) { return 0; }
  public void markStuck() {}
  public void markStuck(boolean left, boolean right) {}
  public void markNotStuck() {}
  public void markStuckLeft() {}
  public void markStuckRight() {}
  public Boolean isStuck() { return false; }
  public Boolean isStuckLeft() { return false; }
  public Boolean isStuckRight() { return false; }
  public Boolean isMoving() { return false; }
  public Boolean isTurning() { return false; }
  public void setNewSweep(int min, int minA, int max, int maxA) {}
  public boolean isSweepDataChanged() { return false; }
  public boolean hasUpdatedSonarValues() { return false; }
  public int[] getSonarValues() { return new int[] {}; }
  public int getBarcode() { return 0; }
  public void setBarcode(int barcode) {}
  public Buffer getLightValueBuffer() { return null; }
  public Line getLine() { return null; }
  public void setLine(Line line) {}
  public void setBarcodeAngle(double angle) {}
  public double getBarcodeAngle() { return 0; }
  public float getAverageTacho() { return 0; }
  public boolean isLightDataCorrupt() { return false; }
  public void setLightCorruption(boolean lightCorruption) {}
  public String toString() { return ""; }
  public boolean isLeftObstacle() { return false; }
  public void setLeftObstacle(boolean leftObstacle) {}
  public boolean isRightObstacle() { return false; }
  public void setRightObstacle(boolean rightObstacle) {}
  public int getGapEndAngle() { return 0; }
  public void setGapEndAngle(int gapEndAngle) {}
  public boolean isGapFound() { return false; }
  public void setGapFound(boolean gapFound) {}
  public int getGapStartAngle() { return 0; }
  public void setGapStartAngle(int gapStartAngle) {}
  public boolean isScanningLightData() { return false; }
  public void setScanningLightData(boolean scanningLightData) {}
  public float getPositionX() { return 0; }
  public void setPositionX(float positionX) {}
  public float getPositionY() { return 0; }
  public void setPositionY(float positionY) {}
  public float getDirection() { return 0; }
  public void setDirection(float direction) {}
  public Boolean isSweepComplete() { return false; }
  public boolean isWallFront() { return false; }
  public void setWallFront(boolean wallFront) {}
  public boolean isWallLeft() { return false; }
  public void setWallLeft(boolean wallLeft) {}
  public boolean isWallRight() { return false; }
  public void setWallRight(boolean wallRight) {}
  public int getWallFrontDistance() { return 0; }
  public void setWallFrontDistance(int wallFrontDistance) {}
  public int getWallLeftDistance() { return 0; }
  public void setWallLeftDistance(int wallLeftDistance) {}
  public int getWallRightDistance() { return 0; }
  public void setWallRightDistance(int wallRightDistance) {}
  public int getWallLeftClosestAngle() { return 0; }
  public void setWallLeftClosestAngle(int wallLeftClosestAngle) {}
  public int getWallRightClosestAngle() { return 0; }
  public void setWallRightClosestAngle(int wallRightClosestAngle) {}
  public double getTotalTurnedAngle()  { return 0; }
  public void setTotalTurnedAngle(double totalTurnedAngle) {}
}
