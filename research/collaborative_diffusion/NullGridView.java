// a GridView that doesn't do anything, used to avoid == null checks
public class NullGridView implements GridView {
  public GridView show(Grid Grid)                   { return null; }
  public GridView refresh()                         { return this; }
  public GridView refreshSize()                     { return this; }
  public GridView refreshWalls()                    { return this; }
  public GridView changeTitle(String title)         { return this; }
  public GridView changeLocation(int left, int top) { return this; }
}
