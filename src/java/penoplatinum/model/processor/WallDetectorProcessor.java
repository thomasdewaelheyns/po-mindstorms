package penoplatinum.model.processor;

import penoplatinum.simulator.Bearing;
import penoplatinum.model.GhostModel;
import penoplatinum.pacman.GhostAction;
import penoplatinum.grid.Sector;
import penoplatinum.grid.Agent;


public class WallDetectorProcessor extends ModelProcessor {
  public WallDetectorProcessor() { super(); }
  public WallDetectorProcessor( ModelProcessor nextProcessor ) {
    super(nextProcessor);
  }

  protected void work() {
    GhostModel model = (GhostModel)this.model;
    Sector detected  = model.getDetectedSector();
    int lastMovement = model.getLastMovement();

    

    // if we moved forward, the previously detected sector is no longer of
    // any interest
    if(lastMovement == GhostAction.FORWARD) {
      detected.clearCertainty();
    } // else we keep it, so we can access it

    // only update when we have a complete set of sensorvalues
    if( ! model.hasNewSonarValues() ) { return; }
    
    Sector sector    = new Sector();
    
    Agent agent      = model.getAgent();
    int bearing      = agent.getBearing();
    
    
    // front = free distance front/bearing
    if( model.getFrontFreeDistance() < 35 ) {
      sector.addWall(bearing);
    } else {
      sector.removeWall(bearing);
    }
    
    // back = depends on last movement
    // TODO: clean this up (probably extract methods)
    // System.out.println( model.getAgent().getName() + " : previous Detected Wall configuration: " );
    // System.out.println( detected );
    // System.out.println( "========= determining back " + lastMovement + " at " + bearing );
    switch(lastMovement) {
      case GhostAction.FORWARD:
        // there is no wall the way we came, we can remove it for sure
        sector.removeWall(Bearing.reverse(bearing));
        break;
      case GhostAction.TURN_LEFT:
      case GhostAction.TURN_RIGHT:
        // we turned our back to a wall we normally shoudl have detected on 
        // a previous step when we entered this sector
        if( detected.isKnown(Bearing.reverse(bearing)) ) {
          if( detected.hasWall(Bearing.reverse(bearing)) ) {
            sector.addWall(Bearing.reverse(bearing));
          } else {
            sector.removeWall(Bearing.reverse(bearing));
          }
        } else { 
          // shouldn't happen
//          System.err.println( "We turned and don't known what we know ?" );
//          try { System.in.read(); } catch(Exception e) {}
        }
        break;
      case GhostAction.NONE:
      default:
        // we don't know anything more than we knew before, so let's copy that
        if( detected.isKnown(Bearing.reverse(bearing)) ) {
          if( detected.hasWall(Bearing.reverse(bearing)) ) {
            sector.addWall(Bearing.reverse(bearing));
          } else {
            sector.removeWall(Bearing.reverse(bearing));
          }
        } else { 
          // this can actually happen, once, when we are at the first sector,
          // haven't turned around and just did nothing to start with
          // strange, but it can happen ;-)
        }
    }
    
    // left = leftFrom(bearing)
    if( model.getLeftFreeDistance() < 35 ) {
      sector.addWall(Bearing.leftFrom(bearing));
    } else {
      sector.removeWall(Bearing.leftFrom(bearing));
    }

    // right = rightFrom(bearing)
    if( model.getRightFreeDistance() < 35 ) {
      sector.addWall(Bearing.rightFrom(bearing));
    } else {
      sector.removeWall(Bearing.rightFrom(bearing));
    }

    // System.out.println( model.getAgent().getName() + " : Detect new Wall configuration: " );
    // System.out.println( sector );
    // try { System.in.read(); } catch( Exception e ) {}

    model.updateSector(sector);
    model.markSonarValuesProcessed();
  }
}
