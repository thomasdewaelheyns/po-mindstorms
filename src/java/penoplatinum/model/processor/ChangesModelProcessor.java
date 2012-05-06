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
  
  
  private Point prevPosition;
  private Bearing prevBearing;

  protected void work() {
    Model model = getModel();
    GridModelPart gridPart = GridModelPart.from(model);
    if(gridPart.getMyPosition().equals(prevPosition) && prevBearing == gridPart.getMyBearing()){
      return;
    }
    prevPosition = gridPart.getMyPosition();
    prevBearing = gridPart.getMyBearing();
    
    MessageModelPart messagePart = MessageModelPart.from(model);
    ProtocolHandler protocol = messagePart.getProtocolHandler();
    
    protocol.handleEnterSector(gridPart.getMySector());
    handleBarcode(model, gridPart, protocol);
    handleDiscovery(gridPart, protocol, model);
    // Send pacman position updates
    if (gridPart.getPacmanID() > pacmanID) {
      pacmanID = gridPart.getPacmanID();
      protocol.handleFoundAgent(gridPart.getMyGrid(), gridPart.getPacmanAgent());
    }
  }

  private void handleDiscovery(GridModelPart gridPart, ProtocolHandler protocol, Model model) {
    List<Sector> changed = gridPart.getChangedSectors();
    for(Sector current : changed){
      // for each changed sector, notify the GhostProtocol
      protocol.handleFoundSector(current);
      // and report to dashboard (directly)
      if(model.getReporter() != null){
        model.getReporter().reportSectorUpdate(current);
      }
    }
    gridPart.clearChangedSectors();
  }

  private void handleBarcode(Model model, GridModelPart gridPart, ProtocolHandler protocol) {
    if(lastBarcode != BarcodeModelPart.from(model).getLastBarcodeValue()){
      lastBarcode = BarcodeModelPart.from(model).getLastBarcodeValue();
      Bearing barcodeBearing = gridPart.getMyBearing();
      int alignedCode = lastBarcode/2;
      if(alignedCode > Barcode.reverse(alignedCode, 6)){
        barcodeBearing = barcodeBearing.reverse();
        alignedCode = Barcode.reverse(alignedCode, 6);
      }
      //Utils.Log("Found barcode, handle barcode: "+gridPart.getMyPosition()+", "+barcodeBearing+" : "+alignedCode);
      BarcodeAgent agent = BarcodeAgent.getBarcodeAgent(alignedCode);
      gridPart.getMyGrid().add(agent, gridPart.getMyPosition(), barcodeBearing);
      protocol.handleFoundAgent(gridPart.getMyGrid(), agent);
    }
  }
}
