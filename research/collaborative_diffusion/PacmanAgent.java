public class PacmanAgent implements Agent {
  // our position
  private int left;
  private int top;
  private boolean blocked = false;
  
  // our extra knowledge
  private Maze maze;
  
  public PacmanAgent(int left, int top) {
    this.left = left;
    this.top  = top;
  }

  public boolean isTarget() { return true;  }
  public boolean isHunter() { return false; }

  public int getValue() { return 1000; }

  public void setMaze(Maze maze) { 
    this.maze = maze;
  }

  public int getLeft() {
    return this.left;
  }

  public int getTop() {
    return this.top;
  }
  
  public boolean isHolding() {
    return this.blocked;
  }

  public void move(int n, int e, int s, int w) {
    int move = -1;

    if( n < 0 && e < 0 && s < 0 && w < 0 ) {
      this.blocked = true;
    }

    // get the positions of all agents and try to move away from them as 
    // far as possible
    // first calc current distance
    int bestDist = this.getDistance(this.left, this.top);

    if( n > 0 ) {
      int dist = this.getDistance(this.left, this.top-1);
      if( dist > bestDist ) { bestDist = dist; move = Baring.N; }
    }

    if( e > 0 ) {
      int dist = this.getDistance(this.left+1, this.top);
      if( dist > bestDist ) { bestDist = dist; move = Baring.E; }
    }
    
    if( s > 0 ) {
      int dist = this.getDistance(this.left, this.top+1);
      if( dist > bestDist ) { bestDist = dist; move = Baring.S; }
    }
    
    if( w > 0 ) {
      int dist = this.getDistance(this.left-1, this.top);
      if( dist > bestDist ) { bestDist = dist; move = Baring.W; }
    }

    switch(move) {
      case Baring.N: this.top--;  break;
      case Baring.E: this.left++; break;
      case Baring.S: this.top++;  break;
      case Baring.W: this.left--; break;
      default: // do nothing
    }
  }
  
  private int getDistance(int left, int top) {
    int smallestDist = 1000;
    for(Agent agent: this.maze.getAgents()) {
      if( agent != this ) {
        int dist= Math.abs(agent.getLeft() - left)
                + Math.abs(agent.getTop()  - top);
        if(dist < smallestDist) { smallestDist = dist; }
      }
    }
    return smallestDist;
  }
}
