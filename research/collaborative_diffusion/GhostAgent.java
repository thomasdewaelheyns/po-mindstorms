public class GhostAgent implements Agent {
  private int left;
  private int top;
  
  private int previousMove = -1;
  
  public GhostAgent(int left, int top) {
    this.left = left;
    this.top  = top;
  }

  public int getLeft() {
    return this.left;
  }

  public int getTop() {
    return this.top;
  }
  
  public boolean isHolding() {
    return this.previousMove == -1;
  }

  public void move(int n, int e, int s, int w) {
    int move = -1;
    
    // if any of the moves brings us onto the 1000 pos, we hold our position
    if( n == 1000 || e == 1000 || s == 1000 || w == 1000 ) {
      this.previousMove = -1;
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
    if( move == -1 ) {
      move = moves[(int)(Math.random()*count)];
    }

    switch(move) {
      case Baring.N: this.top--;  break;
      case Baring.E: this.left++; break;
      case Baring.S: this.top++;  break;
      case Baring.W: this.left--; break;
      default: // do nothing
    }

    // keep track of previous move
    this.previousMove = move;
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
