package penoplatinum.pacman;

/**
 * GhostModel
 * 
 * Implementation of Model, providing all needed by a typical Ghost
 * 
 * @author: Team Platinum
 */
import java.util.List;
import java.util.ArrayList;

import penoplatinum.SimpleHashMap;
import penoplatinum.Utils;
import penoplatinum.simulator.Line;
import penoplatinum.modelprocessor.Buffer;

// we're using commons collections HashedMap because HashMap isn't implemented
// on Lejos.

import penoplatinum.modelprocessor.ModelProcessor;
import penoplatinum.grid.Sector;
import penoplatinum.grid.Agent;
import penoplatinum.grid.Grid;
import penoplatinum.grid.AggregatedGrid;
import penoplatinum.grid.GridView;
import penoplatinum.grid.DiffusionGridProcessor;

import penoplatinum.grid.SimpleGrid;
//import penoplatinum.grid.SwingGridView;
import penoplatinum.modelprocessor.LightColor;
import penoplatinum.simulator.Model;

import penoplatinum.simulator.mini.Bearing;
import penoplatinum.simulator.mini.MessageHandler;
import penoplatinum.simulator.mini.Queue;

public class GhostModel implements Model {
  // little bit of configuration

  private List<Integer> sonarValues = new ArrayList<Integer>();
  private boolean newSonarValues = false;
  // two queue-like lists for in- and out-goinging messages
  private List<String> inbox = new ArrayList<String>();
  private List<String> outbox = new ArrayList<String>();
  // ModelProcessors are a chain of Decorators
  private ModelProcessor processor;
  // Data specific to the GhostNavigator
  // the agent on the grid
  private Agent agent;
  // sector representing the current/prev current-sector
  private Sector currentSector = new Sector(); // THIS IS NOT a reference to the Grid
  private Sector prevSector = new Sector(); // THIS IS NOT a reference to the Grid
  // This is our grid
  private Grid myGrid;
  // This is a map of the other robots. It consists of a key representing the
  // name of the other robot/ghost and a value that contains a Grid.
  private SimpleHashMap<String, Grid> otherGrids = new SimpleHashMap<String, Grid>();
  // we keep track of the last movement
  private int lastMovement = GhostAction.NONE;
  private boolean isSweepDataChanged;
  private GhostProtocolHandler protocol;
  private ArrayList<Sector> barcodeSectors = new ArrayList<Sector>();
  
  public GhostModel(String name) {
    this.agent = new GhostAgent(name);
    this.setupGrid();
    
    protocol = new GhostProtocolHandler(agent, this);
    final Queue queue = new Queue();
    queue.subscribe(new MessageHandler() {
      
      @Override
      public void useQueue(Queue queue) {
      }
      
      @Override
      public void receive(String msg) {
        outbox.add(msg);
      }
    });
    protocol.useQueue(queue);
    
  }
  
  private void log(String msg) {
    if (0 == 0) {
      return;
    }
//    System.out.printf("[%10s] %2d,%2d / Model  : %s\n",
//            this.getAgent().getName(),
//            this.getAgent().getLeft(),
//            this.getAgent().getTop(),
//            msg);
  }

  // we create a new Grid, add the first sector, the starting point
  private void setupGrid() {
    this.myGrid = new AggregatedGrid().setProcessor(new DiffusionGridProcessor()).addSector(new Sector().setCoordinates(0, 0).put(this.agent, Bearing.N));
  }

  // when running in a UI environment we can provide a View for the Grid
  public GhostModel displayGridOn(GridView view) {
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
    
    if (values.length != SENSORVALUES_NUM) {
      throw new RuntimeException("Invalid number of sensorvalues given!");
    }

    //this.prevSensors = this.sensors.clone(); //TODO: WARNING GC
    this.sensors = values;
    this.process();
    
    return this;
    
    
  }

  // triggers the processor(s) to start processing the sensordata and update
  // the grid
  private void process() {
    
    setScanningLightData(false); // Resets this flag to false
    sweepComplete = false;
    if (this.processor != null) {
      this.processor.process();
    }
    isSweepDataChanged = false;
    
  }
  
