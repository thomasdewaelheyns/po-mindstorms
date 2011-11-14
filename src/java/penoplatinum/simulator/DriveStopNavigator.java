package penoplatinum.simulator;

/**
 * DriveStopNavigator
 * 
 * This Navigator implementation drives until it "bumps" into something. It
 * then stops, even if its movement hasn't completed.
 * 
 * Author: Team Platinum
 */

public class DriveStopNavigator implements Navigator {
  private Model model;
  private Boolean driving = false;
  private Boolean bumped  = false;

  public DriveStopNavigator( Model model ) {
    this.model = model;
  }

  public Boolean reachedGoal() {
    return this.bumped;
  }

  public int nextAction() {
    if( this.model.isStuck() ) {
      // we've bumped into something
      System.out.println( "Whoops, I bumped into something..." );
      this.bumped = true;
      return Navigator.STOP;
    }

    // start driving
    if( ! this.driving ) { return Navigator.MOVE; }
    
    // if we're driving and we haven't bumped into something ... carry on
    return Navigator.NONE;
  }

  public double getDistance() {
    return 100;
  }

  public double getAngle() {
    return 0;
  }

  @Override
  public Navigator setModel(Model m) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
