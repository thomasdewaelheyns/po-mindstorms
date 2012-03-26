/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.pacman;

import penoplatinum.model.GhostModel;
import java.util.List;
import penoplatinum.grid.Agent;
import penoplatinum.grid.AggregatedSubGrid;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
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
    //TODO: check x and y are valid for use in our grid system

    AggregatedSubGrid grid = model.getGridPart().getGrid(agentName);
    grid.setActorPosition(agentName, x, y);


    model.getGridPart().getGrid().DEBUG_checkGridCorrectness(model.getGridPart().getAgent());

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

    AggregatedSubGrid grid = model.getGridPart().getGrid(agentName);

    int[] values = new int[]{n, e, s, w};
    Boolean[] walls = new Boolean[4];

    for (int i = 0; i <= 3; i++) {
      walls[i] = GhostProtocolHandler.decodeTrit(values[i]);
    }

    Sector oldSector = model.getGridPart().getGrid().getSector(x, y);

    grid.setSector(x, y, walls);

    Sector newSector = model.getGridPart().getGrid().getSector(x, y);
    if (oldSector == null || newSector.getWalls() != oldSector.getWalls()) {
      if (newSector != null) {
        model.getGridPart().markSectorChanged(newSector);
      }
    }

    model.getGridPart().getGrid().DEBUG_checkGridCorrectness(model.getGridPart().getAgent());

  }

  @Override
  public void handleBarcodeAt(String agentName, int left, int top, int code, int bearing) {
    final AggregatedSubGrid grid = model.getGridPart().getGrid(agentName);

    grid.setBarcodeAt(left,top, code, bearing);

    model.getGridPart().getGrid().DEBUG_checkGridCorrectness(model.getGridPart().getAgent());

  }

  @Override
  public void handlePacman(String agentName, int x, int y) {

    //TODO: check if input x and y coords are compatible with our grid    


    AggregatedSubGrid grid = model.getGridPart().getGrid(agentName);
    grid.setPacmanPosition(x, y);


    model.getGridPart().getGrid().DEBUG_checkGridCorrectness(model.getGridPart().getAgent());
  }
}
