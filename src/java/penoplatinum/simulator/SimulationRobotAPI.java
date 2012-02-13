package penoplatinum.simulator;

/**
 * SimulationRobotAPI
 * 
 * Implements the RobotAPI interface to track the robot's actions and return
 * this information to the Simulator.
 * 
 * @author: Team Platinum
 */
public class SimulationRobotAPI implements RobotAPI {

  private SimulatedEntity simulatedEntity;
  private int sonarAngle = 90;   // the sonar moves from - to + this angle
  private int prevSonarTacho = 0;

  public void setSimulatedEntity(SimulatedEntity simulator) {
    this.simulatedEntity = simulator;
  }

  public void move(double distance) {
    this.simulatedEntity.moveRobot(distance);
  }

  public void turn(int angle) {
    this.simulatedEntity.turnRobot(angle);
  }

  public void stop() {
    this.simulatedEntity.stopRobot();
  }

  public int[] getSensorValues() {
    // we inject the sweeping behaviour of the sonar at this point, because
    // it allows us to simulate an autonous thread without requiring one.
    this.restartSonarMotor();

    // put all sensorvalues in an array and return them
    int[] out = new int[this.simulatedEntity.getSensorValues().length];
    for (int i = 0; i < out.length; i++) {
      out[i] = (int) this.simulatedEntity.getSensorValues()[i];
    }
    return out;
  }

  public void setSpeed(int motor, int speed) {
    this.simulatedEntity.setSpeed(motor, speed);
  }
  private int motorDelay = 10;

  private void restartSonarMotor() {
    int currentTacho = (int) this.simulatedEntity.getSensorValues()[Model.M3];
    // if the motor has finished its previous movement, sweep back ...
    if (currentTacho == this.prevSonarTacho && motorDelay == 0) {
      motorDelay = 20;

    }
    if (motorDelay > 0) {
      motorDelay--;
      if (motorDelay == 1) { // Warning: this 1 here is deliberate
        this.sonarAngle *= -1;
        simulatedEntity.rotateMotorTo(Model.M3, this.sonarAngle);
      }
    }

    this.prevSonarTacho = currentTacho;
  }

  @Override
  public void beep() {
    System.out.println("BEEP");
  }
}
