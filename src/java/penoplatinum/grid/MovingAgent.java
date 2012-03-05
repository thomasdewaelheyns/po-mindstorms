package penoplatinum.grid;

/**
 * MovingAgent abstract base class
 * 
 * Abstract base class for Agents. It implements all the common logic and
 * provides methods that can be overridden to implement Agent-specific details
 * 
 * @author: Team Platinum
 */

import penoplatinum.simulator.mini.Bearing;

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

  public Agent assignSector(Sector sector, int bearing) {
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
  
  public Agent turnLeft() {
    if( this.bearing == Bearing.N ) { this.bearing = Bearing.W; }
    else { this.bearing--; }
    this.sector.getGrid().agentsNeedRefresh();
    return this;
  }
  
  public Agent turnRight() {
    if( this.bearing == Bearing.W ) { this.bearing = Bearing.N; }
    else { this.bearing++; }
    this.sector.getGrid().agentsNeedRefresh();
    return this;
  }

  public boolean canMoveForward() {
    int bearing = this.getBearing();
    Sector current = this.getSector();
    if( current.hasWall(bearing) ) {
      System.err.println(this.name + " ERROR: Can't move through wall.");
      try { System.in.read(); } catch(Exception e) {}
    } else if( ! current.hasNeighbour(bearing) ) {
      System.err.println(this.name + "ERROR: No neighbour to move to.");
      try { System.in.read(); } catch(Exception e) {}      
    } else if( current.getNeighbour(bearing).hasAgent() ) {
      System.err.println(this.name + "ERROR: Neighbour has Agent" );
      //try { System.in.read(); } catch(Exception e) {}      
    } else {
      return true;
    }
    return false;
  }
  
  public Agent moveForward() {
    int bearing = this.getBearing();
    Sector current = this.getSector();
    if( this.canMoveForward() ) {
      // actually move the agent
      current.removeAgent();
      current.getNeighbour(bearing).put(this, bearing);
      this.sector.getGrid().agentsNeedRefresh();
    } else {
      System.err.println(this.name + "ERROR: Didn't check canMoveForward?" );
      try { System.in.read(); } catch(Exception e) {}      
    }
    return this;
  }
}