  public void updateSonarValues(List<Integer> distances, List<Integer> angles) {
    this.distances = distances; //TODO: remove double
    this.sonarValues = distances;
    this.angles = angles;
    
    this.newSonarValues = true;
    isSweepDataChanged = true;
    
    this.process();
  }

  /**
   * This is thread safe
   * @param msg 
   */
  public void addIncomingMessage(String msg) {
    synchronized (this) {
      this.inbox.add(msg);
    }
  }

  /**
   * @param buffer 
   */
  public void receiveIncomingMessages(List<String> buffer) {
    synchronized (this) {
      buffer.addAll(inbox);
      inbox.clear();
    }
  }
  
  public List<String> getOutgoingMessages() {
    return this.outbox;
  }
  
  public void processMessage(String msg) {
    protocol.receive(msg);
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
    this.prevSector = this.currentSector;
    this.currentSector = newSector;
    needsGridUpdate = true;
    
    
  }
  private boolean needsGridUpdate;
  
  public boolean needsGridUpdate() {
    return needsGridUpdate;
  }
  
  public void markGridUpdated() {
    needsGridUpdate = false;
  }
  
  public void markSectorUpdated(Sector current) {
    protocol.sendDiscover(current);
  }
  
  public Sector getDetectedSector() {
    return this.currentSector;
  }
  
  public Sector getCurrentSector() {
    return this.agent.getSector();
  }
  
  public int getLeftFreeDistance() {
    return getWallLeftDistance();
  }
  
  public int getFrontFreeDistance() {
    return getWallFrontDistance();
  }
  
  public int getRightFreeDistance() {
    return getWallRightDistance();
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
    return "Agent @ " + this.agent.getLeft() + "," + this.agent.getTop() + "\n"
            + "AgentBearing: " + this.agent.getBearing() + "\n"
            + "Detected Sector:\n"
            + "  N: " + (n == null ? "?" : (n ? "yes" : "")) + "\n"
            + "  E: " + (e == null ? "?" : (e ? "yes" : "")) + "\n"
            + "  S: " + (s == null ? "?" : (s ? "yes" : "")) + "\n"
            + "  W: " + (w == null ? "?" : (w ? "yes" : "")) + "\n"
            + "Free Left : " + this.getLeftFreeDistance() + "\n"
            + "     Front: " + this.getFrontFreeDistance() + "\n"
            + "     Right: " + this.getRightFreeDistance() + "\n";
  }
  
  public void moveForward() {
    this.agent.moveForward();
    this.lastMovement = GhostAction.FORWARD;
    protocol.sendPosition();
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
//    this.log("inspecting last movement: " + this.lastMovement);
    return this.lastMovement;
  }
  
  public Grid getGrid(String actorName) {
    Grid get = otherGrids.get(actorName);
    if (get == null) {
      get = new SimpleGrid();
      otherGrids.put(actorName, get);

      //SwingGridView view = new SwingGridView();
      //get.displayOn(view);

    }
    return get;
  }
  
  public void printGridStats() {
    
    for (int i = 0; i < otherGrids.values.size(); i++) {
      Grid g = otherGrids.values.get(i);
      
      Utils.Log("Grid " + i + ": " + g.getSectors().size());
      
    }
  }
  
  public ArrayList<Sector> getBarcodeSectors() {
    return barcodeSectors;
  }
  private float positionX;
  private float positionY;
  private float direction;
  /**
   * the raw data of the sensors: three motors, sensors 1, 2, 3, 4 
   * and the states of the three motors defined by the MOTORSTATE enumeration
   */
  private int[] sensors = new int[SENSORVALUES_NUM];
  // processors are chained using a Decorator pattern
  private List<Integer> distances = new ArrayList<Integer>();
  private List<Integer> angles = new ArrayList<Integer>();
  private int[] sweepValues = new int[4];
  private boolean sweepChanged = true;
  private int barcode = -1;
  private Line line = Line.NONE;
  private int bufferSize = 1000;
  private Buffer lightValueBuffer = new Buffer(bufferSize);
  /**
   * This value is true on the step that the sweep was completed
   */
  private Boolean sweepComplete;
  private float averageLightValue;
  private float averageWhiteValue;
  private float averageBlackValue;

  // method to update a set of distances and angles
  public void updateDistances(List<Integer> distances,
          List<Integer> angles) {
    this.distances = distances;
    this.angles = angles;
  }
  
