package penoplatinum.model.processor;

/**
 * WallDetectionProcessor
 * 
 * Uses wall distance information to construct a Sector representing the 
 * current sector.
 * 
 * @author Team Platinum
 */

import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.SonarModelPart;
import penoplatinum.model.part.WallsModelPart;

import penoplatinum.grid.Sector;
import penoplatinum.grid.LinkedSector;
import penoplatinum.grid.Agent;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;


public class WallDetectionModelProcessor extends ModelProcessor {
  // boilerplate Decorator setup
  public WallDetectionModelProcessor() { super(); }
  public WallDetectionModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }
  
  private int    sweepId = 0;
  private Point  prevPosition;

  protected void work() {
    // only update when we have a complete set of sensorvalues
    if( ! this.newSonarValuesAreAvailable() ) { return; }
    

    WallsModelPart wallsPart = WallsModelPart.from(this.getModel());
    GridModelPart  gridPart  = GridModelPart.from(this.getModel());

    LinkedSector sector = new LinkedSector();
    Bearing bearing     = gridPart.getMyBearing();

    // front, left and right
    if( wallsPart.isWallFront() ) { sector.setWall(bearing);               }
    else                          { sector.setNoWall(bearing);             }
    if( wallsPart.isWallLeft() )  { sector.setWall(bearing.leftFrom());    }
    else                          { sector.setNoWall(bearing.leftFrom());  }
    if( wallsPart.isWallRight() ) { sector.setWall(bearing.rightFrom());   }
    else                          { sector.setNoWall(bearing.rightFrom()); }

    Sector prevSector      = wallsPart.getCurrentSector();
    Point  currentPosition = gridPart.getMyPosition();

    // if we have moved into another sector, we came through the rear "wall"
    if( this.prevPosition != null && 
        ! currentPosition.equals(this.prevPosition) )
    {
      sector.setNoWall(bearing.reverse());
    } else {
      if( prevSector != null ) {
        // we didn't move, so things remain the same, or we turned, which 
        // means our rear is facing a previously prevSector wall
        if( prevSector.knowsWall(bearing.reverse()) ) {
          if( prevSector.hasWall(bearing.reverse()) ) {
            sector.setWall(bearing.reverse());
          } else {
            sector.setNoWall(bearing.reverse());
          }
        }
      }/**/
    }

    // keep track of the (new) position ;-)
    this.prevPosition = currentPosition;

    // store the new sector layout
    wallsPart.updateSector(sector);
    System.out.println("Detected walls: "+sector);
  }
  
  private boolean newSonarValuesAreAvailable() {
    int currentSweepId =
      SonarModelPart.from(this.getModel()).getCurrentSweepId();
    if( currentSweepId == this.sweepId ) { return false; }
    this.sweepId = currentSweepId;
    return true;
  }

}
