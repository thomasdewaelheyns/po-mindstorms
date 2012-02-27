public interface GridView {
  public GridView show(Grid Grid);
  public GridView refresh();
  public GridView refreshSize();
  public GridView refreshWalls();
  public GridView changeTitle(String title);
  public GridView changeLocation(int left, int top);
}
