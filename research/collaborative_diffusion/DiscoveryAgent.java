import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class DiscoveryAgent extends MovingAgent {
  private boolean finished = false;
    
  public DiscoveryAgent() { super( "discoverer" ); }
  
  public boolean isHolding() {
    return this.finished;
  }
  
  public void move(int n, int e, int s, int w) {
    if( this.isMoving() ) {
      //this.log("Continue active movement...");
      this.processMovement();
    } else if( this.currentTileHasUnknownWalls() ) {
      //this.log("Detecting tile...");
      this.detectTile();
    } else if( ! this.finished ) {
      //this.log("Following highest Scent..." );
      this.followScent();
    } else {
      this.log("Nothing more to do..." );
    }
  }
  
  private boolean currentTileHasUnknownWalls() {
    Sector sector = this.getSector();
    return sector != null && !sector.isFullyKnown();
  }
  
  private void detectTile() {
    Sector sector = this.getSector();
    // look at each wall that isn't known yet
    for(int bearing=Bearing.W; bearing>=Bearing.N; bearing-- ) {
      if( ! sector.isKnown(bearing) ) {
        if( this.getOrientation() != bearing ) {
          //this.log( "turing to " + bearing + " to detect it" );
          this.turnTo(bearing);
        } else {
          //this.log( "looking at " + bearing );
          Boolean hasWall = this.getProxy().getSector()
                                .hasWall(this.getProxy().getOrientation());
          if( hasWall != null ) {
            if( hasWall ) {
              //this.log( "Wall @ " + bearing + " -> moving on" );
              sector.addWall(bearing);
            } else {
              //this.log( "No Wall @ " + bearing + " -> adding new Sector" );
              sector.removeWall(bearing);
              // no wall, this is interesting, let's add a new Sector
              sector.createNeighbour(bearing).setValue(500);
            }
          }
        }
        break;
      }
    }
  }
  
  private void followScent() {
    int highestValue   = 0;
    int highestBearing = Bearing.NONE;
    // detect scents
    for(int bearing=Bearing.N;bearing<=Bearing.W;bearing++) {
      Sector sector = this.getSector();
      if( ! sector.hasWall(bearing) ) {
        Sector neighbour = sector.getNeighbour(bearing);
        if( neighbour != null ) {
          if( neighbour.getValue() > highestValue ) {
            highestValue   = neighbour.getValue();
            highestBearing = bearing;
          }
        }
      }
    }
    if( highestValue == 0 ) { this.finished = true; }
    this.go(highestBearing);
  }
}
