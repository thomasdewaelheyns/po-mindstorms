public class GhostAgent implements Agent {
  private String name;
  private int left;
  private int top;
  private int orientation;
  
  private int previousMove = -1;
  
  // turns are executed, then moves are executed
  private int turns = 0; // number of turns, positive Right, negative Left
  private int moves = 0; // number of moves
  
  public GhostAgent(int left, int top, int orientation, String name) {
    this.left        = left;
    this.top         = top;
    this.orientation = orientation;
    this.name        = name;
  }
  
  public boolean isTarget() { return false; }
  public boolean isHunter() { return true;  }

  public int getValue() { return 0; }
  
  public void setMaze(Maze maze) { /* do nothing, we're hunters */ }

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
    return this.previousMove == -1;
  }

  public void move(int n, int e, int s, int w) {
    int move = -1;
    
    // if any of the moves brings us onto the 1000 pos, we hold our position
    if( n == -1000 || e == -1000 || s == -1000 || w == -1000 ) {
      this.holdPosition();
      return;
    }
    
    // else move towards the higher ground/scent/value
    int[] values = {n, e, s, w};
    int max = this.getMax(values);
    
    // retain moves with highest value
    int[] moves = {-1, -1, -1, -1};
    int count = 0;
    for( int i=0; i<4; i++ ) {
      if( values[i] == max ) {
        if(i == this.previousMove) {
          move = this.previousMove;
          break;
        }
        moves[count] = i;
        count++;
      }
    }

    // choose randomly one of the best moves
    if( move == -1 && !this.isMoving() ) {
      move = moves[(int)(Math.random()*count)];
    }

    switch(move) {
      case Baring.N: this.goNorth(); break;
      case Baring.E: this.goEast();  break;
      case Baring.S: this.goSouth(); break;
      case Baring.W: this.goWest();  break;
      default: // do nothing new
    }

    // keep track of previous move
    this.previousMove = move;
    
    this.processMovement();
  }
  
  private void holdPosition() {
    this.previousMove = -1;
    this.turns = 0;
    this.moves = 0;
  }
  
  private boolean isMoving() {
    return this.turns != 0 || this.moves > 0;
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

  private void processMovement() {
    if( this.turns < 0 )      { this.turnLeft();    this.turns++; }
    else if( this.turns > 0 ) { this.turnRight();   this.turns--; }
    else if( this.moves > 0 ) { this.moveForward(); this.moves--; }
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
  
  private void moveForward() {
    switch(this.orientation) {
      case Baring.N: this.top--;  break;
      case Baring.E: this.left++; break;
      case Baring.S: this.top++;  break;
      case Baring.W: this.left--; break;
      default: // impossible ? ;-)
    }
  }
  
  private int getMax(int[] values) {
    // find max
    int max = 0;
    for( int value : values ) {
      if( value > max ) { max = value; }
    }
    return max;
  }
}
