public class MiniSimulationRobotAPI {
  public void move( double distance ) {
    // TODO
  }
  
  public void turn( int angle ) {
    // TODO
  }

  public void stop() {
    // nothing to do ;-)
  }
  
  public int[] getSensorValues() {
    // nothing to do ;-)
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
    // TODO: fetch info from walls on the Grid
    int left=0, front=0, right=0;
    return new int[] { left, front, right }
  }
}
