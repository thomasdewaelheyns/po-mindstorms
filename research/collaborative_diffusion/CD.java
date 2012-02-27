public class CD {
  public static void apply(Grid grid) {
    for( int top=grid.getMinTop(); top<=grid.getMaxTop(); top++ ) {
      for( int left=grid.getMinLeft(); left<=grid.getMaxLeft(); left++ ) {
        Sector sector = grid.getSector(left, top);
        if( sector != null ) {
          // a hunting agent resets the value of its sector
          if( sector.hasAgent() && sector.getAgent().isHunter() ) {
            sector.setValue(0);
          } else {
            int total = 0;
            for(int atLocation=Bearing.N; atLocation<=Bearing.W; atLocation++ ) {
              // is we know about walls and there is no wall ...
              if( sector.isKnown(atLocation) && !sector.hasWall(atLocation) ) {
                Sector neighbour = sector.getNeighbour(atLocation);
                if( neighbour != null ) {
                  total += neighbour.getValue();
                }
              }
            }
            sector.setValue(total/4);
          }
        }
      }
    }
  }
}
