package penoplatinum.model.processor;

/**
 * ChangesModelProcessor
 * 
 * Detects changes and notifies e.g. GhostProtocol parties and Dashboard
 *  
 * @author Team Platinum
 */

import java.util.ArrayList;

import penoplatinum.grid.Sector;

import penoplatinum.model.GhostModel;
import penoplatinum.model.GridModelPart;
import penoplatinum.model.Reporter;

import penoplatinum.reporter.DashboardReporter;
import penoplatinum.pacman.ProtocolHandler;

/**
 * Responsible for sending robot information to the Ghost communication channel
 * 
 * @author Team Platinum
 */

public class ChangesModelProcessor extends ModelProcessor {

  public GhostProtocolModelProcessor(ModelProcessor p) {
    super(p);
  }

  public GhostProtocolModelProcessor() {
    super();
  }

  protected void work() {
    GridModelPart grid = ((GhostModel) model).getGridPart();

    ProtocolHandler protocol = model.getMessagePart().getProtocol();

    ArrayList<Sector> changed = model.getGridPart().getChangedSectors();

    for (int i = 0; i < changed.size(); i++) {
      Sector current = changed.get(i);

      // for each changed sector, notify the GhostProtocol
      protocol.handleFoundSector(current);
      // and report to dashboard (directly)
      this.model.getReporter().reportWalls(current);
    }
    
    if( grid.hasRobotMoved() ) {
      // Send position updates
      if( grid.getLastMovement() == GhostAction.FORWARD ) {
        protocol.sendPosition();
        this.model.getReporter().reportAgent(this.model.getGridPart().getAgent());
      }

    // Send pacman position updates
    if( grid.isPacmanPositionChanged() ) {
      protocol.sendPacman();
    }

  }
}
