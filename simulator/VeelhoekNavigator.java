/**
 * VeelhoekNavigator
 * 
 * This Navigator implementation drives a polygon. It doesn't use a model
 * and simply provides the next action, which is considered to be executed
 * without interruption.
 * 
 * Author: Team Platinum
 */

public class VeelhoekNavigator implements Navigator {
  private double turnAngle;
  private double vertexLength;
  private int steps;
  private int currentStep;
  private int currentSubStep;
  
  public VeelhoekNavigator( int edgeCount, double vertexLength ) {
    this.turnAngle      = 360.0 / edgeCount;
    this.vertexLength   = vertexLength;
    this.steps          = edgeCount;
    this.currentStep    = 1;
    this.currentSubStep = 0;
  }

  public Boolean reachedGoal() {
    return this.currentStep > this.steps;
  }

  public int nextAction() {
    this.currentSubStep++;
    if( this.currentSubStep > Navigator.TURN ) { // finished substeps of step
      this.currentSubStep = Navigator.MOVE;
      this.currentStep++;
    }
    if( this.reachedGoal() ) { return Navigator.NONE; }
    return this.currentSubStep;
  }

  public double getDistance() {
    return this.vertexLength;
  }

  public double getAngle() {
    return this.turnAngle;
  }
}
