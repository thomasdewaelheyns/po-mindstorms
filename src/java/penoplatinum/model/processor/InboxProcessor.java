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

  public InboxProcessor() {
    super();
  }

  public InboxProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  private List<String> buffer = new ArrayList<String>();
  
  protected void work() {
    MessageModelPart message = ((GhostModel) this.model).getMessagePart();
    
    buffer.clear();
    
    message.receiveIncomingMessages(buffer);
    
    for (int i = 0; i < buffer.size(); i++) {
      message.getProtocol().receive(buffer.get(i));
    }
  }
}
