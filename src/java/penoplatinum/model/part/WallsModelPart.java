package penoplatinum.model.part;

/**
 * This class holds model data for walls detected by the robot and the layout
 * of the sector the robot is currently on
 * 
 * @author Team Platinum
 */

import penoplatinum.grid.Sector;

import penoplatinum.Config;

import penoplatinum.model.Model;

import penoplatinum.util.Bearing;


public class WallsModelPart implements ModelPart {
  // boilerplate implementation required to register and retrieve a ModelPart
  // from the model
  public static WallsModelPart from(Model model) {
    return (WallsModelPart)model.getPart(ModelPartRegistry.WALLS_MODEL_PART);
  }

  private int wallLeftDistance, wallFrontDistance, wallRightDistance;

  private Sector currentSector = null;
  private Sector prevSector    = null;

  // a counter producing unique identifying numbers for each sectorupdate
  private int sectorId = 0;


  public boolean isWallFront() {
    return this.getWallFrontDistance() < Config.WALL_DISTANCE;
  }

  public boolean isWallLeft() {
    return this.getWallLeftDistance() < Config.WALL_DISTANCE;
  }

  public boolean isWallRight() {
    return this.getWallRightDistance() < Config.WALL_DISTANCE;
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

  public void updateSector(Sector newSector) {
    this.prevSector    = this.currentSector;
    this.currentSector = newSector;
    this.sectorId++;
  }

  public int getSectorId() {
    return this.sectorId;
  }

  public Sector getCurrentSector() {
    return this.currentSector;
  }

  public boolean currentSectorHasChanged() {
    if(this.currentSector == null) { return false;}
    if(this.prevSector    == null) { return true; }

    for(Bearing atBearing : Bearing.NESW) {
      // known -> unknown
      if( this.prevSector.knowsWall(atBearing) &&
          ! this.currentSector.knowsWall(atBearing) ) { return true; }
      // unknown -> known
      if( ! this.prevSector.knowsWall(atBearing) &&
          this.currentSector.knowsWall(atBearing) ) { return true; }
      // known - known
      // wall <-> nowall
      if( this.prevSector.knowsWall(atBearing) && 
          this.currentSector.knowsWall(atBearing) &&
          this.prevSector.hasWall(atBearing) != 
          this.currentSector.hasWall(atBearing) ) { return true; }
    }
    return false;
  }
}
