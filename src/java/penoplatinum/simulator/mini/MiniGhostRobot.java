package penoplatinum.simulator.mini;

/**
 * GhostRobot
 * 
 * Implementation of a Robot, implementing a complete Ghost
 * 
 * @author: Team Platinum
 */


import penoplatinum.model.processor.ModelProcessor;




import penoplatinum.grid.GridView;
import penoplatinum.model.GhostModel;
import penoplatinum.model.processor.GhostProtocolModelProcessor;
import penoplatinum.model.processor.GridRecalcModelProcessor;
import penoplatinum.model.processor.GridUpdateProcessor;
import penoplatinum.model.processor.InboxProcessor;
import penoplatinum.model.processor.MergeGridModelProcessor;
import penoplatinum.model.processor.WallDetectionModelProcessor;
import penoplatinum.model.processor.WallDetectorProcessor;
import penoplatinum.pacman.GhostAction;
import penoplatinum.pacman.GhostProtocolHandler;
import penoplatinum.pacman.GhostProtocolModelCommandHandler;
import penoplatinum.pacman.GhostRobot;
import penoplatinum.simulator.RobotAPI;
import penoplatinum.simulator.SimulationRobotAPI;

public class MiniGhostRobot extends GhostRobot {
  
  public MiniGhostRobot(String name) {
    super(name);
  }

  public MiniGhostRobot(String name, GridView view) {
    super(name,view);
  }
  
  @Override
  protected void setupModel(String name) {
    this.model = new GhostModel(name);
    
    linkComponents();
    
    ModelProcessor processors =
//            new MiniBarcodeModelprocessor(((MiniSimulationRobotAPI)api).getProxy(),
            new WallDetectionModelProcessor(
            new WallDetectorProcessor(
            new InboxProcessor(
            new GridUpdateProcessor(
//            new IRModelProcessor(
            new GridRecalcModelProcessor(
            new GhostProtocolModelProcessor(
            new MergeGridModelProcessor()))))));
    this.model.setProcessor(processors);

    // --- Set initial model state ---


    // Set the implementation of the ghost protocol to use

//    model.getMessagePart().setProtocol(new NullGhostProtocolHandler());
    model.getMessagePart().setProtocol(new GhostProtocolHandler(model, new GhostProtocolModelCommandHandler(model)));
    final Queue queue = new Queue();
    queue.subscribe(new MessageHandler() {

      @Override
      public void useQueue(Queue queue) {
      }

      @Override
      public void receive(String msg) {
        model.getMessagePart().queueOutgoingMessage(msg);
      }
    });
    model.getMessagePart().getProtocol().useQueue(queue);

  }

  @Override
  public void step() {
    // Not so sure this is a good idea
    readBarcodeFromApi();
    super.step();
  }
  
  private void readBarcodeFromApi()
  {
    if (!model.getGridPart().hasRobotMoved() || model.getGridPart().getLastMovement() != GhostAction.FORWARD) {
      return;
    }
    
    model.getBarcodePart().setBarcode(((MiniSimulationRobotAPI)api).readBarcode());
    
  }

  
  
  @Override
  public GhostRobot useRobotAPI(RobotAPI api) {
    
    return super.useRobotAPI(api);
  }

  
  
}
