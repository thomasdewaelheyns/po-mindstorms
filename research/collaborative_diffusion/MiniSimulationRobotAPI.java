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
    return new int[] {};
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
    Boolean wall, agent = false;
    wall = this.proxy.getSector().hasWall(Bearing.leftFrom(bearing));
    if(wall == null) {
      throw new RuntimeException("proxy can not determine wall to left");
    }
    if(!wall && this.proxy.getSector().getNeighbour(Bearing.leftFrom(bearing)).hasAgent()) {
      wall = true;
    }
    left = wall ? 20 : 60;

    wall = this.proxy.getSector().hasWall(bearing);
    if(wall == null) {
      throw new RuntimeException("proxy can not determine wall in front");
    }
    if(!wall && this.proxy.getSector().getNeighbour(bearing).hasAgent()) {
      wall = true;
    }
    front = wall ? 20 : 60;

    wall = this.proxy.getSector().hasWall(Bearing.rightFrom(bearing));
    if(wall == null) {
      throw new RuntimeException("proxy can not determine wall to right");
    }
    if(!wall && this.proxy.getSector().getNeighbour(Bearing.rightFrom(bearing)).hasAgent()) {
      wall = true;
    }
    right = wall ? 20 : 60;

    return new int[] { left, front, right };
  }
}
