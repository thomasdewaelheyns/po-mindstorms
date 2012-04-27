package penoplatinum.model.processor;

import java.util.ArrayList;
import java.util.List;
import penoplatinum.model.GhostModel;
import penoplatinum.model.MessageModelPart;

/**
 * Responsible for processing received messages from the ghost communication 
 * channel
 * 
 * @author MHGameWork
 */

public class InboxProcessor extends ModelProcessor {
  // boilerplate Decorator setup
  public InboxProcessor() { super(); }
  public InboxProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  // get the messages from our inbox and dispatch them to the protocolHandler
  protected void work() {
    MessageModelPart messagePart     = MessageModelPart.from(this.getModel());
    ProtocolHandler  protocolHandler = messagePart.getProtocolHandler();

    for( String message : messagePart.getIncomingMessages() ) {
      protocolHandler.receive(message);
    }
  }
}
