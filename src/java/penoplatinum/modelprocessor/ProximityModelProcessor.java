package penoplatinum.modelprocessor;

/**
 * 
 * Author: Team Platinum
 */
import java.util.List;
import java.util.ArrayList;
import penoplatinum.Utils;
import penoplatinum.simulator.Model;

public class ProximityModelProcessor extends ModelProcessor {

  private static final int PROXIMITY_WALL_AVOID_DISTANCE = 25;
  private static final int OBSTACLE_THRESHOLD = 3;
  public static final int START_ANGLE = 5;
  private int leftCount = 0;
  private int rightCount = 0;

  
  
  public ProximityModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
    
  }

  public void work() {
    if (model == null) throw new RuntimeException();
    int angle = getAngle();
    int distance = getDistance();

    int change = -1;

    if (distance < PROXIMITY_WALL_AVOID_DISTANCE) {
      change = 1;
    }

    if (angle > START_ANGLE) {
      leftCount += change;
      rightCount = 0;
    } else if (angle < -START_ANGLE) {
      leftCount = 0;
      rightCount += change;
    } else {
      leftCount = 0;
      rightCount = 0;
    }


    model.setLeftObstacle(false);
    model.setRightObstacle(false);
    if (leftCount > OBSTACLE_THRESHOLD) {
      model.setLeftObstacle(true);
    }
    if (rightCount > OBSTACLE_THRESHOLD) {
      model.setRightObstacle(true);
    }
  }

  private int getDistance() {
    return this.model.getSensorValue(Model.S3);
  }

  private int getAngle() {
    return this.model.getSensorValue(Model.M3); //TODO: need middletacho here? //- middleTacho;
  }
}
