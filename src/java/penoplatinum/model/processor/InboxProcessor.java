package penoplatinum.model.processor;

import java.util.ArrayList;
import java.util.List;
import penoplatinum.pacman.GhostModel;

public class InboxProcessor extends ModelProcessor {

  public InboxProcessor() {
    super();
  }

  public InboxProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  private List<String> buffer = new ArrayList<String>();
  
  protected void work() {
    GhostModel model = (GhostModel) this.model;
    
    buffer.clear();
    
    model.receiveIncomingMessages(buffer);
    
    for (int i = 0; i < buffer.size(); i++) {
      model.processMessage(buffer.get(i));
    }
  }
}
