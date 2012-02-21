public class PacmanAgent implements Agent {
  // our position
  private int left;
  private int top;
  private int orientation;
  private boolean blocked = false;
  
  // turns are executed, then moves are executed
  private int turns = 0; // number of turns, positive Right, negative Left
  private int moves = 0; // number of moves
    
  // our extra knowledge
  private Maze maze;
  
  public PacmanAgent(int left, int top, int orientation) {
    this.left = left;
    this.top  = top;
    this.orientation = orientation;
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
  
  public int getOrientation() {
    return this.orientation;
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
      case Baring.N: this.goNorth(); break;
      case Baring.E: this.goEast();  break;
      case Baring.S: this.goSouth(); break;
      case Baring.W: this.goWest();  break;
      default: // do nothing new
    }
    
    this.processMovement(n,e,s,w);
  }

  private void goNorth() {
    switch(this.orientation) {
      case Baring.W: this.turns = +1; break;
      case Baring.E: this.turns = -1; break;
      case Baring.S: this.turns = +2; break;
      default: // we're already facing north ;-)
    }
    this.moves = 1;
  }

  private void goEast() {
    switch(this.orientation) {
      case Baring.N: this.turns = +1; break;
      case Baring.S: this.turns = -1; break;
      case Baring.W: this.turns = +2; break;
      default: // we're already facing east ;-)
    }
    this.moves = 1;
  }
  
  private void goSouth() {
    switch(this.orientation) {
      case Baring.N: this.turns = -2; break;
      case Baring.E: this.turns = +1; break;
      case Baring.W: this.turns = -1; break;
      default: // we're already facing south ;-)
    }
    this.moves = 1;
  }

  private void goWest() {
    switch(this.orientation) {
      case Baring.N: this.turns = -1; break;
      case Baring.E: this.turns = +2; break;
      case Baring.S: this.turns = + 1; break;
      default: // we're already facing west ;-)
    }
    this.moves = 1;
  }

  private void processMovement(int n, int e, int s, int w) {
    if( this.turns < 0 )      { this.turnLeft();    this.turns++; }
    else if( this.turns > 0 ) { this.turnRight();   this.turns--; }
    else if( this.moves > 0 ) { this.moveForward(n,e,s,w); this.moves--; }
  }
  
  private void turnRight() {
    if( this.orientation == Baring.W ) {
      this.orientation = Baring.N;
    } else {
      this.orientation++;
    }
  }

  private void turnLeft() {
    if( this.orientation == Baring.N ) {
      this.orientation = Baring.W;
    } else {
      this.orientation--;
    }
  }
  
  private void moveForward(int n, int e, int s, int w) {
    switch(this.orientation) {
      case Baring.N: if( n >= 0 ) { this.top--;  } break;
      case Baring.E: if( e >= 0 ) { this.left++; } break;
      case Baring.S: if( s >= 0 ) { this.top++;  } break;
      case Baring.W: if( w >= 0 ) { this.left--; } break;
      default: // impossible ? ;-)
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
