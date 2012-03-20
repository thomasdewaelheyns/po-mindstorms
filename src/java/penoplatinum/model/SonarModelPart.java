/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains information about the last sonar sweep. 
 * 
 * @author MHGameWork
 */
public class SonarModelPart {

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

  public void markSonarValuesProcessed() {
    this.newSonarValues = false;
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

  // TODO: refactor this to more function name about extrema
  //       or separate methods to get extrema
  public int[] getSonarValues() {
    this.sweepChanged = false;
    return this.sweepValues.clone(); //TODO: WARNING GC
  }
  private boolean isSweepDataChanged;
  private int[] sweepValues = new int[4];
  private boolean sweepChanged = true;

  /**
   * Returns true when the sweep was completed this step
   * @return 
   */
  public Boolean isSweepComplete() {
    return sweepComplete;
  }
  /**
   * This value is true on the step that the sweep was completed
   */
  private Boolean sweepComplete;
}
