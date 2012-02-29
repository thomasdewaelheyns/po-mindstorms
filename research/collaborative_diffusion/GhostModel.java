public class GhostModel {
  // little bit of configuration
  private static final int SENSORVALUES_COUNT = 4; // TODO

  /**
   * the raw data of the sensors: ... TODO: required stuff for Driver
   */
  private int[] sensors     = new int[GhostModel.SENSORVALUES_COUNT];
  private int[] prevSensors = new int[GhostModel.SENSORVALUES_COUNT];

  private int[] sonarValues;

  // two queue-like lists for in- and out-goinging messages
  private List<String> inbox  = new ArrayList<String>();
  private List<String> outbox = new ArrayList<String>();

  // ModelProcessors are a chain of Decorators
  private ModelProcessor processor;
  
  // Data specific to the GhostNavigator
  
  // the agent on the grid
  Agent agent;
  
  // This is our grid
  Grid myGrid;
  // This is a map of the other robots. It consists of a key representing the
  // name of the other robot/ghost and a value that contains a Grid.
  // This Grid can be a real Grid, or a GridTranslatingProxy that directly
  // applies the incoming changes to our own Grid. This is possible, after the
  // other robot/ghost has crossed a common reference point/barcode.
  HashedMap otherGrids = new HashedMap();

  public GhostModel(String name) {
    this.agent = new DiscoveryAgent(name);
    this.setupGrid();
  }
  
  // we create a new Grid, add the first sector, the starting point
  private void setupGrid() {
    this.grid = new Grid()
                  .addSector(new Sector()
                              .setCoordinates(0,0)
                              .putAgent(this.agent, Bearing.N));
  }
  
  // when running in a UI environment we can provide a View for the Grid
  public GhostModel displayGridOn(GridView view) {
    this.grid.displayOn(view).show();
    return this;
  }
  
  // receive an update of the sensor values
  public void updateSensorValues(int[] values) {
    // TODO: add stuff required for the Driver
    this.process();
  }

  public void updateSonarValues(int[] values) {
    this.sonarValues = values;
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
}