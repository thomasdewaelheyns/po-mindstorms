public class MiniSimulationRobotAPI implements RobotAPI {
  private Agent proxy;
  
  public MiniSimulationRobotAPI(Agent proxy) {
    this.proxy = proxy;
  }
  
  public void move(double distance) {
    this.proxy.moveForward();
  }
  
  public void turn(int angle) {
    int bearing = this.proxy.getBearing();
    switch(angle) {
      case -90: bearing = Bearing.leftFrom (this.proxy.getBearing()); break;
      case  90: bearing = Bearing.rightFrom(this.proxy.getBearing()); break;
    }
    this.proxy.turnTo(bearing);
  }

  public void stop() {
    // nothing to do ;-)
  }
  
  public int[] getSensorValues() {
    return new int[] { this.proxy.getLeft() - this.proxy.getOriginalLeft(),
                       this.proxy.getTop()  - this.proxy.getOriginalTop(),
                       this.calcBearingDiff()
                     };
  }
  
  // TODO: too much knowledge here about default position of Agent in GhostModel
  // TODO: at least this should go into Bearing class
  private int calcBearingDiff() {
    // calc diff from default agent
    int diff = Bearing.N - this.proxy.getOriginalBearing();
    if( Math.abs(diff) == 3 ) { diff /= 3; } // -3 => 1   3 => -1
    // apply to current
    int virtual = this.proxy.getBearing() + diff;
    if( virtual < 0 ) { virtual += 3; }
    else if( virtual > 3 ) { virtual -= 3; }
    return virtual;
  }
  
  public void setSpeed(int motor, int speed) {
    // nothing to do
  }
  
  public void beep() {
    System.out.println( "BEEP" );
  }
  
  // NEW
  public void sweep(int[] bearings) {
    // nothing to do... hard-coded
  }
  
  public boolean sweepInProgress() {
    // we have everything immediately
    return false;
  }
  
  public int[] getSweepResult() {
    int left, front, right;

    int bearing = this.proxy.getBearing();
    Boolean wall = this.proxy.getSector().hasWall(Bearing.leftFrom(bearing));
    if(wall == null) {
      throw new RuntimeException("proxy can not determine wall to left");
    }
    left = wall ? 20 : 60;

    wall = this.proxy.getSector().hasWall(bearing);
    if(wall == null) {
      throw new RuntimeException("proxy can not determine wall in front");
    }
    front = wall ? 20 : 60;

    wall = this.proxy.getSector().hasWall(Bearing.rightFrom(bearing));
    if(wall == null) {
      throw new RuntimeException("proxy can not determine wall to right");
    }
    right = wall ? 20 : 60;

    return new int[] { left, front, right };
  }
}
