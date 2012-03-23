package penoplatinum.model.processor;

import penoplatinum.model.GhostModel;

/**
 * This modelprocessor is responsible for updating the hillclimbing info
 * @author MHGameWork
 */
public class GridRecalcModelProcessor extends ModelProcessor {

  public GridRecalcModelProcessor(ModelProcessor p) {
    super(p);
  }

  public GridRecalcModelProcessor() {
  }

  @Override
  protected void work() {
    if (!model.getGridPart().hasChangedSectors()) {
      return;
    }

    for (int i = 0; i < 10; i++) {
      updateHillClimbingInfo();
    }
  }

  private void updateHillClimbingInfo() {
    ((GhostModel) this.model).getGridPart().getGrid().refresh();

  }
}
