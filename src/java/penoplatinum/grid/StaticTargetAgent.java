package penoplatinum.grid;

/**
 * StaticTargetAgent
 * 
 * Agent used in the MiniSimulator to represent a not-moving target.
 * 
 * @author: Team Platinum
 */


import penoplatinum.Color;
import penoplatinum.simulator.mini.Bearing;

public class StaticTargetAgent implements Agent {
  // our position
  private Sector sector;
  private int    bearing;
  
  // our color
  private Color color = new Color(255,255,0);

  public Agent assignSector(Sector sector, int bearing) {
    this.sector  = sector;
    this.bearing = bearing;
    return this;
  }
  
  public Sector getSector() {
    return this.sector;
  }
  
  public String getName()   { return "target"; }
  public int    getValue()  { return 10000; }
  public Color  getColor()  { return this.color; }

  public int    getLeft()         { return this.sector.getLeft(); }
  public int    getOriginalLeft() { return this.sector.getLeft(); }
  public int    getTop()          { return this.sector.getTop();  }
  public int    getOriginalTop()  { return this.sector.getTop();  }

  public int    getBearing() { return this.bearing; }
  public int    getOriginalBearing() { return this.bearing; }

  // whatever you do, we won't budge
  public Agent  turnLeft()          { return this; }
  public Agent  turnRight()         { return this; }

  public boolean canMoveForward()    { return false; }
  public Agent   moveForward()       { return this;  }
}
