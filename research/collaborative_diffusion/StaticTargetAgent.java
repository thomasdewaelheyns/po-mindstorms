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

  public void move(int[] values) {
    // we're static
  }

  public boolean isTarget() { return true;  }
  public boolean isHunter() { return false; }

  public int getValue() { return 1000; }

  public String getName() { return "pacman"; }
  public int    getLeft() { return this.sector.getLeft(); }
  public int    getTop()  { return this.sector.getTop(); }
  public int    getOrientation() { return this.bearing; }

  public Agent       setProxy(ProxyAgent proxy) { return this; }
  public ProxyAgent  getProxy() { return null; }
  
  public boolean isHolding() { return false; }
}
