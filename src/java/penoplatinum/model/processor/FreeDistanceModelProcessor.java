package penoplatinum.model.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import penoplatinum.model.GhostModel;
import penoplatinum.model.SonarModelPart;
import penoplatinum.model.WallsModelPart;
import penoplatinum.util.Utils;

/**
 * Implements a modelProcessor that gathers the information of the SonarSweep
 * It records a complete sweep and at the end, pushes the information to the
 * model.
 * 
 * Author: Team Platinum
 */
public class WallDetectionModelProcessor extends ModelProcessor {

  public WallDetectionModelProcessor() {}
  public WallDetectionModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  List<Integer> distances;
  List<Integer> angles;

  public void work() {
    GhostModel model = (GhostModel) this.model;
    
    
    SonarModelPart sonar = model.getSonarPart();
    WallsModelPart walls = model.getWallsPart();
    
    
    if (!sonar.isSweepDataChanged()) {
      return;
    }

    currentIndex = 0;

    distances = sonar.getDistances();
    angles = sonar.getAngles();

    if (angles.get(0) > angles.get(1)) {
      distances = new ArrayList<Integer>();
      angles = new ArrayList<Integer>();
      distances.addAll(sonar.getDistances());
      angles.addAll(sonar.getAngles());
      Utils.reverse(angles);
      Utils.reverse(distances);
    }

    walls.setWallRightDistance(getEstimatedWallDistance(-110, -70));
    walls.setWallFrontDistance(getEstimatedWallDistance(-25, 25));
    walls.setWallLeftDistance(getEstimatedWallDistance(70, 110));
  }
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
    if (num == 0) {
      return 2000000;
    }
    return sum / num;
  }
}
