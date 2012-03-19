package penoplatinum.model.processor;

import penoplatinum.model.GhostModel;
import penoplatinum.simulator.Model;

public class IRModelProcessor extends ModelProcessor {

  public IRModelProcessor() {
  }

  public IRModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  @Override
  protected void work() {
    GhostModel model = (GhostModel) this.model;
    if (!model.needsGridUpdate()) {
      return;
    }
    int dir = model.getSensorValue(Model.S1);
    int dx = 0, dy = 0;
    switch (dir) {
      case 2:
        dx = -1;
        break;
      case 5:
        dy = -1;
        break;
      case 8:
        dx = 1;
        break;
      default: 
        model.setPacManInNext(false, 0, 0);
        return;
    }
    int sum = 0;
    if(dir % 2 == 0){
      sum += model.getSensorValue(Model.IR0+dir/2-1);
      sum += model.getSensorValue(Model.IR0+dir/2);
      sum/=2;
    } else {
      sum += model.getSensorValue(Model.IR0+dir/2);
    }
    if (sum <= 150) {
      model.setPacManInNext(false, 0, 0);
      return;
    }
    if ((model.getAgent().getBearing() & 1) == 1) { //rottate
      int temp = dx;
      dx = -dy;
      dy = temp;
    }
    if ((model.getAgent().getBearing() & 2) == 2) {//mirror 
      dy = -dy;
      dx = -dx;
    }
//    System.out.println("PACMAN");
    model.setPacManInNext(true, model.getAgent().getLeft() + dx, model.getAgent().getTop() + dy);
  }
}
