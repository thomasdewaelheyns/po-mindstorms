import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class DiscoveryAgent extends MovingAgent {
  private boolean finished = false;
    
  public DiscoveryAgent() { super( "discoverer" ); }
  
  public boolean isHolding() {
    return this.finished;
  }
  
  public void move(int[] values) {
    if( this.isMoving() ) {
      this.continueActiveMovement();
    } else if( this.currentSectorHasUnknownWalls() ) {
      this.detectSectorWalls();
    } else if( ! this.finished ) {
      this.chooseNextAction(values);
    } else {
      this.log("Nothing more to do..." );
    }
  }
  
  private boolean currentSectorHasUnknownWalls() {
    Sector sector = this.getSector();
    return sector != null && !sector.isFullyKnown();
  }
  
  private void detectSectorWalls() {
    Sector sector = this.getSector();
    // look at each wall that isn't known yet
    for(int bearing=Bearing.W; bearing>=Bearing.N; bearing-- ) {
      if( ! sector.isKnown(bearing) ) {
        if( this.getOrientation() != bearing ) {
          this.turnTo(bearing);
        } else {
          // TODO: change to lookAt(bearing) thingy
          // FIXME: should we take into account the fact that we don't know
          //        if the sonar detects a wall or another agent ?
          Boolean hasWall = this.getProxy().getSector()
                                .hasWall(this.getProxy().getOrientation());
          if( hasWall != null ) {
            if( hasWall ) {
              sector.addWall(bearing);
            } else {
              sector.removeWall(bearing);
              sector.createNeighbour(bearing).setValue(5000);
            }
          }
        }
        break;
      }
    }
  }
  
  private void chooseNextAction(int[] values) {
    int highestValue = this.getMax(values);
    if( highestValue == 0 ) {
      this.finished = true;
      return;
    }

    int highestBearing = Bearing.NONE;
    
    for(int bearing=Bearing.N;bearing<=Bearing.W;bearing++) {
      if( values[bearing] == highestValue ) {
        this.go(bearing);
        return;
      }
    }

  }
}
