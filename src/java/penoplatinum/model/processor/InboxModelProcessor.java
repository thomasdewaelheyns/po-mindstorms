package penoplatinum.model.processor;

/**
 * Responsible for processing received messages from the ghost communication 
 * channel
 * 
 * @author MHGameWork
 */

import penoplatinum.model.part.MessageModelPart;
import penoplatinum.protocol.ProtocolHandler;


public class InboxModelProcessor extends ModelProcessor {
  // boilerplate Decorator setup
  public InboxModelProcessor() { super(); }
  public InboxModelProcessor(ModelProcessor nextProcessor) {
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
