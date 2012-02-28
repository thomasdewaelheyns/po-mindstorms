import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import java.util.Arrays;

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

    // if there are no values to "climb" to, we're done
    // TODO: check global grid information to look for unknown sectors that
    //       are further away
    if( highestValue == 0 ) {
      this.finished = true;
      return;
    }

    // retain moves with highest value
    int[] moves = {-1, -1, -1, -1};
    int count = 0;
    for( int i=0; i<4; i++ ) {
      if( values[i] == highestValue ) {
        moves[count] = i;
        count++;
      }
    }

    // choose randomly one of the best moves (this introduces some non-deter-
    // minism to help solve deadlocks of agents that block each other
    int bearing = moves[(int)(Math.random()*count)];
    this.go(bearing);
    //this.log( "higest = " + highestValue + " out of " + Arrays.toString(values) + " -> " + bearing );
  }
}
