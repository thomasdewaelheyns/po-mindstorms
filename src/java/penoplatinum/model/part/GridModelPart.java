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
import penoplatinum.grid.GhostAgent;
import penoplatinum.grid.PacmanAgent;
import penoplatinum.grid.Sector;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;


public class GridModelPart implements ModelPart {
  // boilerplate implementation required to register and retrieve a ModelPart
  // from the model
  public static GridModelPart from(Model model) {
    return (GridModelPart)model.getPart(ModelPartRegistry.GRID_MODEL_PART);
  }

  private Grid  myGrid;
  private Agent myAgent;
  private PacmanAgent pacman = new PacmanAgent();
  private ArrayList<Sector> changedSectors = new ArrayList<Sector>();
  private int pacmanID;
  
  private boolean diffusePacman, diffuseUnknownSectors;


  public Grid getMyGrid() {
    return this.myGrid;
  }

  public Agent getMyAgent() {
    return this.myAgent;
  }
  
  public void refreshMyGrid() {
    if( ! this.hasChangedSectors()) { return; }
    for(int i=0; i<10; i++) {
      this.applyDiffusion();
    }
  }
  
  private void applyDiffusion() {
    int minLeft = this.myGrid.getMinLeft(), maxLeft = this.myGrid.getMaxLeft(),
        minTop = this.myGrid.getMinTop(), maxTop = this.myGrid.getMaxTop();

    for(Sector sector : this.myGrid.getSectors()) {
      int total = 0;
      int count = 0;
      // a hunting agent resets the value of its sector
      if( this.myGrid.hasAgentOn(sector, PacmanAgent.class) ) {
        total = 10000;
        count = 1;
      } else if( this.myGrid.hasAgentOn(sector, GhostAgent.class) ) {
        // a ghost blocks all diffusion
      } else {
        // unknown sectors are "interesting"
        if( ! sector.isFullyKnown() ) {
          total = 5000;
          count = 1;
        } else {
          // diffuse
          for( Bearing atBearing: Bearing.NESW ) { 
            // if we know about walls and there is NO wall take the sector's
            // value into account
            if( sector.knowsWall(atBearing) && ! sector.hasWall(atBearing)) {
              if( sector.hasNeighbour(atBearing) ) {
                total += sector.getNeighbour(atBearing).getValue();
                count++;
              }
            }
          }
        }
      }
      if (count > 0) {
        sector.setValue((int) ((total / count) * 0.75));
      } else {
        sector.setValue(0);
      }
    }
  }
  
  public void onlyApplyCollaborateDiffusionOnPacman() {
    this.diffusePacman = true;
    this.diffuseUnknownSectors = false;
  }
  
  public void onlyApplyCollaborateDiffusionOnUnknownSectors() {
    this.diffuseUnknownSectors = true;
    this.diffusePacman = false;
  }
  
  public boolean hasChangedSectors() {
    return changedSectors.size() != 0;
  }
  
  public void markSectorChanged(Sector sector){
    changedSectors.add(sector);
  }
  
  public List<Sector> getChangedSectors() {
    return changedSectors;
  }
  
  public void clearChangedSectors() {
    changedSectors.clear();
  }
  
  public Point getMyPosition(){
    Sector sector = this.myGrid.getSectorOf(this.myAgent);
    return this.myGrid.getPositionOf(sector);
  }
  
  public Bearing getMyBearing(){
    return this.myGrid.getBearingOf(this.myAgent);
  }
  
  public void setPacMan(int x, int y) {
    this.myGrid.moveTo(this.pacman, new Point(x, y), Bearing.UNKNOWN);
    this.pacmanID++;
  }
  
  public int getPacmanID() {
    return this.pacmanID;
  }
  
  public PacmanAgent getPacmanAgent(){
    return pacman;
  }

  public Sector getCurrentSector() {
    return null;
  }
  
  /*
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

  public Agent getAgent() {
    return this.agent;
  }

  public Sector getCurrentSector() {
    return this.agent.getSector();
  }

*/
}
