package penoplatinum.modelprocessor;

import penoplatinum.pacman.GhostModel;

public class GridRecalcModelProcessor extends ModelProcessor{

  @Override
  protected void work() {
    GhostModel model = (GhostModel) this.model;
    if (!model.needsGridUpdate()) {
      return;
    }
    for (int i = 0; i < 10; i++) {
      updateHillClimbingInfo();
    }
    model.markGridUpdated();
  }

  private void updateHillClimbingInfo() {
    ((GhostModel) this.model).getGrid().refresh();
  }
  
}