  public List<Integer> getDistances() {
    return this.distances;
  }
  
  public List<Integer> getAngles() {
    return this.angles;
  }

  /**
   * accessors to give access to the sensor and map data. these are mainly
   * used by the ModelProcessors.
   */
  public int getSensorValue(int num) {
    return this.sensors[num];
  }
  
  public int getLightSensorValue() {
    return this.sensors[Model.S4];
  }
  
  public Boolean isMoving() {
    return sensors[Model.MS1] != MOTORSTATE_STOPPED || sensors[Model.MS2] != MOTORSTATE_STOPPED;
//    return this.sensors[Model.M1] != this.prevSensors[Model.M1] &&
//           this.sensors[Model.M2] != this.prevSensors[Model.M2];
  }
  
  public Boolean isTurning() {
    return isMoving() && (sensors[Model.MS1] != sensors[Model.MS2]);
//    int changeLeft  = this.sensors[Model.M1] - this.prevSensors[Model.M1];
//    int changeRight = this.sensors[Model.M2] - this.prevSensors[Model.M2];
//    return changeLeft != 0 && changeLeft == changeRight * -1;
  }
  
  public boolean isSweepDataChanged() {
    return isSweepDataChanged; //TODO: does this work???
  }

  // indicates whether the sweep-values have changed since the last time
  // they are consulted
  public boolean hasUpdatedSonarValues() {
    return this.sweepChanged;
  }

  // TODO: refactor this to more function name about extrema
  //       or separate methods to get extrema
  public int[] getSonarValues() {
    this.sweepChanged = false;
    return this.sweepValues.clone(); //TODO: WARNING GC
  }
  
  public int getBarcode() {
    return this.barcode;
  }
  private int lastBarcode;
  
  public void setBarcode(int barcode) {
    if (barcode != -1) {
      lastBarcode = barcode;
      
      barcodeSectors.add(getAgent().getSector());
      getAgent().getSector().setTagCode(barcode);
      getAgent().getSector().setTagBearing(getAgent().getBearing());
      
      protocol.sendBarcode(barcode, getAgent().getBearing());
    }
    this.barcode = barcode;
    
  }
  
  public Buffer getLightValueBuffer() {
    return this.lightValueBuffer;
  }
  
  public Line getLine() {
    return line;
  }
  
  public void setLine(Line line) {
    this.line = line;
  }

  /**
   * Returns the average tacho count of the 2 motors
   */
  public float getAverageTacho() {
    return (getSensorValue(M1) + getSensorValue(M2)) / 2f;
  }
  private StringBuilder builder = new StringBuilder();
  
  public String toString() {
    int lightValue = this.getSensorValue(S4);
    
    String interpretedColor = "VIOLET!!"; //interpreter.getCurrentColor().toString();
    int sonarAngle = this.getSensorValue(M3) + 90;
    int sonarDistance = getSensorValue(S3);
    boolean pushLeft = this.getSensorValue(S1) == 255;
    boolean pushRight = this.getSensorValue(S2) == 255;
    
    builder.delete(0, builder.length());
    builder.append(lightValue).append(",\"").append(interpretedColor.toLowerCase()).append("\",\"").append(lastBarcode).append("\",").append(sonarAngle).append(',').append(sonarDistance).append(',').append(pushLeft).append(',').append(pushRight);
    return builder.toString();
  }
  private boolean gapFound;
  private int gapStartAngle;
  private int gapEndAngle;
  
  public int getGapEndAngle() {
    return gapEndAngle;
  }
  
  public void setGapEndAngle(int gapEndAngle) {
    this.gapEndAngle = gapEndAngle;
  }
  
  public boolean isGapFound() {
    return gapFound;
  }
  
  public void setGapFound(boolean gapFound) {
    this.gapFound = gapFound;
  }
  
  public int getGapStartAngle() {
    return gapStartAngle;
  }
  
  public void setGapStartAngle(int gapStartAngle) {
    this.gapStartAngle = gapStartAngle;
  }

  /**
   * @return the positionX
   */
  public float getPositionX() {
    return positionX;
  }

  /**
   * @param positionX the positionX to set
   */
  public void setPositionX(float positionX) {
    this.positionX = positionX;
  }

