package penoplatinum.modelprocessor;

import penoplatinum.pacman.GhostModel;

public class InboxProcessor extends ModelProcessor {

  public InboxProcessor() {
    super();
  }

  public InboxProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  protected void work() {
    GhostModel model = (GhostModel) this.model;
    for (int i = 0; i < model.getIncomingMessages().size(); i++) {
      model.processMessage(model.getIncomingMessages().get(i));
    }
    // TODO
  }
}
