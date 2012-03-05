package penoplatinum.simulator.tiles;

public class Sectors {
  public static Sector NONE = new Sector()
          .addWall(0)
          .addWall(1)
          .addWall(2)
          .addWall(3);
  
  public static Sector NOWALL = new Sector();
  
  public static Sector N = new Sector()
          .addWall(0);
  public static Sector E = new Sector()
          .addWall(1);
  public static Sector S = new Sector()
          .addWall(2);
  public static Sector W = new Sector()
          .addWall(3);
  
  public static Sector NE = new Sector()
          .addWall(0)
          .addWall(1);
  public static Sector NS = new Sector()
          .addWall(0)
          .addWall(2);
  public static Sector NW = new Sector()
          .addWall(0)
          .addWall(3);
  public static Sector ES = new Sector()
          .addWall(1)
          .addWall(2);
 public static Sector EW = new Sector()
          .addWall(1)
          .addWall(3);
 public static Sector SW = new Sector()
          .addWall(2)
          .addWall(3);
 
 public static Sector NES = new Sector()
          .addWall(0)
          .addWall(1)
          .addWall(2);
 public static Sector NEW = new Sector()
          .addWall(0)
          .addWall(1)
          .addWall(3);
 public static Sector NSW = new Sector()
          .addWall(0)
          .addWall(2)
          .addWall(3);
 public static Sector ESW = new Sector()
          .addWall(1)
          .addWall(2)
          .addWall(3);
    
 public static Sector NESW = new Sector()
          .addWall(0)
          .addWall(1)
          .addWall(2)
          .addWall(3);
}
