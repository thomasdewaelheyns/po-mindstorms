public interface MazeView {
  public MazeView show(Maze maze);
  public MazeView refresh();
  public MazeView refreshWalls();
  public MazeView changeTitle(String title);
  public MazeView changeLocation(int left, int top);
}
