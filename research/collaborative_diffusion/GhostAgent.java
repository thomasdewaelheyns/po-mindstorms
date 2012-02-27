public class GhostAgent extends MovingAgent {
  private int previousMove = -1;

  public GhostAgent(String name) { super(name); }
  
  public boolean isHunter() { return true;  }

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
      case Bearing.N: this.goNorth(); break;
      case Bearing.E: this.goEast();  break;
      case Bearing.S: this.goSouth(); break;
      case Bearing.W: this.goWest();  break;
      default: // do nothing new
    }

    // keep track of previous move
    this.previousMove = move;
    
    this.processMovement();
  }
  
  private void holdPosition() {
    this.previousMove = -1;
    this.cancelMovement();
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
