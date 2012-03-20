/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.model;

/**
 * Contains information about the last gap detected by the sonar
 * 
 * @author MHGameWork
 */
public class GapModelPart {

  private boolean gapFound;
  private int gapStartAngle;
  private int gapEndAngle;

  public int getGapEndAngle() {
    return gapEndAngle;
  }

  public void setGapEndAngle(int gapEndAngle) {
    this.gapEndAngle = gapEndAngle;
  }

  public boolean isGapFound() {
    return gapFound;
  }

  public void setGapFound(boolean gapFound) {
    this.gapFound = gapFound;
  }

  public int getGapStartAngle() {
    return gapStartAngle;
  }

  public void setGapStartAngle(int gapStartAngle) {
    this.gapStartAngle = gapStartAngle;
  }
}