  /**
   * @return the positionY
   */
  public float getPositionY() {
    return positionY;
  }

  /**
   * @param positionY the positionY to set
   */
  public void setPositionY(float positionY) {
    this.positionY = positionY;
  }
  
  public float getDirection() {
    return direction;
  }
  
  public void setDirection(float direction) {
    this.direction = direction;
  }

  /**
   * Returns true when the sweep was completed this step
   * @return 
   */
  public Boolean isSweepComplete() {
    return sweepComplete;
  }
  private boolean wallLeft;
  private boolean wallFront;
  private boolean wallRight;
  private int wallLeftDistance;
  private int wallFrontDistance;
  private int wallRightDistance;
  private int wallLeftClosestAngle;
  private int wallRightClosestAngle;
  
  public boolean isWallFront() {
    return wallFront;
  }
  
  public void setWallFront(boolean wallFront) {
    this.wallFront = wallFront;
  }
  
  public boolean isWallLeft() {
    return wallLeft;
  }
  
  public void setWallLeft(boolean wallLeft) {
    this.wallLeft = wallLeft;
  }
  
  public boolean isWallRight() {
    return wallRight;
  }
  
  public void setWallRight(boolean wallRight) {
    this.wallRight = wallRight;
  }
  
  public int getWallFrontDistance() {
    return wallFrontDistance;
  }
  
  public void setWallFrontDistance(int wallFrontDistance) {
    this.wallFrontDistance = wallFrontDistance;
  }
  
  public int getWallLeftDistance() {
    return wallLeftDistance;
  }
  
  public void setWallLeftDistance(int wallLeftDistance) {
    this.wallLeftDistance = wallLeftDistance;
  }
  
  public int getWallRightDistance() {
    return wallRightDistance;
  }
  
  public void setWallRightDistance(int wallRightDistance) {
    this.wallRightDistance = wallRightDistance;
  }
  
  public int getWallLeftClosestAngle() {
    return wallLeftClosestAngle;
  }
  
  public void setWallLeftClosestAngle(int wallLeftClosestAngle) {
    this.wallLeftClosestAngle = wallLeftClosestAngle;
  }
  
  public int getWallRightClosestAngle() {
    return wallRightClosestAngle;
  }
  
  public void setWallRightClosestAngle(int wallRightClosestAngle) {
    this.wallRightClosestAngle = wallRightClosestAngle;
  }
  private double totalTurnedAngle;
  
  public double getTotalTurnedAngle() {
    return totalTurnedAngle;
  }
  
  public void setTotalTurnedAngle(double totalTurnedAngle) {
    this.totalTurnedAngle = totalTurnedAngle;
  }
  
  private void setScanningLightData(boolean b) {
  }
  
  public float getAverageBlackValue() {
    return averageBlackValue;
  }
  
  public void setAverageBlackValue(float averageBlackValue) {
    this.averageBlackValue = averageBlackValue;
  }
  
  public float getAverageLightValue() {
    return averageLightValue;
  }
  
  public void setAverageLightValue(float averageLightValue) {
    this.averageLightValue = averageLightValue;
  }
  
  public float getAverageWhiteValue() {
    return averageWhiteValue;
  }
  
  public void setAverageWhiteValue(float averageWhiteValue) {
    this.averageWhiteValue = averageWhiteValue;
  }
  private LightColor currentLightColor = LightColor.Brown;
  
  public LightColor getCurrentLightColor() {
    if (0 == 0) {
      return currentLightColor;
    }
    float blackBorder = (averageLightValue + averageBlackValue) * 0.5f;
    float whiteBorder = (averageLightValue + averageWhiteValue) * 0.5f;
    if (getLightSensorValue() < blackBorder) {
      return LightColor.Black;
    }
    if (getLightSensorValue() > whiteBorder) {
      return LightColor.White;
    }
    
    return LightColor.Brown;
  }
  
  public void setCurrentLightColor(LightColor value) {
    currentLightColor = value;
  }
  boolean isReadingBarcode = false;
  
  @Override
  public void setReadingBarcode(boolean b) {
    this.isReadingBarcode = b;
  }
  
  @Override
  public boolean isReadingBarcode() {
    return this.isReadingBarcode;
  }
}
