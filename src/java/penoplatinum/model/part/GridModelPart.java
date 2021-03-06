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
import penoplatinum.grid.MultiGhostGrid;
import penoplatinum.grid.agent.Agent;
import penoplatinum.grid.AggregatedGrid;
import penoplatinum.grid.GridUtils;
import penoplatinum.grid.agent.GhostAgent;
import penoplatinum.grid.agent.PacmanAgent;
import penoplatinum.grid.Sector;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class GridModelPart implements ModelPart {

  public static final int PACMAN_VALUE = 10000;
  public static final int UNKNOWN_VALUE = 5000;
  // boilerplate implementation required to register and retrieve a ModelPart
  // from the model

  public static GridModelPart from(Model model) {
    return (GridModelPart) model.getPart(ModelPartRegistry.GRID_MODEL_PART);
  }
  private Grid myGrid;
  private Agent myAgent;
  private PacmanAgent pacman = new PacmanAgent();
  public boolean pacmanSurrounded = false;
  private ArrayList<Sector> changedSectors = new ArrayList<Sector>();
  private int pacmanID;
  private boolean diffusePacman, diffuseUnknownSectors;
  private MultiGhostGrid grids;
  private String myName;

  public GridModelPart(String name) {
    this.setup(name);
  }

  // we create the combined ghosts' grid
  // we keep a reference to our own grid and set up the first sector and agent
  private void setup(String name) {
    this.myName = name;
    this.grids = new MultiGhostGrid(name);
    this.myGrid = this.grids.getGhostGrid(name);
    AggregatedGrid aggGrid = new AggregatedGrid();
    this.grids.useAggregatedGrid(aggGrid);
    Point position = new Point(0, 0);
    this.myAgent = new GhostAgent(name);
    this.myGrid.add(this.myAgent, position, Bearing.N);
  }

  public Grid getMyGrid() {
    return this.myGrid;
  }

  public List<String> getOtherAgentsNames() {
    List<String> names = new ArrayList<String>();
    for (Agent agent : this.grids.getAgents()) {
      if (agent instanceof GhostAgent) {
        String name = agent.getName();
        if (!this.myName.equals(name)) {
          names.add(name);
        }
      }
    }
    return names;
  }

  public List<Sector> getChangedSectors(String name) {
    return this.grids.getChangedSectors(name);
  }

  public Grid getGridOf(String agentName) {
    return this.grids.getGhostGrid(agentName);
  }

  public Agent getMyAgent() {
    return this.myAgent;
  }

  public Sector getMySector() {
    Sector sector = this.myGrid.getSectorAt(this.getMyPosition());
    if (sector == null) {
//      throw new RuntimeException( "GridModelPart::getMySector: my current sector == null" );
      throw new RuntimeException();
    }
    return sector;
  }

  public Point getMyPosition() {
    return this.myGrid.getPositionOf(this.myAgent);
  }

  public Bearing getMyBearing() {
    return this.myGrid.getBearingOf(this.myAgent);
  }

  public void refreshMyGrid() {
    //if( ! this.hasChangedSectors()) { return; }
    for (int i = 0; i < 5; i++) {
      this.applyDiffusion();
    }
  }

  private void applyDiffusion() {
    Grid diffuseGrid = this.grids;
    //Grid diffuseGrid = this.myGrid;

    int minLeft = diffuseGrid.getMinLeft(), maxLeft = diffuseGrid.getMaxLeft(),
            minTop = diffuseGrid.getMinTop(), maxTop = diffuseGrid.getMaxTop();

    for (Sector sector : diffuseGrid.getSectors()) {
      int total = 0;
      int count = 0;

      Point position = diffuseGrid.getPositionOf(sector);

      // a hunting agent resets the value of its sector
      if (diffusePacman && diffuseGrid.getAgentAt(position, PacmanAgent.class) != null) {
        total = PACMAN_VALUE;
        sector.setValue(total);
      } else if (diffuseGrid.getAgentAt(position, GhostAgent.class) != null) {
        // a ghost blocks all diffusion
        total = 0;
        sector.setValue(total);
      } else if (diffuseUnknownSectors && !GridUtils.isFullyKnown(sector)) {
        // unknown sectors are "interesting"
        total = UNKNOWN_VALUE;
        sector.setValue(total);
      } else {
        // diffuse
        for (Bearing atBearing : Bearing.NESW) {
          // if we know about walls and there is NO wall take the sector's
          // value into account
          if (GridUtils.givesAccessTo(sector, atBearing)) {
            if (sector.hasNeighbour(atBearing)) {
              total += sector.getNeighbour(atBearing).getValue();
              count++;
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
  }

  public void applyDiffusionFlags(boolean unknown, boolean pacman) {
    this.diffuseUnknownSectors = unknown;
    this.diffusePacman = pacman;
  }

  public void markSectorChanged(Sector sector) {
    if (sector == null) {
      return;
    }
    this.changedSectors.add(sector);
  }

  public boolean hasChangedSectors() {
    return this.changedSectors.size() > 0;
  }

  public List<Sector> getChangedSectors() {
    return this.changedSectors;
  }

  public void clearChangedSectors() {
    this.changedSectors.clear();
  }

  public void setPacman(Grid g, Point pos) {
//    System.out.println("Pacman: "+pos);
    if (g.getPositionOf(pacman) == null) {
      g.add(this.pacman, pos, Bearing.N);
    } else {
      g.moveTo(this.pacman, pos, Bearing.N);
    }
  }

  public void incrementPacmanID() {
    this.pacmanID++;
  }

  public int getPacmanID() {
    return this.pacmanID;
  }

  public PacmanAgent getPacmanAgent() {
    return this.pacman;
  }

  public Grid getFullGrid() {
    return this.grids;
  }
}
