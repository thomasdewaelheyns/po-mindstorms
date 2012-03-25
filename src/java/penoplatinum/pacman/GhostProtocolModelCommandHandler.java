/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.pacman;

import penoplatinum.model.GhostModel;
import java.util.List;
import penoplatinum.grid.Agent;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.grid.SimpleGrid;
import penoplatinum.model.processor.MergeGridModelProcessor;
import penoplatinum.util.Point;
import penoplatinum.simulator.Bearing;

/**
 *
 * @author MHGameWork
 */
public class GhostProtocolModelCommandHandler implements GhostProtocolCommandHandler {

  private final GhostModel model;

  public GhostProtocolModelCommandHandler(GhostModel model) {
    this.model = model;

  }

  @Override
  public void handlePosition(String agentName, int x, int y) {
    Grid grid = model.getGridPart().getGrid(agentName);

    //TODO: check x and y are valid for use in our grid system

    Sector sector = grid.getOrCreateSector(x, y);
    Agent agent = grid.getAgent(agentName);
    if (agent == null) {
      // Add the agent when it doesn't exist
      agent = new GhostAgent(agentName);
      grid.addAgent(agent);
    }

    if (agent.getSector() != null) {
      agent.getSector().removeAgent();
    }

    int bearing = Bearing.N; //TODO: 


    grid.agentsNeedRefresh();
    sector.put(agent, bearing);


    //grid.refresh(); //TODO: this shouldn't run on the robot



  }

  @Override
  public void handleDiscover(String agentName, int x, int y,
          int n, int e, int s, int w) {

    //TODO: check x and y are valid for use in our grid system


    // Check if there is a othergrid, or whether the file can be merged directly

//    OtherGhost ghost = model.getGridPart().findOtherGhost(agentName);
//    if (ghost == null) {
//      // There is no mapping, add sector to the othergrid
//      Grid grid = model.getGridPart().getGrid(agentName);
//      setSector(grid, x, y, n, e, s, w);
//    } else {
    // Merge the discovered sector into the models grid, using the stored 
    //    relative coordinates

    // transform the x and y coord

    Grid grid = model.getGridPart().getGrid(agentName);
    Sector sector = grid.getOrCreateSector(x, y);

    int[] values = new int[]{n, e, s, w};

    for (int i = 0; i <= 3; i++) {
      Boolean newVal = GhostProtocolHandler.decodeTrit(values[i]);
      //TODO: dont simply set the remote sector, but set to unknown when changed first
      sector.setWall(i, newVal);
    }

//    SimpleGrid.mergeSector(sector, ghost.getTransformationTRT().getRotation(), otherSector);
//    }

  }

  public void setSector(Grid grid, int x, int y, int n, int e, int s, int w) {

    Sector sector = grid.getSector(x, y);

    if (sector == null) {
      sector = new Sector();
      sector.setCoordinates(x, y);
      grid.addSector(sector);
    }

    int[] values = new int[]{n, e, s, w};

    for (int i = 0; i <= 3; i++) {

      Boolean newVal = GhostProtocolHandler.decodeTrit(values[i]);
      sector.setWall(i, newVal);

    }
    // grid.refresh(); //TODO: this shouldn't run on the robot
  }

  @Override
  public void handleBarcode(String agentName, int code, int bearing) {



    final Grid grid = model.getGridPart().getGrid(agentName);
//    if(model.getGridPart().findOtherGhost(agentName) != null){      return;    }   
    Agent agent = grid.getAgent(agentName);
    if (agent.getSector() == null) {
      int magicI = 8;
    }
    agent.getSector().setTagCode(code);
    agent.getSector().setTagBearing(bearing);

    // tag the current sector of the agent with the given barcode
    // check if we have it too
    // if so, import the agents map in our grid,
    //        create translators to continously import its information
    //        in our own grid
    List<Sector> bs = model.getGridPart().getGrid().getTaggedSectors();
    for (int i = 0; i < bs.size(); i++) {
      model.getGridPart().getGrid().attemptMapBarcode(bs.get(i), agent.getSector(), agentName);
    }


  }

  @Override
  public void handlePacman(String agentName, int x, int y) {

    //TODO: check if input x and y coords are compatible with our grid    


    Grid grid;
    grid = model.getGridPart().getGrid(agentName);


    Agent ag = grid.getAgent("pacman");
    if (ag == null) {
      ag = new PacmanAgent();

    }


    grid.getOrCreateSector(x, y).put(ag, Bearing.N);


  }
}
