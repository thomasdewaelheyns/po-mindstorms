package penoplatinum.simulator;

/**
 * BumperNavigator
 * 
 * This Navigator implementation drives until it "bumps" into something. It
 * then turns an angle and tries to continue its journey.
 * 
 * Author: Team Platinum
 */

public class SonarNavigator implements Navigator {
  public static final int NONE   = 0;
  public static final int DRIVE  = 1;
  public static final int BACKUP = 2;
  public static final int TURN  = 4;
  
  private Model model;
  private int   status = Navigator.NONE;
  private int   angle = 0;

  public SonarNavigator( Model model ) {
    this.model = model;
  }
  public SonarNavigator(){
  }
  public SonarNavigator setModel(Model m){
    this.model = m;
    return this;
  }

  public Boolean reachedGoal() {
    return false;
  }

  
  //TODO Enum? gebruiken
  //TODO Andere richting draaien
  
  public int nextAction() {
    // start driving ...
    if(model.getSensorValue(Model.M3)==0){
      System.out.println(model.getSensorValue(Model.S3));
      if(model.getSensorValue(Model.S3)>50){
        return Navigator.MOVE;
      }
      if(model.getSensorValue(Model.S3)>40){
        angle = 5;
        return Navigator.TURN;
      }
      if(model.getSensorValue(Model.S3)>30){
        angle = -50;
        return Navigator.TURN;
      }
      if(model.getSensorValue(Model.S3)>20){
        int angle = 90;
        return Navigator.TURN;
      }
      
    }
    // if we're driving and we haven't bumped into something ... carry on
    return Navigator.NONE;
  }

  public double getDistance() {
    return 0.1;
  }

  public double getAngle() {
    return angle;
  }
}
