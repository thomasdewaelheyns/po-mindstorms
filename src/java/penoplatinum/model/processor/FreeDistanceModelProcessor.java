package penoplatinum.model.processor;

import java.util.ArrayList;
import java.util.List;
import penoplatinum.model.Model;
import penoplatinum.model.part.SonarModelPart;
import penoplatinum.model.part.WallsModelPart;
import penoplatinum.util.Utils;

/**
 * Implements a modelProcessor that gathers the information of the SonarSweep
 * It records a complete sweep and at the end, pushes the information to the
 * model.
 * 
 * Author: Team Platinum
 */
public class FreeDistanceModelProcessor extends ModelProcessor {

  public FreeDistanceModelProcessor() {
  }

  public FreeDistanceModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }
  
  private int lastSweepID;

  public void work() {
    Model model = getModel();
    SonarModelPart sonar = SonarModelPart.from(model);
    WallsModelPart walls = WallsModelPart.from(model);

    if (sonar.getCurrentSweepId()<=lastSweepID) {
      return;
    }

    lastSweepID = sonar.getCurrentSweepId();

    List<Integer> distances = sonar.getDistances();
    int[] angles = sonar.getAngles();

    if (angles[0] > angles[1]) {
      distances = new ArrayList<Integer>();
      int[] anglesNew = new int[angles.length];
      distances.addAll(sonar.getDistances());
      Utils.reverse(angles, anglesNew);
      angles = anglesNew;
      Utils.reverse(distances);
    }

    walls.setWallRightDistance(getEstimatedWallDistance(angles, distances, -110, -70));
    walls.setWallFrontDistance(getEstimatedWallDistance(angles, distances, -25, 25));
    walls.setWallLeftDistance (getEstimatedWallDistance(angles, distances, 70, 110));
  }

  private int getEstimatedWallDistance(int[] angles, List<Integer> distances, int startAngle, int endAngle) {
    int sum = 0;
    int num = 0;
    
    for (int currentIndex = 0; currentIndex < distances.size(); currentIndex++) {
      int angle = angles[currentIndex];
      if (angle < startAngle) {
        continue;
      }
      if (angle > endAngle) {
        break;
      }
      sum += distances.get(currentIndex);
      num++;
    }
    if (num == 0) {
      return 2000000;
    }
    return sum / num;
  }
}
