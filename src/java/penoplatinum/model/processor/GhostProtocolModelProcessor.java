/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.model.processor;

import java.util.ArrayList;
import penoplatinum.grid.Sector;
import penoplatinum.model.GhostModel;
import penoplatinum.model.GridModelPart;
import penoplatinum.pacman.DashboardAgent;
import penoplatinum.pacman.GhostAction;
import penoplatinum.pacman.GhostProtocolHandler;

/**
 * Responsible for sending robot information to the Ghost communication channel
 * Responsible for sending dashboard updates?
 * 
 * @author MHGameWork
 */
public class GhostProtocolModelProcessor extends ModelProcessor {

  DashboardAgent dashboardCommunicator;

  public void useDashboardCommunicator(DashboardAgent communicator) {
    dashboardCommunicator = communicator;
  }

  @Override
  protected void work() {
    GridModelPart grid = ((GhostModel) model).getGridPart();

    GhostProtocolHandler protocol = model.getMessagePart().getProtocol();

    if (!grid.hasRobotMoved()) {
      return;
    }

    // Send position updates
    if (grid.getLastMovement() == GhostAction.FORWARD) {
      protocol.sendPosition();
    }

    // Send pacman position updates
    if (grid.isPacmanPositionChanged()) {
      protocol.sendPacman();
    }

    // Send changed sectors
    ArrayList<Sector> changed = model.getGridPart().getChangedSectors();
    for (int i = 0; i < changed.size(); i++) {
      Sector current = changed.get(i);

      // for each changed sector
      protocol.sendDiscover(current);
      if (dashboardCommunicator != null) {
        dashboardCommunicator.sendSectorWalls(model.getGridPart().getAgent().getName(), "myGrid", current);
      }
    }




  }
}