public class GhostAgent extends MovingAgent {
  private int previousMove = -1;

  public GhostAgent(String name) { super(name); }
  
  public boolean isHunter() { return true;  }

  public void move(int[] values) {
    int move = -1;
    
    // if any of the moves brings us onto the 1000 pos, we hold our position
    for(int bearing=Bearing.N;bearing<=Bearing.W;bearing++) {
      if( values[bearing] == -1000 ) {
        this.holdPosition();
        return;
      }
    }
    
    // else move towards the higher ground/scent/value
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
    
    this.go(move);

    // keep track of previous move
    this.previousMove = move;
    
    this.continueActiveMovement();
  }
  
  private void holdPosition() {
    this.previousMove = -1;
    this.cancelMovement();
  }
}
