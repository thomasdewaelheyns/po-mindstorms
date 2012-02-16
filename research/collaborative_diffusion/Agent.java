public interface Agent {
  public int getLeft();
  public int getTop();
  public void move(int n, int e, int s, int w);
  public boolean isHolding();
}
