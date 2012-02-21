public class DiscoveryAgent implements Agent {
  private int  left;
  private int  top;
  private int  orientation;
  private Maze goalMaze;
  private Maze maze;
  
  // internal state: 0 = nothing, 1=find-right-hand-free-space, 2=move
  private int currentAction = 0;
    
  public DiscoveryAgent(int left, int top, int orientation, Maze maze) {
    this.left        = left;
    this.top         = top;
    this.orientation = orientation;
    this.goalMaze    = maze;
  }
  
  public void setMaze(Maze maze) {
    this.maze = maze;
  }

  public int getLeft()        { return this.left; }
  public int getTop()         { return this.top;  }
  public int getOrientation() { return this.orientation; }

  // TODO: initial test algo, implement DFS and then CD=based
  public void move(int n, int e, int s, int w) {

    switch(this.currentAction) {
      case 2: // moving in this orientation
        this.moveAhead()
            .look();
        break;
      case 0: // doing nothing ? => start looking around
        this.currentAction = 1;
      case 1: // looking around => turn right and look for wall
        this.turnRight()
            .look();
    }
  }
  
  private DiscoveryAgent moveAhead() {
    switch(this.orientation) {
      case Baring.N: this.top--;  break;
      case Baring.E: this.left++; break;
      case Baring.S: this.top++;  break;
      case Baring.W: this.left--; break;
    }
    return this;
  }
  
  private DiscoveryAgent turnRight() {
    if( this.orientation == Baring.W ) {
      this.orientation = Baring.N;
    } else {
      this.orientation++;
    }
    return this;
  }

  private DiscoveryAgent turnLeft() {
    if( this.orientation == Baring.N ) {
      this.orientation = Baring.W;
    } else {
      this.orientation--;
    }
    return this;
  }

  private DiscoveryAgent look() {
    if( this.goalMaze.hasWall(this.left, this.top, this.orientation) ) {
      // a wall, we need to look further
      this.maze.setWall(this.left, this.top, this.orientation);
      this.currentAction = 1;
    } else {
      // no wall, we can move ahead
      this.maze.unsetWall(this.left, this.top, this.orientation);
      this.currentAction = 2;
    }
    return this;
  }

  // not really applicable
  public boolean isTarget()      { return false; }
  public boolean isHunter()      { return false; }
  public int     getValue()      { return 0;     }
  public boolean isHolding()     { return false; }
}
