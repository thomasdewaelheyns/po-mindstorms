package penoplatinum;

import penoplatinum.robot.Robot;

public class AngieEventLoop {

  private Robot robot;
  private AngieRobotAPI angie;
  private String lastState = "";

  public AngieEventLoop(Robot robot) {
    this.angie = new AngieRobotAPI();
    this.robot = robot;
    robot.useRobotAPI(angie);
  }

  /**
   * This method is thread safe, it invokes the eventloop to update the state
   */
  public String fetchState() throws InterruptedException {
    synchronized (updateLock) {
      updateStateInvoked = true;
      while (updateStateInvoked) {
        updateLock.wait();
      }
      return lastState;
    }

  }
  private Object updateLock = new Object();
  private boolean updateStateInvoked;

  public void runEventLoop() {
    while (true) {
      step();
    }
  }

  public void step() {
    angie.getSonar().updateSonarMovement();
    robot.step();
  }
}