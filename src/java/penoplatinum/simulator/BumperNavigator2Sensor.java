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
  public static final int BACKUP_OTHER = 3;
  public static final int TURN   = 4;
  public static final int TURN_OTHER = 5;

  private Model model;
  private int   status = BumperNavigator2Sensor.NONE;

  public BumperNavigator2Sensor( Model model ) {
    this.model = model;
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
    if( this.status == BumperNavigator2Sensor.DRIVE && this.model.isStuck() ) {
      System.out.println( "Whoops, I bumped into something..." );
      if(this.model.getSensorValue(Model.S1)>25){
        System.out.println("BumberTurn");
        return this.backup();
      } else if(this.model.getSensorValue(Model.S2)>25){
        System.out.println("BumberTurnOther");
        return this.backupOther();
      }
    }

    // if we're backing up but are no longer moving, time to turn
    if( this.status == BumperNavigator2Sensor.BACKUP && ! this.model.isMoving() ) {
      return this.turnAway();
    }
    if( this.status == BumperNavigator2Sensor.BACKUP_OTHER && ! this.model.isMoving() ) {
      return this.turnAway();
    }
    // if we're turning away but are no longer moving, time to drive again
    if( this.status == BumperNavigator2Sensor.TURN && ! this.model.isMoving() ) {
      return this.drive();
    }
    if( this.status == BumperNavigator2Sensor.TURN_OTHER && ! this.model.isMoving() ) {
      return this.drive();
    }

    // if we're driving and we haven't bumped into something ... carry on
    return Navigator.NONE;
  }
  
  private int drive() {
    this.status = BumperNavigator2Sensor.DRIVE;
    return Navigator.MOVE;
  }

  private int backup() {
    this.status = BumperNavigator2Sensor.BACKUP;
    return Navigator.MOVE;
  }
  private int backupOther() {
    this.status = BumperNavigator2Sensor.BACKUP_OTHER;
    return Navigator.MOVE;
  }

  private int turnAway() {
    if(this.status == BumperNavigator2Sensor.BACKUP){
      this.status = BumperNavigator2Sensor.TURN;
    } else {
      this.status = BumperNavigator2Sensor.TURN_OTHER;
    }
    return Navigator.TURN;
  }

  public double getDistance() {
    return this.status == BumperNavigator2Sensor.DRIVE ? 100 : -0.02;
  }

  public double getAngle() {
    return (this.status == BumperNavigator2Sensor.TURN ? -81 : 80);
  }
}
