public class StaticTargetAgent implements Agent {
  // our position
  private int left;
  private int top;
  private int orientation;
  private boolean blocked = false;
  
  public StaticTargetAgent(int left, int top, int orientation) {
    this.left        = left;
    this.top         = top;
    this.orientation = orientation;
  }

  public boolean isTarget() { return true;  }
  public boolean isHunter() { return false; }

  public int getValue() { return 1000; }

  public void setMaze(Maze maze) { 
    // we're not using any maze info
  }
  
  public String getName() {
    return "pacman";
  }

  public int getLeft() {
    return this.left;
  }

  public int getTop() {
    return this.top;
  }
  
  public int getOrientation() {
    return this.orientation;
  }
  
  public boolean isHolding() {
    return this.blocked;
  }

  public void move(int n, int e, int s, int w) {
    // we're static
  }
}
