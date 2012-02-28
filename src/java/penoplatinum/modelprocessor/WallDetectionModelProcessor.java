package penoplatinum.modelprocessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements a modelProcessor that gathers the information of the SonarSweep
 * It records a complete sweep and at the end, pushes the information to the
 * model.
 * 
 * Author: Team Platinum
 */
public class WallDetectionModelProcessor extends ModelProcessor {

  public WallDetectionModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }
  List<Integer> distances;
  List<Integer> angles;

  public void work() {
    if (!model.isSweepComplete()) {
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
      Collections.reverse(angles);
      Collections.reverse(distances);

    }


    model.setWallRight(hasWall(-90, -60));
    model.setWallFront(hasWall(-30, 30));
    model.setWallLeft(hasWall(60, 90));
  }
  int currentIndex;

  private boolean hasWall(int startAngle, int endAngle) {
    int sum = 0;
    int num = 0;
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
    }

    if (num == 0) {
      return false;
    }
    if (sum / num < 30) {
      return true;
    }

    return false;
  }
}
