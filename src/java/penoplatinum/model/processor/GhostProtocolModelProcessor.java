package penoplatinum.model.processor;

import java.util.ArrayList;

import penoplatinum.grid.Sector;

import penoplatinum.model.GhostModel;
import penoplatinum.model.GridModelPart;
import penoplatinum.model.Reporter;

import penoplatinum.pacman.DashboardReporter;
import penoplatinum.pacman.GhostAction;
import penoplatinum.pacman.ProtocolHandler;

/**
 * Responsible for sending robot information to the Ghost communication channel
 * 
 * @author Team Platinum
 */

public class GhostProtocolModelProcessor extends ModelProcessor {

  public GhostProtocolModelProcessor(ModelProcessor p) {
    super(p);
  }

  public GhostProtocolModelProcessor() {
    super();
  }

  @Override
  protected void work() {
    GridModelPart grid = ((GhostModel) model).getGridPart();

    ProtocolHandler protocol = model.getMessagePart().getProtocol();

    // Send changed sectors
    ArrayList<Sector> changed = model.getGridPart().getChangedSectors();
    for (int i = 0; i < changed.size(); i++) {
      Sector current = changed.get(i);

      // for each changed sector
      protocol.sendDiscover(current);
      // report
      this.model.getReporter().reportWalls(current);
    }
    
    if (!grid.hasRobotMoved()) {
      return;
    }

    // Send position updates
    if( grid.getLastMovement() == GhostAction.FORWARD ) {
      protocol.sendPosition();
      this.model.getReporter().reportAgent(this.model.getGridPart().getAgent());
    }

    // Send pacman position updates
    if (grid.isPacmanPositionChanged()) {
      protocol.sendPacman();
    }

  }
}
