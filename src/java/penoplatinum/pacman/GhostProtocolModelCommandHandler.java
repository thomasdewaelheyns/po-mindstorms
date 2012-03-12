/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.pacman;

import java.util.ArrayList;
import penoplatinum.Utils;
import penoplatinum.grid.Agent;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.grid.SimpleGrid;
import penoplatinum.map.Point;
import penoplatinum.simulator.mini.Bearing;

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

    OtherGhost ghost = model.findOtherGhost(agentName);
    Grid grid;
    if (ghost == null) {
      // update the agent's position
      grid = model.getGrid(agentName);


    } else {
      grid = model.getGrid();
      // transform the x and y coord
      Point p = ghost.getTransformationTRT().transform(x, y);
      x = p.getX();
      y = p.getY();
    }

    Sector sector = grid.getSector(x, y);
    if (sector == null) {
      sector = new Sector();
      sector.setCoordinates(x, y);
      grid.addSector(sector);
    }
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
    agent.assignSector(sector, bearing);

    //grid.refresh(); //TODO: this shouldn't run on the robot



  }

  @Override
  public void handleDiscover(String agentName, int x, int y,
          int n, int e, int s, int w) {

    // Check if there is a othergrid, or whether the file can be merged directly

    OtherGhost ghost = model.findOtherGhost(agentName);
    if (ghost == null) {
      // There is no mapping, add sector to the othergrid
      Grid grid = model.getGrid(agentName);
      setSector(grid, x, y, n, e, s, w);
    } else {
      // Merge the discovered sector into the models grid, using the stored 
      //    relative coordinates

      // transform the x and y coord
      Point p = ghost.getTransformationTRT().transform(x, y);


      Sector sector = model.getGrid().getSector(p.getX(), p.getY());
      if (sector == null) {
        sector = new Sector(model.getGrid()).setCoordinates(x, y);
        model.getGrid().addSector(sector);
      }


      Sector otherSector = new Sector();//.setCoordinates(x, y);

      int[] values = new int[]{n, e, s, w};

      for (int i = 0; i <= 3; i++) {
        Boolean newVal = GhostProtocolHandler.decodeTrit(values[i]);
        otherSector.placeWall(i, newVal);
      }

      SimpleGrid.mergeSector(sector, ghost.getTransformationTRT().getRotation(), otherSector);
    }

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
      if (newVal == null) {
        sector.clearWall(i);
      } else if (newVal) {
        sector.addWall(i);
      } else {
        sector.removeWall(i);
      }

    }
    // info set!! :P
    // grid.refresh(); //TODO: this shouldn't run on the robot
  }

  @Override
  public void handleBarcode(String agentName, int code, int bearing) {



    final Grid grid = model.getGrid(agentName);
    Agent agent = grid.getAgent(agentName);
    agent.getSector().setTagCode(code);
    agent.getSector().setTagBearing(bearing);

    // tag the current sector of the agent with the given barcode
    // check if we have it too
    // if so, import the agents map in our grid,
    //        create translators to continously import its information
    //        in our own grid
    ArrayList<Sector> bs = model.getBarcodeSectors();
    for (int i = 0; i < bs.size(); i++) {
      int ourCode = bs.get(i).getTagCode();

      int invertedCode = invertCode(code);

      if (ourCode == invertedCode) {
        code = invertedCode;

        // Switch bearing
        bearing = Bearing.reverse(bearing);
      }


//      // WARNING: this is cheat!!
//      bearing = Bearing.reverse(bearing);
//      code = ourCode;
//      // END WARNING

      if (ourCode == code) {
        final int relativeBearing = (bearing - bs.get(i).getTagBearing() + 4) % 4;

        TransformationTRT transform = new TransformationTRT().setTransformation(bs.get(i).getLeft(), bs.get(i).getTop(), relativeBearing, agent.getLeft(), agent.getTop());

        model.setOtherGhostInitialOrientation(agentName, transform);

        model.getGrid().importGrid(grid, transform);
        model.getGrid().refresh();
      }


    }


  }

  private int invertCode(int code) {
    int out = 0;
    for (int i = 0; i < 6; i++) { //TODO: hardcoded barcode length!!!
      out |= code & 1;
      code >>= 1;
      out <<= 1;
    }
    out >>= 1;
    return out;
  }
}
