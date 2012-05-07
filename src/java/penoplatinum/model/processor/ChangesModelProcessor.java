package penoplatinum.model.processor;

/**
 * ChangesModelProcessor
 * 
 * Detects changes and notifies e.g. GhostProtocol parties and Dashboard
 *  
 * @author Team Platinum
 */
import java.util.List;
import penoplatinum.grid.agent.BarcodeAgent;
import penoplatinum.grid.Sector;
import penoplatinum.grid.agent.PacmanAgent;
import penoplatinum.model.Model;
import penoplatinum.model.part.Barcode;
import penoplatinum.model.part.BarcodeModelPart;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.MessageModelPart;
import penoplatinum.protocol.ProtocolHandler;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

/**
 * Responsible for sending robot information to the Ghost communication channel
 * 
 * @author Team Platinum
 */
public class ChangesModelProcessor extends ModelProcessor {

  private int pacmanID;
  private int lastBarcode = -1;

  public ChangesModelProcessor() {
  }

  public ChangesModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }
  private Point prevPosition = new Point(0, 0);
  private Bearing prevBearing = Bearing.N;

  protected void work() {
    Model model = getModel();
    GridModelPart gridPart = GridModelPart.from(model);
    if (gridPart.getMyPosition().equals(prevPosition) && prevBearing == gridPart.getMyBearing()) {
      return;
    }
    prevPosition = gridPart.getMyPosition();
    prevBearing = gridPart.getMyBearing();

    MessageModelPart messagePart = MessageModelPart.from(model);
    ProtocolHandler protocol = messagePart.getProtocolHandler();

    // notify other ghosts of my new position
    protocol.handleEnterSector(gridPart.getMySector());

    // report
    this.handleBarcode(model, gridPart, protocol);
    this.handleDiscovery(model, gridPart, protocol);
    this.handleChangedValues(model, gridPart, protocol);

    // TODO: activate for reporting of other ghosts' grids
    this.handleOtherGridChanges(gridPart);

    // Send pacman position updates
    if (gridPart.getPacmanID() > pacmanID) {
      pacmanID = gridPart.getPacmanID();
      PacmanAgent pacman = gridPart.getPacmanAgent();
      protocol.handleFoundAgent(gridPart.getMyGrid(), pacman);
      if(model.getReporter() != null){
        model.getReporter().reportAgentUpdate(pacman);
      }
    }
    if (gridPart.pacmanSurrounded) {
      MessageModelPart.from(model).getProtocolHandler().handleCaptured();
    }
  }
  private String other1, other2, other3;

  private void handleOtherGridChanges(GridModelPart gridPart) {
    if (this.other1 == null || this.other2 == null || this.other3 == null) {
      this.initAgentNames(gridPart);
    }
    // TODO: how do we handle changed names ?
    if (this.other1 != null && this.other2 != null && this.other3 != null) {
      this.handleChangedSectors(gridPart, this.other1, "others[0]");
      this.handleChangedSectors(gridPart, this.other2, "others[1]");
      this.handleChangedSectors(gridPart, this.other3, "others[2]");
    }
  }

  private void handleChangedSectors(GridModelPart gridPart, String gridName, String dashboardName) {
    Model model = this.getModel();
    if (model.getReporter() != null) {
      for (Sector sector : gridPart.getChangedSectors(gridName)) {
        model.getReporter().reportSectorUpdate(sector, dashboardName);
      }
    }
  }

  private void initAgentNames(GridModelPart gridPart) {
    List<String> names = gridPart.getOtherAgentsNames();
    if (names.size() == 3) {
      this.other1 = names.get(0);
      this.other2 = names.get(1);
      this.other3 = names.get(2);
    } else {
      System.out.println("WARNING: didn't get enough other Ghost's names");
    }
  }

  private void handleDiscovery(Model model, GridModelPart gridPart, ProtocolHandler protocol) {
    for (Sector current : gridPart.getChangedSectors()) {
      // for each changed sector, notify the GhostProtocol
      protocol.handleFoundSector(current);
      // and report to dashboard (directly)
      if (model.getReporter() != null) {
        model.getReporter().reportSectorUpdate(current, "myGrid");
      }
    }
    gridPart.clearChangedSectors();
  }

  private void handleBarcode(Model model, GridModelPart gridPart, ProtocolHandler protocol) {
    if (lastBarcode != BarcodeModelPart.from(model).getLastBarcodeValue()) {
      lastBarcode = BarcodeModelPart.from(model).getLastBarcodeValue();
      Bearing barcodeBearing = gridPart.getMyBearing();
      int alignedCode = lastBarcode / 2;
      if (alignedCode > Barcode.reverse(alignedCode, 6)) {
        barcodeBearing = barcodeBearing.reverse();
        alignedCode = Barcode.reverse(alignedCode, 6);
      }
      BarcodeAgent agent = BarcodeAgent.getBarcodeAgent(alignedCode);
      gridPart.getMyGrid().add(agent, gridPart.getMyPosition(), barcodeBearing);
      protocol.handleFoundAgent(gridPart.getMyGrid(), agent);
      if (model.getReporter() != null) {
        model.getReporter().reportAgentUpdate(agent);
      }

    }
  }

  private void handleChangedValues(Model model,
          GridModelPart gridPart,
          ProtocolHandler protocol) {
    for (Sector sector : gridPart.getMyGrid().getSectors()) {
      if (model.getReporter() == null) {
        continue;
      }
      model.getReporter().reportValueUpdate(sector);
    }
  }
}