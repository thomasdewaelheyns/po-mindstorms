public abstract class MovingAgent implements Agent {
  private String name;
  private Sector sector;
  private int bearing         = Bearing.N, 
              originalBearing = Bearing.NONE,
              originalLeft    = -999,
              originalTop     = -999;

  public MovingAgent(String name) {
    this.name = name;
  }

  public Agent setSector(Sector sector, int bearing) {
    this.sector          = sector;
    if( this.originalLeft == -999 ) {
      this.originalLeft = sector.getLeft();
      this.originalTop  = sector.getTop();
    }
    this.bearing         = bearing;
    if( this.originalBearing == Bearing.NONE ) {
      this.originalBearing = bearing;
    }
    return this;
  }
  
  public Sector getSector() {
    return this.sector;
  }
  
  public boolean isTarget() { return false; }
  public boolean isHunter() { return false; }

  public String getName()   { return this.name; }
  public int    getValue()  { return 0; }

  public int    getLeft()         { return this.sector.getLeft(); }
  public int    getOriginalLeft() { return this.originalLeft;     }
  public int    getTop()          { return this.sector.getTop();  }
  public int    getOriginalTop()  { return this.originalTop;      }

  public int    getBearing()         { return this.bearing; }
  public int    getOriginalBearing() { return this.originalBearing; }
  
  public Agent turnTo(int bearing) {
    this.bearing = bearing;
    return this;
  }
  
  public Agent moveForward() {
    int bearing = this.getBearing();
    Sector current = this.getSector();
    if( current.hasWall(bearing) ) {
      System.err.println("ERROR: Can't move through wall.");
    } else if( ! current.hasNeighbour(bearing) ) {
      System.err.println("ERROR: No neighbour to move to.");
    } else if( current.getNeighbour(bearing).hasAgent() ) {
      System.err.println("ERROR: Neighbour has Agent" );
    } else {
      // actually move the proxy
      current.removeAgent();
      current.getNeighbour(bearing).putAgent(this, bearing);
    }
    return this;
  }
}
