/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.model;

import penoplatinum.grid.Sector;

/**
 * This class holds model data for walls detected by the robot and the layout
 * of the sector the robot is currently on
 * 
 * @author MHGameWork
 */
public class WallsModelPart implements IModelPart {

  private int wallLeftDistance;
  private int wallFrontDistance;
  private int wallRightDistance;
  private static final int WALL_DISTANCE = 35;
  private Sector currentSector = new Sector();
  private Sector prevSector = new Sector();

  public boolean isWallFront() {
    return getWallFrontDistance() < WALL_DISTANCE;
  }

  public boolean isWallLeft() {
    return getWallLeftDistance() < WALL_DISTANCE;
  }

  public boolean isWallRight() {
    return getWallRightDistance() < WALL_DISTANCE;
  }

  public int getWallFrontDistance() {
    return wallFrontDistance;
  }

  public void setWallFrontDistance(int wallFrontDistance) {
    this.wallFrontDistance = wallFrontDistance;
  }

  public int getWallLeftDistance() {
    return wallLeftDistance;
  }

  public void setWallLeftDistance(int wallLeftDistance) {
    this.wallLeftDistance = wallLeftDistance;
  }

  public int getWallRightDistance() {
    return wallRightDistance;
  }

  public void setWallRightDistance(int wallRightDistance) {
    this.wallRightDistance = wallRightDistance;
  }

  public Sector getDetectedSector() {
    return this.currentSector;
  }

  public void updateSector(Sector newSector) {
    this.prevSector = this.currentSector;
    this.currentSector = newSector;
    hasUpdatedSector = true;

  }
  private boolean hasUpdatedSector;

  public boolean hasUpdatedSector() {
    return this.hasUpdatedSector;
  }

  // Future Use: detect changes (e.g. keep track of change ratio)
  public boolean sectorHasChanged() {
    // TODO: make this more intelligent, going from unknown to known is not a
    //       negative change, but going from known/wall to known/nowall is a 
    //       bad sign
    return this.prevSector.getWalls() != this.currentSector.getWalls();
  }

  @Override
  public void clearDirty() {
    hasUpdatedSector = false;
  }
}
