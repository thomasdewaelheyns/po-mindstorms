package penoplatinum.model.part;

/**
 * This class holds model data for walls detected by the robot and the layout
 * of the sector the robot is currently on
 * 
 * @author Team Platinum
 */

import penoplatinum.grid.Sector;

import penoplatinum.model.Model;


public class WallsModelPart implements ModelPart {
  // boilerplate implementation required to register and retrieve a ModelPart
  // from the model
  public static WallsModelPart from(Model model) {
    return (WallsModelPart)model.getPart(ModelPartRegistry.WALLS_MODEL_PART);
  }

  private int wallLeftDistance;
  private int wallFrontDistance;
  private int wallRightDistance;

  private static final int WALL_DISTANCE = 35;

  private boolean hasUpdatedSector;

  private Sector currentSector = null;
  private Sector prevSector    = null;


  public boolean isWallFront() {
    return this.getWallFrontDistance() < WALL_DISTANCE;
  }

  public boolean isWallLeft() {
    return this.getWallLeftDistance() < WALL_DISTANCE;
  }

  public boolean isWallRight() {
    return this.getWallRightDistance() < WALL_DISTANCE;
  }

  public int getWallFrontDistance() {
    return this.wallFrontDistance;
  }

  public void setWallFrontDistance(int wallFrontDistance) {
    this.wallFrontDistance = wallFrontDistance;
  }

  public int getWallLeftDistance() {
    return this.wallLeftDistance;
  }

  public void setWallLeftDistance(int wallLeftDistance) {
    this.wallLeftDistance = wallLeftDistance;
  }

  public int getWallRightDistance() {
    return this.wallRightDistance;
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

  public void clearDirty() {
    this.hasUpdatedSector = false;
  }
}
