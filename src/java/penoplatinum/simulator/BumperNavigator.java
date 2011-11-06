package penoplatinum.simulator;

/**
 * BumperNavigator
 * 
 * This Navigator implementation drives until it "bumps" into something. It
 * then turns an angle and tries to continue its journey.
 * 
 * Author: Team Platinum
 */

public class BumperNavigator implements Navigator {
  public static final int NONE   = 0;
  public static final int DRIVE  = 1;
  public static final int BACKUP = 2;
  public static final int TURN   = 4;

  private Model model;
  private int   status = BumperNavigator.NONE;

  public BumperNavigator( Model model ) {
    this.model = model;
  }

  public Boolean reachedGoal() {
    return false;
  }

  public int nextAction() {
    // start driving ...
    if( this.status == BumperNavigator.NONE ) {
      System.out.println( "Whoohoow, let's drive..." );
      return this.drive();
    }
    
    // if we're driving and the model indicates we're stuck, we start the
    // backup procedure
    if( this.status == BumperNavigator.DRIVE && this.model.isStuck() ) {
      System.out.println( "Whoops, I bumped into something..." );
      return this.backup();
    }

    // if we're backing up but are no longer moving, time to turn
    if( this.status == BumperNavigator.BACKUP && ! this.model.isMoving() ) {
      return this.turnAway();
    }

    // if we're turning away but are no longer moving, time to drive again
    if( this.status == BumperNavigator.TURN && ! this.model.isMoving() ) {
      return this.drive();
    }

    // if we're driving and we haven't bumped into something ... carry on
    return Navigator.NONE;
  }
  
  private int drive() {
    this.status = BumperNavigator.DRIVE;
    return Navigator.MOVE;
  }

  private int backup() {
    this.status = BumperNavigator.BACKUP;
    return Navigator.MOVE;
  }

  private int turnAway() {
    this.status = BumperNavigator.TURN;
    return Navigator.TURN;
  }

  public double getDistance() {
    return this.status == BumperNavigator.DRIVE ? 100 : -0.02;
  }

  public double getAngle() {
    return -80;
  }
}
