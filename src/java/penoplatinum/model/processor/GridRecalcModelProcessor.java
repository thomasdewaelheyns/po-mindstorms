package penoplatinum.model.processor;

import penoplatinum.model.GhostModel;
import penoplatinum.model.GridModelPart;
import penoplatinum.model.WallsModelPart;

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
    WallsModelPart walls = ((GhostModel) this.model).getWallsPart();


    if (!walls.needsGridUpdate()) {
      return;
    }
    for (int i = 0; i < 10; i++) {
      updateHillClimbingInfo();
    }
    walls.markGridUpdated();
  }

  private void updateHillClimbingInfo() {
    ((GhostModel) this.model).getGridPart().getGrid().refresh();
  }
}
