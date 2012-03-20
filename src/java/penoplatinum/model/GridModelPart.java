/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.model;

import java.util.ArrayList;
import penoplatinum.SimpleHashMap;
import penoplatinum.grid.Agent;
import penoplatinum.grid.AggregatedGrid;
import penoplatinum.grid.DiffusionGridProcessor;
import penoplatinum.grid.Grid;
import penoplatinum.grid.GridView;
import penoplatinum.grid.Sector;
import penoplatinum.grid.SimpleGrid;
import penoplatinum.pacman.GhostAction;
import penoplatinum.pacman.GhostAgent;
import penoplatinum.pacman.OtherGhost;
import penoplatinum.pacman.PacmanAgent;
import penoplatinum.simulator.Bearing;
import penoplatinum.util.TransformationTRT;
import penoplatinum.util.Utils;

/**
 * Holds this robots grid information, and the grid information of the other 
 * robots
 * 
 * TODO: maybe rename MazeModelPart (all maze information)
 * 
 * @author MHGameWork
 */
public class GridModelPart {

  // This is our grid
  private Grid myGrid;
  // This is a map of the other robots. It consists of a key representing the
  // name of the other robot/ghost and a value that contains a Grid.
  private SimpleHashMap<String, Grid> otherGrids = new SimpleHashMap<String, Grid>();
  private ArrayList<OtherGhost> otherGhosts = new ArrayList<OtherGhost>();
  // Data specific to the GhostNavigator
  // the agent on the grid
  private Agent agent;
  private ArrayList<Sector> changedSectors = new ArrayList<Sector>();

  
  public GridModelPart(String name) {
    this.agent = new GhostAgent(name);
    this.setupGrid();
  }

  // we create a new Grid, add the first sector, the starting point
  private void setupGrid() {
    this.myGrid = new AggregatedGrid().setProcessor(new DiffusionGridProcessor()).addSector(new Sector().setCoordinates(0, 0).put(this.agent, Bearing.N));
  }

  // when running in a UI environment we can provide a View for the Grid
  public void displayGridOn(GridView view) {
    this.myGrid.displayOn(view);
  }

  public Grid getGrid() {
    return this.myGrid;
  }

  public void markSectorChanged(Sector current) {
    // TODO: potential fps eater, may be better to just cache everything in the
    //       list and filter doubles later on
    for (int i = 0; i < changedSectors.size(); i++) {
      if (changedSectors.get(i) ==current)
        return;
    }
    changedSectors.add(current);
  }

  public ArrayList<Sector> getChangedSectors() {
    return changedSectors;
  }

  public boolean hasChangedSectors()
  {
    return changedSectors.size() != 0;
  }
          
  public void markChangedSectorsProcessed()
  {
    changedSectors.clear();
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

  public SimpleHashMap<String, Grid> getOtherGrids() {
    return otherGrids;
  }

  
  
  public void setOtherGhostInitialOrientation(String name, TransformationTRT transform) {
    OtherGhost g = findOtherGhost(name);
    if (g == null) {
      g = new OtherGhost();
      g.setName(name);
      otherGhosts.add(g);
    }
    g.setTransformationTRT(transform);
  }

  public OtherGhost findOtherGhost(String actorName) {
    for (int i = 0; i < otherGhosts.size(); i++) {
      if (otherGhosts.get(i).getName().equals(actorName)) {
        return otherGhosts.get(i);
      }
    }
    return null;
  }

  public void printGridStats() {

    for (int i = 0; i < otherGrids.values.size(); i++) {
      Grid g = otherGrids.values.get(i);

      Utils.Log("Grid " + i + ": " + g.getSectors().size());

    }
  }

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

  public void clearLastMovement() {
    this.lastMovement = GhostAction.NONE;
    hasRobotMoved = false;
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

      Sector s = this.getGrid().getSector(x, y);
      s.put(p, 0);
      pacmanPositionChanged = true;
    }


  }
  private boolean pacmanPositionChanged;

  public boolean isPacmanPositionChanged() {
    return pacmanPositionChanged;
  }

  public void markPacmanPositionChangeProcessed() {
    this.pacmanPositionChanged = false;
  }
  
  
}
