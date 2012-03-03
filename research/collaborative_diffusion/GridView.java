public interface GridView {
  public GridView show(Grid Grid);

  public GridView refresh();
  public GridView sectorsNeedRefresh();
  public GridView valuesNeedRefresh();
  public GridView agentsNeedRefresh();

  public GridView changeTitle(String title);
  public GridView changeLocation(int left, int top);
}
