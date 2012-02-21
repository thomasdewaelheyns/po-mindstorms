public class PacmanAgent extends MovingAgent {
  private boolean blocked = false;
  
  public PacmanAgent(int left, int top, int orientation) {
    super(left, top, orientation, "pacman" );
  }

  public boolean isTarget() { return true; }
  public int     getValue() { return 1000; }

  // we can't reuse the standard !moving implementation, because when possible
  // pacman will not be moving, but will nog be blocked
  public boolean isHolding() {
    return this.blocked;
  }

  public void move(int n, int e, int s, int w) {
    int move = -1;

    if( n < 0 && e < 0 && s < 0 && w < 0 ) {
      this.blocked = true;
    } else {
      move = this.chooseBestMove(n, e, s, w);

      switch(move) {
        case Baring.N: this.goNorth(); break;
        case Baring.E: this.goEast();  break;
        case Baring.S: this.goSouth(); break;
        case Baring.W: this.goWest();  break;
        default: // do nothing new
      }
    }
    
    this.processMovement(n, e, s, w);
  }

  private int chooseBestMove(int n, int e, int s, int w) {
    int move = -1;
    
    // get the positions of all agents and try to move away from them as 
    // far as possible
    // first calc current distance
    int bestDist = this.getDistance(this.getLeft(), this.getTop());

    if( n > 0 ) {
      int dist = this.getDistance(this.getLeft(), this.getTop()-1);
      if( dist > bestDist ) { bestDist = dist; move = Baring.N; }
    }

    if( e > 0 ) {
      int dist = this.getDistance(this.getLeft()+1, this.getTop());
      if( dist > bestDist ) { bestDist = dist; move = Baring.E; }
    }
    
    if( s > 0 ) {
      int dist = this.getDistance(this.getLeft(), this.getTop()+1);
      if( dist > bestDist ) { bestDist = dist; move = Baring.S; }
    }
    
    if( w > 0 ) {
      int dist = this.getDistance(this.getLeft()-1, this.getTop());
      if( dist > bestDist ) { bestDist = dist; move = Baring.W; }
    }
    
    return move;
  }

  private int getDistance(int left, int top) {
    int smallestDist = 1000;
    for(Agent agent: this.getMaze().getAgents()) {
      if( agent != this ) {
        int dist= Math.abs(agent.getLeft() - left)
                + Math.abs(agent.getTop()  - top);
        if(dist < smallestDist) { smallestDist = dist; }
      }
    }
    return smallestDist;
  }
}
