package penoplatinum.model.part;

/**
 * SonarModelPart
 *
 * Contains information about the last sonar sweep.
 * 
 * @author Team Platinum
 */

import java.util.List;

import penoplatinum.model.Model;


public class SonarModelPart implements ModelPart {
  // boilerplate implementation required to register and retrieve a ModelPart
  // from the model
  public static SonarModelPart from(Model model) {
    return (SonarModelPart)model.getPart(ModelPartRegistry.SONAR_MODEL_PART);
  }

  private List<Integer> distances;
  private int[] angles;

  private int sweepId = 0;


  public void update(List<Integer> distances, int[] angles) {
    this.distances = distances;
    this.angles    = angles;
    // increase the sweepId to allow users of the part to determine when they
    // want an update and do something with the sonar distances & angles
    this.sweepId++;
  }

  public List<Integer> getDistances() {
    return this.distances;
  }

  public int[] getAngles() {
    return this.angles;
  }

  public int getCurrentSweepId() {
    return this.sweepId;
  }
}
