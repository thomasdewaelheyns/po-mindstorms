// a GridView that doesn't do anything, used to avoid != null checks
public class NullGridView implements GridView {
  public GridView show(Grid Grid)                   { return null; }

  public GridView refresh()                         { return this; }
  public GridView sectorsNeedRefresh()              { return this; }
  public GridView valuesNeedRefresh()               { return this; }
  public GridView agentsNeedRefresh()               { return this; }

  public GridView changeTitle(String title)         { return this; }
  public GridView changeLocation(int left, int top) { return this; }
}
