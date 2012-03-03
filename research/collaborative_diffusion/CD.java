public class CD {
  public static void apply(Grid grid) {
    for( int top=grid.getMinTop(); top<=grid.getMaxTop(); top++ ) {
      for( int left=grid.getMinLeft(); left<=grid.getMaxLeft(); left++ ) {

        Sector sector = grid.getSector(left, top);

        if( sector != null && sector.isFullyKnown() ) {
          // a hunting agent resets the value of its sector
          if( sector.hasAgent() && sector.getAgent().isHunter() ) {
            sector.setValue(0);
          } else {
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
            // TODO: determine optimal algorithm
            if( count > 0 ) {
              sector.setValue((int)((total/count)*0.75));
            } else {
              sector.setValue(total/4);
            }
          }
        }
      }
    }
  }
}
