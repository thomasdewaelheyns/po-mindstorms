public class PacmanAgent implements Agent {
  // our position
  private int left;
  private int top;
  
  public PacmanAgent(int left, int top) {
    this.left = left;
    this.top  = top;
  }

  public boolean isTarget() { return true;  }
  public boolean isHunter() { return false; }

  public int getValue() { return 1000; }

  public int getLeft() {
    return this.left;
  }

  public int getTop() {
    return this.top;
  }
  
  public boolean isHolding() {
    return false;
  }

  public void move(int n, int e, int s, int w) {
    int move = -1;
    
    // TODO, now just sit still
    
    switch(move) {
      case Baring.N: this.top--;  break;
      case Baring.E: this.left++; break;
      case Baring.S: this.top++;  break;
      case Baring.W: this.left--; break;
      default: // do nothing
    }
  }
  
}
