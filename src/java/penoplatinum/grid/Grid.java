package penoplatinum.grid;

/**
 * Grid Interface
 * 
 * Defines a 2D orthogonal Grid of Sectors
 *  
 * @author: Team Platinum
 */
import java.util.List;
import penoplatinum.pacman.TransformationTRT;

public interface Grid {

  public Grid setProcessor(GridProcessor processor);

  public Grid addSector(Sector sector);

  public Sector getSector(int left, int top);

  public List<Sector> getSectors();

  public Grid displayOn(GridView view);

  public Grid refresh();

  public Grid dump();

  public int getMinLeft();

  public int getMaxLeft();

  public int getMinTop();

  public int getMaxTop();

  public int getWidth();

  public int getHeight();

  public Grid addAgent(Agent agent);

  public Agent getAgent(String name);

  public List<Agent> getAgents();

  public Grid clearAgents();

  public Grid sectorsNeedRefresh();

  public Grid wallsNeedRefresh();

  public Grid valuesNeedRefresh();

  public Grid agentsNeedRefresh();

  void importGrid(Grid g, TransformationTRT transformation);

  void addTaggedSector(Sector s);

  List<Sector> getTaggedSectors();
}
