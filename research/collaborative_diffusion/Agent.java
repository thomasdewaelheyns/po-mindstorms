public interface Agent {
  public boolean isTarget();
  public boolean isHunter();
  public int getValue();
  public int getLeft();
  public int getTop();
  public void move(int n, int e, int s, int w);
  public boolean isHolding();
  public void setMaze(Maze maze);
}
