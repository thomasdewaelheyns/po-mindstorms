public abstract class MovingAgent implements Agent {
  private String name;

  private Sector sector;
  private int    bearing;

  // turns are executed, then moves are executed
  private int turns = 0; // number of turns, positive Right, negative Left
  private int moves = 0; // number of moves


  public MovingAgent(String name) {
    this.name = name;
  }

  public Agent setSector(Sector sector, int bearing) {
    this.sector  = sector;
    this.bearing = bearing;
    return this;
  }
  
  public Sector getSector() {
    return this.sector;
  }
  
  public boolean isTarget() { return false; }
  public boolean isHunter() { return false; }

  public int getValue() { return 0; }

  public String getName() { return this.name; }
  public int    getLeft() { return this.sector.getLeft(); }
  public int    getTop()  { return this.sector.getTop(); }
  public int    getOrientation() { return this.bearing; }
  
  protected void log(String msg) {
    System.out.printf( "%s @ %2d,%2d : %s\n",
                       this.getName(), this.getLeft(), this.getTop(), msg);
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

  protected void turnTo(int bearing) {
    switch(bearing) {
      case Bearing.N: this.turnToNorth(); break;
      case Bearing.E: this.turnToEast();  break;
      case Bearing.S: this.turnToSouth(); break;
      case Bearing.W: this.turnToWest();  break;
    }
  }

  protected void turnToNorth() {
    switch(this.bearing) {
      case Bearing.W: this.turns = +1; break;
      case Bearing.E: this.turns = -1; break;
      case Bearing.S: this.turns = +2; break;
      default: // we're already facing north ;-)
    }
  }

  protected void goNorth() {
    this.turnToNorth();
    this.moves = 1;
  }

  protected void turnToEast() {
    switch(this.bearing) {
      case Bearing.N: this.turns = +1; break;
      case Bearing.S: this.turns = -1; break;
      case Bearing.W: this.turns = +2; break;
      default: // we're already facing east ;-)
    }
  }

  protected void goEast() {
    this.turnToEast();
    this.moves = 1;
  }
  
  protected void turnToSouth() {
    switch(this.bearing) {
      case Bearing.N: this.turns = +2; break;
      case Bearing.E: this.turns = +1; break;
      case Bearing.W: this.turns = -1; break;
      default: // we're already facing south ;-)
    }
  }
  
  protected void goSouth() {
    this.turnToSouth();
    this.moves = 1;
  }

  protected void turnToWest() {
    switch(this.bearing) {
      case Bearing.N: this.turns = -1; break;
      case Bearing.E: this.turns = +2; break;
      case Bearing.S: this.turns = + 1; break;
      default: // we're already facing west ;-)
    }
  }

  protected void goWest() {
    this.turnToWest();
    this.moves = 1;
  }

  protected void processMovement() {
    if( this.turns < 0 )      { this.turnLeft();    this.turns++; }
    else if( this.turns > 0 ) { this.turnRight();   this.turns--; }
    else if( this.moves > 0 ) { this.moveForward(); this.moves--; }
  }
  
  protected void turnRight() {
    if( this.bearing == Bearing.W ) {
      this.bearing = Bearing.N;
    } else {
      this.bearing++;
    }
  }

  protected void turnLeft() {
    if( this.bearing == Bearing.N ) {
      this.bearing = Bearing.W;
    } else {
      this.bearing--;
    }
  }

  protected void moveForward() {
    Sector target = this.sector.getNeighbour(this.bearing);
    if( target == null ) {
      this.log( "can't move forward (bearing=" +this.bearing+")" );
    } else {
      this.sector.removeAgent();
      target.putAgent(this, this.bearing);
    }
  }
  
  protected void go(int bearing) {
    switch(bearing) {
      case Bearing.N: this.goNorth(); break;
      case Bearing.E: this.goEast();  break;
      case Bearing.S: this.goSouth(); break;
      case Bearing.W: this.goWest();  break;
    }
  }

  // this is proper to the concrete implementation
  public abstract void move(int n, int e, int s, int w);
}