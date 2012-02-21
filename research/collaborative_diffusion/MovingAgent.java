public abstract class MovingAgent implements Agent {
  private String name;
  private int    left;
  private int    top;
  private int    orientation;
  private Maze   maze;

  // turns are executed, then moves are executed
  private int turns = 0; // number of turns, positive Right, negative Left
  private int moves = 0; // number of moves

  public MovingAgent(int left, int top, int orientation, String name) {
    this.left        = left;
    this.top         = top;
    this.orientation = orientation;
    this.name        = name;
  }
  
  public boolean isTarget() { return false; }
  public boolean isHunter() { return false; }

  public int getValue() { return 0; }

  public void setMaze(Maze maze) {
    this.maze = maze;
  }
  
  protected Maze getMaze() {
    return this.maze;
  }

  public String getName() {
    return this.name;
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
  
  public boolean isMoving() {
    return this.turns != 0 || this.moves > 0;
  }

  public boolean isHolding() {
    return ! this.isMoving();
  }
  
  protected void cancelMovement() {
    this.turns = 0;
    this.moves = 0;
  }

  protected void goNorth() {
    switch(this.orientation) {
      case Baring.W: this.turns = +1; break;
      case Baring.E: this.turns = -1; break;
      case Baring.S: this.turns = +2; break;
      default: // we're already facing north ;-)
    }
    this.moves = 1;
  }

  protected void goEast() {
    switch(this.orientation) {
      case Baring.N: this.turns = +1; break;
      case Baring.S: this.turns = -1; break;
      case Baring.W: this.turns = +2; break;
      default: // we're already facing east ;-)
    }
    this.moves = 1;
  }
  
  protected void goSouth() {
    switch(this.orientation) {
      case Baring.N: this.turns = -2; break;
      case Baring.E: this.turns = +1; break;
      case Baring.W: this.turns = -1; break;
      default: // we're already facing south ;-)
    }
    this.moves = 1;
  }

  protected void goWest() {
    switch(this.orientation) {
      case Baring.N: this.turns = -1; break;
      case Baring.E: this.turns = +2; break;
      case Baring.S: this.turns = + 1; break;
      default: // we're already facing west ;-)
    }
    this.moves = 1;
  }

  protected void processMovement(int n, int e, int s, int w) {
    if( this.turns < 0 )      { this.turnLeft();    this.turns++; }
    else if( this.turns > 0 ) { this.turnRight();   this.turns--; }
    else if( this.moves > 0 ) { this.moveForward(n,e,s,w); this.moves--; }
  }
  
  protected void turnRight() {
    if( this.orientation == Baring.W ) {
      this.orientation = Baring.N;
    } else {
      this.orientation++;
    }
  }

  protected void turnLeft() {
    if( this.orientation == Baring.N ) {
      this.orientation = Baring.W;
    } else {
      this.orientation--;
    }
  }
  
  protected void moveForward(int n, int e, int s, int w) {
    switch(this.orientation) {
      case Baring.N: if( n >= 0 ) { this.top--;  } break;
      case Baring.E: if( e >= 0 ) { this.left++; } break;
      case Baring.S: if( s >= 0 ) { this.top++;  } break;
      case Baring.W: if( w >= 0 ) { this.left--; } break;
      default: // impossible ? ;-)
    }
  }

  // this is proper to the concrete implementation
  public abstract void move(int n, int e, int s, int w);
}