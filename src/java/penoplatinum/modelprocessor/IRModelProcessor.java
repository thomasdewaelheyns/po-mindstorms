package penoplatinum.modelprocessor;

import penoplatinum.simulator.Model;

public class IRModelProcessor extends ModelProcessor {

  @Override
  protected void work() {
    int sum = 0;
    for (int i = Model.IR0; i <= Model.IR4; i++) {
      sum += model.getSensorValue(i);
    }
    sum /= 2;
    if (sum <= 150) {
      model.setPacManInNext(false, 0, 0);
      return;
    }
    int dx = 0, dy = 0;
    switch (model.getSensorValue(Model.S4)) {
      case 3:
        dx = -1;
        break;
      case 5:
        dy = -1;
        break;
      case 7:
        dx = 1;
        break;
    }
    if ((model.getAgent().getBearing() & 1) == 1) {
      int temp = dx;
      dx = -dy;
      dy = temp;
    }
    if ((model.getAgent().getBearing() & 2) == 2) {
      dy = -dy;
      dx = -dx;
    }
    model.setPacManInNext(true, model.getAgent().getLeft()+dx, model.getAgent().getTop()+dy);
  }
}
