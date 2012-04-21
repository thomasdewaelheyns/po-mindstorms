package penoplatinum.grid;

/**
 * StaticTargetAgent
 * 
 * Agent used in the MiniSimulator to represent a not-moving target.
 * 
 * @author: Team Platinum
 */


import penoplatinum.util.Color;
import penoplatinum.util.Colors;

public class StaticTargetAgent implements Agent {
  // our position
  private Sector sector;
  private int    bearing;
  
  // our color
  private Color color = Colors.YELLOW;

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

  @Override
  public Agent createCopy() {
    return new StaticTargetAgent();
  }

  @Override
  public Agent activate() {
    return this;
  }

  @Override
  public boolean isActive() {
    return false;
  }
}
