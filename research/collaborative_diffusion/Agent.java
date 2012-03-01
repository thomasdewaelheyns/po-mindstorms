// interface of an agent that is placed on the Grid
public interface Agent {
  public Agent    setSector(Sector sector, int atLocation);
  public Sector   getSector();
  
  public boolean  isTarget();
  public boolean  isHunter();

  public String   getName();
  public int      getValue();

  public int      getLeft();
  public int      getOriginalLeft();
  public int      getTop();
  public int      getOriginalTop();

  public int      getBearing();
  public int      getOriginalBearing();
  
  public Agent    turnTo(int bearing);
  public Agent    moveForward();
}
