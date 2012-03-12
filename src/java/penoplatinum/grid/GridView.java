package penoplatinum.grid;

/**
 * GridView Interface
 * 
 * Interface for classes that provide a rendering implementation to visualize
 * Grids.
 * 
 * @author: Team Platinum
 */

public interface GridView {
  public GridView display(Grid grid);

  public GridView setSectorSize(int size);

  public GridView refresh();
  public GridView sectorsNeedRefresh();
  public GridView valuesNeedRefresh();
  public GridView agentsNeedRefresh();

  public GridView changeTitle(String title);
  public GridView changeLocation(int left, int top);
}
