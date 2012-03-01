import java.util.List;
import java.util.ArrayList;

// we're using commons collections HashedMap because HashMap isn't implemented
// on Lejos.
import org.apache.commons.collections.map.HashedMap;


public class GhostModel implements Model {
  // little bit of configuration
  private static final int SENSORVALUES_COUNT = 4; // TODO

  private int[] sonarValues;
  private boolean newSonarValues = false;

  // two queue-like lists for in- and out-goinging messages
  private List<String> inbox  = new ArrayList<String>();
  private List<String> outbox = new ArrayList<String>();

  // ModelProcessors are a chain of Decorators
  private ModelProcessor processor;
  
  // Data specific to the GhostNavigator
  
  // the agent on the grid
  private Agent agent;
  // its position
  private int left, top, bearing;
  // sector representing the current/prev current-sector
  private Sector currentSector = new Sector(); // THIS IS NOT a reference to the Grid
  private Sector prevSector    = new Sector(); // THIS IS NOT a reference to the Grid
  
  // This is our grid
  Grid myGrid;
  // This is a map of the other robots. It consists of a key representing the
  // name of the other robot/ghost and a value that contains a Grid.
  // This Grid can be a real Grid, or a GridTranslatingProxy that directly
  // applies the incoming changes to our own Grid. This is possible, after the
  // other robot/ghost has crossed a common reference point/barcode.
  HashedMap otherGrids = new HashedMap();

  public GhostModel(String name) {
    this.agent = new GhostAgent(name);
    this.setupGrid();
  }
  
  // we create a new Grid, add the first sector, the starting point
  private void setupGrid() {
    this.myGrid = new Grid()
                    .addSector(new Sector()
                                .setCoordinates(0,0)
                                .putAgent(this.agent, Bearing.N));
  }
  
  // when running in a UI environment we can provide a View for the Grid
  public GhostModel displayGridOn(GridView view) {
    this.myGrid.displayOn(view).show();
    return this;
  }
  
  public Model setProcessor(ModelProcessor processor) {
    this.processor = processor;
    this.processor.setModel(this);
    return this;
  }

  // receive an update of the sensor values
  public void updateSensorValues(int[] values) {
    this.left    = values[0];
    this.top     = values[1];
    this.bearing = values[2];

    this.process();
  }
  
  // triggers the processor(s) to start processing the sensordata and update
  // the grid
  private void process() {
    if( this.processor != null ) { this.processor.process(); }
  }

  public void updateSonarValues(int[] values) {
    this.sonarValues = values;
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
  
  public int getLeft() {
    return this.left;
  }

  public int getTop() {
    return this.top;
  }

  public int getBearing() {
    return this.bearing;
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
    return this.sonarValues[0];
  }

  public int getFrontFreeDistance() {
    return this.sonarValues[1];
  }

  public int getRightFreeDistance() {
    return this.sonarValues[2];
  }

  // Future Use: detect changes (e.g. keep track of change ratio)
  public boolean sectorHasChanged() {
    // TODO: make this more intelligent, going from unknown to known is not a
    //       negative change, but going from known/wall to known/nowall is a 
    //       bad sign
    return this.prevSector.getWalls() != this.currentSector.getWalls();
  }
  
  public String explain() {
    return "@ " + this.left + "," + this.top + " / Agent @ " + this.agent.getLeft() + "," + this.agent.getTop() + "\n" + 
           "Bearing: " + this.bearing + " / AgentBearing: " + this.agent.getBearing() + "\n" +
           "Detected Sector:\n" +
           "  N: " + ( this.currentSector.hasWall(Bearing.N) ? "yes" : "" ) + "\n" + 
           "  E: " + ( this.currentSector.hasWall(Bearing.E) ? "yes" : "" ) + "\n" + 
           "  S: " + ( this.currentSector.hasWall(Bearing.S) ? "yes" : "" ) + "\n" + 
           "  W: " + ( this.currentSector.hasWall(Bearing.W) ? "yes" : "" ) + "\n" +
           "Free Left : " + this.getLeftFreeDistance() + "\n" + 
           "     Front: " + this.getFrontFreeDistance() + "\n" + 
           "     Right: " + this.getRightFreeDistance() + "\n";
  }
}
