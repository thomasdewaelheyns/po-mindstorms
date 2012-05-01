package penoplatinum.fullTests;

import penoplatinum.Config;
import penoplatinum.robot.Robot;
import penoplatinum.robot.RobotAPI;

/**
 * This is a very dumb robot that is programmed to move in a square.
 * Used to test the moving capabilities of the SimulatedEntity
 * And the MotorSensors in full.
 * @author Team Platinum
 */
public class DumbRobot implements Robot {

  private RobotAPI api;
  private int state = 0;

  @Override
  public Robot useRobotAPI(RobotAPI api) {
    this.api = api;
    return this;
  }

  @Override
  public RobotAPI getRobotAPI() {
    return this.api;
  }

  @Override
  public void processCommand(String cmd) {
    //do nothing
  }

  @Override
  public void step() {
    int[] sensorValues = this.api.getSensorValues();
    if (sensorValues[Config.MS1] + sensorValues[Config.MS2] != 6) { // is movings
      return;
    }
    if (state == 12 || state == -1) {
      state = -1;
      return;
    } else if (state % 2 == 0) {
      this.api.move(0.40);
    } else if (state % 2 == 1) {
      this.api.turn(-90);
    }
    state++;
  }

  @Override
  public Boolean reachedGoal() {
    return state == -1;
  }

  @Override
  public void stop() {
    this.api.stop();
    state = -1;
  }

  @Override
  public String getName() {
    return "HELLO I AM DUmB.";
  }
}
