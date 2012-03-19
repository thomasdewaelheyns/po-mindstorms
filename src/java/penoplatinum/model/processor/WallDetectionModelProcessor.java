package penoplatinum.model.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import penoplatinum.model.GhostModel;

/**
 * Implements a modelProcessor that gathers the information of the SonarSweep
 * It records a complete sweep and at the end, pushes the information to the
 * model.
 * 
 * Author: Team Platinum
 */
public class WallDetectionModelProcessor extends ModelProcessor {

  public WallDetectionModelProcessor() {
  }

  public WallDetectionModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }
  List<Integer> distances;
  List<Integer> angles;

  public void work() {
    GhostModel model = (GhostModel) this.model;
    if (!model.isSweepDataChanged()) {
      return;
    }

    // Simple process of the sweep data

    currentIndex = 0;

    distances = model.getDistances();
    angles = model.getAngles();

    if (angles.get(0) > angles.get(1)) {
      distances = new ArrayList<Integer>();
      angles = new ArrayList<Integer>();
      distances.addAll(model.getDistances());
      angles.addAll(model.getAngles());
      reverse(angles);
      reverse(distances);

    }


    model.setWallRightDistance(getEstimatedWallDistance(-110, -70));
//    model.setWallRightClosestAngle(cheatOutputAngle);

    model.setWallFrontDistance(getEstimatedWallDistance(-25, 25));

    model.setWallLeftDistance(getEstimatedWallDistance(70, 110));
//    model.setWallLeftClosestAngle(cheatOutputAngle);



    model.setWallLeft(model.getWallLeftDistance() < 35);
    model.setWallFront(model.getWallFrontDistance() < 35);
    model.setWallRight(model.getWallRightDistance() < 35);




  }
  int cheatOutputAngle;
  int currentIndex;
  

  private int getEstimatedWallDistance(int startAngle, int endAngle) {
    int sum = 0;
    int num = 0;

    int minDist = 200000;
    int minStartAngle = 100000;
    int minEndAngle = 100000;

    for (; currentIndex < distances.size(); currentIndex++) {
      int dist = distances.get(currentIndex);
      int angle = angles.get(currentIndex);
      if (angle < startAngle) {
        continue;
      }
      if (angle > endAngle) {
        break;
      }
      sum += dist;
      num++;

      if (dist < minDist) {
        minDist = dist;
        minStartAngle = angle;
        minEndAngle = angle;
      } else if (dist == minDist) {
        minEndAngle = angle;
      }


    }
    cheatOutputAngle = (minEndAngle + minStartAngle) / 2;
    if (num == 0) {
      return 2000000;
    }
    return sum / num;
  }
}
