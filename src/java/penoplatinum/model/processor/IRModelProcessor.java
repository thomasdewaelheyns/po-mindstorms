package penoplatinum.model.processor;

import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

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
    Model model = getModel();
    
    SensorModelPart sensor = SensorModelPart.from(model);
    if(sensor.isMoving()){
      return;
    }
    
    GridModelPart grid = GridModelPart.from(model);
      
    int dir = sensor.getIRDirection();
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
        return;
    }
    int sum = 0;
    if(dir % 2 == 0){
      sum += sensor.getIRValue(dir/2-1);
      sum += sensor.getIRValue(dir/2);
      sum/=2;
    } else {
      sum += sensor.getIRValue(dir/2);
    }
    if (sum <= 150) {
      return;
    }
    Bearing myBearing = grid.getCurrentBearing();
    if (myBearing == Bearing.E || myBearing == Bearing.W) { //rotate
      int temp = dx;
      dx = -dy;
      dy = temp;
    }
    if (myBearing == Bearing.S || myBearing == Bearing.W) {//mirror 
      dy = -dy;
      dx = -dx;
    }
    
    Point position = grid.getMyPosition();
    grid.setPacMan(position.getX() + dx, position.getY() + dy);
  }
}
