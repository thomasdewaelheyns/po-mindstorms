package penoplatinum.model.processor;

/**
 * ChangesModelProcessor
 * 
 * Detects changes and notifies e.g. GhostProtocol parties and Dashboard
 *  
 * @author Team Platinum
 */

import java.util.List;

import penoplatinum.grid.Sector;
import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.MessageModelPart;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.protocol.ProtocolHandler;

/**
 * Responsible for sending robot information to the Ghost communication channel
 * 
 * @author Team Platinum
 */
public class ChangesModelProcessor extends ModelProcessor {

  private int pacmanID;

  public ChangesModelProcessor() {
  }

  public ChangesModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  protected void work() {
    Model model = getModel();
    if (SensorModelPart.from(model).isMoving()) {
      return;
    }
    GridModelPart gridPart = GridModelPart.from(model);
    MessageModelPart messagePart = MessageModelPart.from(model);
    ProtocolHandler protocol = messagePart.getProtocolHandler();

    List<Sector> changed = gridPart.getChangedSectors();
    for(Sector current : changed){
      // for each changed sector, notify the GhostProtocol
      protocol.handleFoundSector(current);
      // and report to dashboard (directly)
      model.getReporter().reportSectorUpdate(current);
    }
    gridPart.clearChangedSectors();

    // Send position updates 
/*
    if (grid.getLastMovement() == GhostAction.FORWARD) {
      protocol.sendPosition();
      model.getReporter().reportAgentUpdate(gridPart.getMyAgent());
    }/**/

    // Send pacman position updates
    if (gridPart.getPacmanID() > pacmanID) {
      pacmanID = gridPart.getPacmanID();
      protocol.handleFoundAgent(gridPart.getMyGrid(), gridPart.getPacmanAgent());
    }
  }
}
