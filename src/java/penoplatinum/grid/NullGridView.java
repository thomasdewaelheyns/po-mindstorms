package penoplatinum.grid;

/**
 * NullGridView
 * 
 * Implementation of GridView that doesn't do anything. It is used as an
 * initial dummy GridView, without requiring != null checks.
 * 
 * @author: Team Platinum
 */

public class NullGridView implements GridView {
  private static GridView instance = null;

  private NullGridView() {}
  
  public static GridView getInstance() {
    if(instance == null) {
      synchronized(NullGridView.class) { 
        instance = new NullGridView();
      }
    }
    return instance;
  }

  public GridView display(Grid Grid)                { return null; }
  public GridView setSectorSize(int size)           { return this; }

  public GridView refresh()                         { return this; }
  public GridView sectorsNeedRefresh()              { return this; }
  public GridView valuesNeedRefresh()               { return this; }
  public GridView agentsNeedRefresh()               { return this; }
  public GridView barcodesNeedsRefresh()            { return this; }

  
  public GridView changeTitle(String title)         { return this; }
  public GridView changeLocation(int left, int top) { return this; }

  @Override
  public GridView display(Grid aThis, boolean b) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
