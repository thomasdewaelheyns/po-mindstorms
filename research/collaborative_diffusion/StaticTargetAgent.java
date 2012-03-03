public class StaticTargetAgent implements Agent {
  // our position
  private Sector sector;
  private int    bearing;

  public Agent setSector(Sector sector, int bearing) {
    this.sector  = sector;
    this.bearing = bearing;
    return this;
  }
  
  public Sector getSector() {
    return this.sector;
  }
  
  public boolean isTarget() { return true;  }
  public boolean isHunter() { return false; }

  public String getName()   { return "target"; }
  public int    getValue()  { return 10000; }

  public int    getLeft()         { return this.sector.getLeft(); }
  public int    getOriginalLeft() { return this.sector.getLeft(); }
  public int    getTop()          { return this.sector.getTop();  }
  public int    getOriginalTop()  { return this.sector.getTop();  }

  public int    getBearing() { return this.bearing; }
  public int    getOriginalBearing() { return this.bearing; }

  // whatever you do, we won't budge
  public Agent  turnLeft()          { return this; }
  public Agent  turnRight()         { return this; }
  public Agent  moveForward()       { return this; }
}
