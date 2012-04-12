package penoplatinum.model.part;

/**
 * Holds this robots grid information, and the grid information of the other 
 * robots
 * 
 * @author Team Platinum
 */

import java.util.List;
import java.util.ArrayList;

import penoplatinum.model.Model;

import penoplatinum.grid.Grid;
import penoplatinum.grid.Agent;
import penoplatinum.grid.Sector;


public class GridModelPart implements ModelPart {
  // boilerplate implementation required to register and retrieve a ModelPart
  // from the model
  public static GridModelPart from(Model model) {
    return (GridModelPart)model.getPart(ModelPartRegistry.GRID_MODEL_PART);
  }

  // private AggregatedGrid myGrid;
  private Grid myGrid;
  private Agent myAgent;
  private ArrayList<Sector> changedSectors = new ArrayList<Sector>();


  public GridModelPart(String name) {
    // this.myAgent = new GhostAgent(name);
    // this.setupGrid();
  }

  public Grid getMyGrid() {
    return this.myGrid;
  }

  public Agent getMyAgent() {
    return this.myAgent;
  }
  
  public void refreshMyGrid() {
    if( ! this.hasChangedSectors()) { return; }
    for(int i=0; i<10; i++) {
      // TODO
      // this.myGrid.refresh();
    }
  }
  
  public boolean hasChangedSectors() {
    return changedSectors.size() != 0;
  }

  /*
  // This is our grid
  
  // Data specific to the GhostNavigator
  // the agent on the grid
  private Agent agent;

  // we create a new Grid, add the first sector, the starting point
  private void setupGrid() {
    this.myGrid = new AggregatedGrid();
    this.myGrid.setProcessor(new DiffusionGridProcessor()).addSector(new Sector().setCoordinates(0, 0).put(this.agent, Bearing.N));
  }

  // when running in a UI environment we can provide a View for the Grid
  public void displayGridOn(GridView view) {
    this.myGrid.displayOn(view);
  }


  
  public AggregatedSubGrid getGrid(String actorName) {
    return getGrid().getGhostGrid(actorName);
  }
  
  public void markSectorChanged(Sector current) {
    // TODO: potential fps eater, may be better to just cache everything in the
    //       list and filter doubles later on
    for (int i = 0; i < changedSectors.size(); i++) {
      if (changedSectors.get(i) == current) {
        return;
      }
    }
    changedSectors.add(current);


  }

  public ArrayList<Sector> getChangedSectors() {
    return changedSectors;
  }


//  public void printGridStats() {
//
//    for (int i = 0; i < otherGrids.values.size(); i++) {
//      Grid g = otherGrids.values.get(i);
//
//      Utils.Log("Grid " + i + ": " + g.getSectors().size());
//
//    }
//  }

  public Agent getAgent() {
    return this.agent;
  }

  public Sector getCurrentSector() {
    return this.agent.getSector();
  }

  public String explain() {
    Boolean n = this.getCurrentSector().hasWall(Bearing.N);
    Boolean e = this.getCurrentSector().hasWall(Bearing.E);
    Boolean s = this.getCurrentSector().hasWall(Bearing.S);
    Boolean w = this.getCurrentSector().hasWall(Bearing.W);
    return "Agent @ " + this.agent.getLeft() + "," + this.agent.getTop() + "\n"
            + "AgentBearing: " + this.agent.getBearing() + "\n"
            + "Detected Sector:\n"
            + "  N: " + (n == null ? "?" : (n ? "yes" : "")) + "\n"
            + "  E: " + (e == null ? "?" : (e ? "yes" : "")) + "\n"
            + "  S: " + (s == null ? "?" : (s ? "yes" : "")) + "\n"
            + "  W: " + (w == null ? "?" : (w ? "yes" : "")) + "\n";
//            + "Free Left : " + this.getLeftFreeDistance() + "\n"
//            + "     Front: " + this.getFrontFreeDistance() + "\n"
//            + "     Right: " + this.getRightFreeDistance() + "\n";
  }
// we keep track of the last movement
  private int lastMovement = GhostAction.NONE;
  private boolean hasRobotMoved;

  public void moveForward() {
    this.agent.moveForward();

    this.lastMovement = GhostAction.FORWARD;
    hasRobotMoved = true;
  }

  public boolean hasRobotMoved() {
    return hasRobotMoved;
  }

  public void turnLeft() {
    this.agent.turnLeft();
    this.lastMovement = GhostAction.TURN_LEFT;
    hasRobotMoved = true;
  }

  public void turnRight() {
    this.agent.turnRight();
    this.lastMovement = GhostAction.TURN_RIGHT;
    hasRobotMoved = true;
  }

  public int getLastMovement() {
//    this.log("inspecting last movement: " + this.lastMovement);
    return this.lastMovement;
  }
  boolean isNextToPacman = false;

  public boolean isIsNextToPacman() {
    return isNextToPacman;
  }

  public PacmanAgent getPacmanAgent() {
    return (PacmanAgent) getGrid().getAgent("pacman");
  }

  public void setPacManInNext(boolean b, int x, int y) {
//    if (this.isNextToPacman) {
//      Sector s = this.getGrid().getSector(pacmanX, pacmanY);
//      getGrid().removeAgent(s.getAgent());
//      s.removeAgent();
//    }
    this.isNextToPacman = b;
    if (b) {
      PacmanAgent p;
      p = getPacmanAgent();
      if (p == null) {
        p = new PacmanAgent();
      }

      Sector s = this.getGrid().getOrCreateSector(x, y);
      s.put(p, 0);
      pacmanPositionChanged = true;
    }


  }
  private boolean pacmanPositionChanged;

  public boolean isPacmanPositionChanged() {
    return pacmanPositionChanged;
  }

  @Override
  public void clearDirty() {
    changedSectors.clear();
    this.pacmanPositionChanged = false;

    this.lastMovement = GhostAction.NONE;
    hasRobotMoved = false;
  }
  */
}
