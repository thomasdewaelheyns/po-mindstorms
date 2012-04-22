package penoplatinum.model.part;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains information about the last sonar sweep. 
 * 
 * @author MHGameWork
 */
public class SonarModelPart implements ModelPart {

  private List<Integer> distances = new ArrayList<Integer>();
  private List<Integer> angles = new ArrayList<Integer>();

  // method to update a set of distances and angles
  public void updateDistances(List<Integer> distances,
          List<Integer> angles) {
    this.distances = distances;
    this.angles = angles;
  }

  public List<Integer> getDistances() {
    return this.distances;
  }

  public List<Integer> getAngles() {
    return this.angles;
  }

  public void updateSonarValues(List<Integer> distances, List<Integer> angles) {
    this.distances = distances; //TODO: remove double
    //this.sonarValues = distances; 
    this.angles = angles;

    this.newSonarValues = true;
    isSweepDataChanged = true;
//    this.process();
  }

  public boolean hasNewSonarValues() {
    return this.newSonarValues;
  }

  public boolean isSweepDataChanged() {
    return isSweepDataChanged; //TODO: does this work???
  }

  // indicates whether the sweep-values have changed since the last time
  // they are consulted
  public boolean hasUpdatedSonarValues() {
    return this.sweepChanged;
  }
  private boolean newSonarValues = false;
  private boolean isSweepDataChanged;
  private boolean sweepChanged = true;


  @Override
  public void clearDirty() {
    this.newSonarValues = false;
    isSweepDataChanged = false;
    sweepChanged = false;

  }
}
