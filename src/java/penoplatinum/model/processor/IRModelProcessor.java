package penoplatinum.model.processor;

import penoplatinum.model.GhostModel;
import penoplatinum.model.GridModelPart;
import penoplatinum.model.SensorModelPart;
import penoplatinum.simulator.Model;

/**
 * Responsible for processing IR information and detecting pacman
 * 
 * @author MHGameWork
 */
public class IRModelProcessor extends ModelProcessor {

  public IRModelProcessor() {
  }

  public IRModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  @Override
  protected void work() {
    GhostModel model = (GhostModel) this.model;
    
    
    SensorModelPart sensor = model.getSensorPart();
    GridModelPart grid = model.getGridPart();
    
    
    if (!model.getWallsPart().needsGridUpdate()) {
      return;
    }
    int dir = sensor.getSensorValue(Model.S1);
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
        grid.setPacManInNext(false, 0, 0);
        return;
    }
    int sum = 0;
    if(dir % 2 == 0){
      sum += sensor.getSensorValue(Model.IR0+dir/2-1);
      sum += sensor.getSensorValue(Model.IR0+dir/2);
      sum/=2;
    } else {
      sum += sensor.getSensorValue(Model.IR0+dir/2);
    }
    if (sum <= 150) {
      grid.setPacManInNext(false, 0, 0);
      return;
    }
    if ((grid.getAgent().getBearing() & 1) == 1) { //rottate
      int temp = dx;
      dx = -dy;
      dy = temp;
    }
    if ((grid.getAgent().getBearing() & 2) == 2) {//mirror 
      dy = -dy;
      dx = -dx;
    }
//    System.out.println("PACMAN");
    grid.setPacManInNext(true, grid.getAgent().getLeft() + dx, grid.getAgent().getTop() + dy);
  }
}
