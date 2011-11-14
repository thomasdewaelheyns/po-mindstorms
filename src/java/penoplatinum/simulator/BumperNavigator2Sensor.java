package penoplatinum.simulator;

/**
 * BumperNavigator
 * 
 * This Navigator implementation drives until it "bumps" into something. It
 * then turns an angle and tries to continue its journey.
 * 
 * Author: Team Platinum
 */

public class BumperNavigator2Sensor implements Navigator {
  public static final int NONE   = 0;
  public static final int DRIVE  = 1;
  public static final int BACKUP = 2;
  public static final int TURN  = 4;
  
  private Model model;
  private int   status = BumperNavigator2Sensor.NONE;
  private Boolean lastStuckLeft;

  public BumperNavigator2Sensor( Model model ) {
    this.model = model;
  }
  public BumperNavigator2Sensor() {
  }
  public Navigator setModel(Model m){
    model = m;
    return this;
  }

  public Boolean reachedGoal() {
    return false;
  }

  
  //TODO Enum? gebruiken
  //TODO Andere richting draaien
  
  public int nextAction() {
    // start driving ...
    if( this.status == BumperNavigator2Sensor.NONE ) {
      System.out.println( "Whoohoow, let's drive..." );
      return this.drive();
    }
    
    // if we're driving and the model indicates we're stuck, we start the
    // backup procedure
    if( this.status == BumperNavigator2Sensor.DRIVE && this.model.isStuckLeft() ) {
      System.out.println( "Whoops, I bumped into something on my left side..." );
        return this.backup(true);
    }
    if( this.status == BumperNavigator2Sensor.DRIVE && this.model.isStuckRight() ) {
      System.out.println( "Whoops, I bumped into something on my left side..." );
        return this.backup(false);
    }

    // if we're backing up but are no longer moving, time to turn
    if( this.status == BumperNavigator2Sensor.BACKUP && ! this.model.isMoving() ) {
      return this.turnAway();
    }
    // if we're turning away but are no longer moving, time to drive again
    if( this.status == BumperNavigator2Sensor.TURN && ! this.model.isMoving() ) {
      return this.drive();
    }

    // if we're driving and we haven't bumped into something ... carry on
    return Navigator.NONE;
  }
  
  private int drive() {
    this.status = BumperNavigator2Sensor.DRIVE;
    return Navigator.MOVE;
  }

  private int backup(Boolean turnLeft) {
    this.status = BumperNavigator2Sensor.BACKUP;
    this.lastStuckLeft = turnLeft;
    return Navigator.MOVE;
  }

  private int turnAway() {
    if(lastStuckLeft){
      this.status = BumperNavigator2Sensor.TURN;
    } else {
      this.status = BumperNavigator2Sensor.TURN;
    }
    return Navigator.TURN;
  }

  public double getDistance() {
    return (this.status == BumperNavigator2Sensor.DRIVE ? 100 : -0.02);
  }

  public double getAngle() {
    return (lastStuckLeft ? -81 : 80);
  }
}
