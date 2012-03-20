/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import penoplatinum.util.Utils;
import penoplatinum.simulator.Navigator;
import penoplatinum.simulator.Robot;

/**
 *
 * @author: Team Platinum
 */
public class AngieEventLoop {

    private Robot robot;
    private AngieRobotAPI angie;
    private Navigator navigator;
    private String lastState = "";

    public AngieEventLoop(Robot robot) {
        this.angie = new AngieRobotAPI();

        this.robot = robot;
        robot.useRobotAPI(angie);

    }

    private void cacheState() {
//        synchronized (this) {
//            //TODO: WARNING, this was copied/moved to NavigatorRobot, fps was removed
//            this.lastState = this.navigatorRobot.getStatusMessage();
//        }
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
    private int fps;

    public void runEventLoop() {
        int count = 0;
        int delta = 0;
        while (true) {
            long start = System.nanoTime();
            step();
            delta += System.nanoTime() - start;
            if (delta > 1000L * 1000 * 1000) {
                fps = (int) (count / (double) delta * 1000d * 1000d * 1000d);
                Utils.Log("FPS: " + Integer.toString(fps));
                count = 0;
                delta = 0;
            }
            count++;

//            synchronized(updateLock)
//            {
//                if (updateStateInvoked)
//                {
//                    this.cacheState();
//                    updateStateInvoked =false;
//                    updateLock.notify();
//                }
//            }
//            Thread.yield();
            
            
        }

    }

    public void step() {
        angie.getSonar().updateSonarMovement();
        robot.step();
    }
}
