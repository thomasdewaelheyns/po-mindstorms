package penoplatinum.model.processor;

/**
 * IRModelProcessor
 *
 * Responsible for processing IR information and detecting pacman
 * 
 * @author Team Platinum
 */

import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.SensorModelPart;

import penoplatinum.util.Bearing;


public class IRModelProcessor extends ModelProcessor {
  // boilerplate decorator setup
  public IRModelProcessor() {}
  public IRModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  protected void work() {
    SensorModelPart sensor = SensorModelPart.from(this.getModel());
    
    // we don't work while moving
    if( sensor.isMoving()) { return; }
    
    // based on the direction of the signal, we calculate the translation
    // of the position, relative to our current position
    int dir = sensor.getIRDirection();
    int dx = 0, dy = 0;
    switch (dir) {
      case 2:    dx = -1;     break;
      case 5:    dy = -1;     break;
      case 8:    dx =  1;     break;
      default:   return;
    }

    // based on the IRValues we calculate a sum, which should be above
    // 150. probably a minimum signal quality
    // lot's of magic numbers, no comments, ... this is not a cleaned up class
    int sum = 0;
    if(dir % 2 == 0) {
      sum += sensor.getIRValue(dir/2-1);
      sum += sensor.getIRValue(dir/2);
      sum/=2;
    } else {
      sum += sensor.getIRValue(dir/2);
    }
    if( sum <= 150 ) { return; }

    GridModelPart gridPart = GridModelPart.from(this.getModel());

    // more magic changes to the translation
    // luckily the bug was in the unit test and not here, because it's close
    // to impossible to understand this in a reasonable time, which we don't
    // have at the moment *sigh*
    Bearing myBearing = gridPart.getMyBearing();
    if( myBearing == Bearing.E || myBearing == Bearing.W ) { // rotate
      int temp = dx;
      dx = -dy;
      dy = temp;
    }
    if( myBearing == Bearing.S || myBearing == Bearing.W ) { // mirror 
      dy = -dy;
      dx = -dx;
    }

    gridPart.setPacman(gridPart.getMyPosition().translate(dx, dy));
  }
}
