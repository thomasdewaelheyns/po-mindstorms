package penoplatinum.grid;

/**
 * DiffusionGridProcessor
 * 
 * Applies a simple diffusion to all values of a Grid.
 * 
 * @author: Team Platinum
 */

import penoplatinum.simulator.mini.Bearing;

public class DiffusionGridProcessor extends GridProcessor {
  protected void work() {
    int minLeft = this.grid.getMinLeft(),  maxLeft = this.grid.getMaxLeft(),
        minTop  = this.grid.getMinTop(),   maxTop  = this.grid.getMaxTop();
        
    for(int top=minTop; top<=maxTop; top++ ) {
      for( int left=minLeft; left<=maxLeft; left++ ) {

        Sector sector = this.grid.getSector(left, top);

        if( sector != null && sector.isFullyKnown() ) {
          // a hunting agent resets the value of its sector
          if( !sector.hasAgent() ) {
            int total = 0;
            int count = 0;
            for(int atLocation=Bearing.N; atLocation<=Bearing.W; atLocation++ ) {
              // if we know about walls and there is NO wall take the sector's
              // value into account
              if( sector.isKnown(atLocation) && !sector.hasWall(atLocation) ) {
                Sector neighbour = sector.getNeighbour(atLocation);
                if( sector.hasNeighbour(atLocation) ) {
                  total += sector.getNeighbour(atLocation).getValue();
                  count++;
                }
              }
            }

            if( count > 0 ) {
              sector.setValue((int)((total/count)*0.75));
            } else {
              sector.setValue(0);
            }
          }
        }
      }
    }
  }
}
