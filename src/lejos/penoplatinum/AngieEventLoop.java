/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import penoplatinum.simulator.Navigator;
import penoplatinum.simulator.NavigatorRobot;

/**
 *
 * @author: Team Platinum
 */
public class AngieEventLoop {

    private NavigatorRobot navigatorRobot;
    private Angie angie;
    private Navigator navigator;
    private String lastState = "";

    public AngieEventLoop() {
        this.angie = new Angie();

        navigatorRobot = new NavigatorRobot();
        navigatorRobot.useRobotAPI(angie);

    }

    public void useNavigator(Navigator navigator) {
        this.navigatorRobot.useNavigator(navigator);
        this.navigator = navigator;
    }

    private void cacheState() {
        synchronized (this) {

//            Utils.Log(this.navigatorRobot.getModelState());
            this.lastState =
                    this.navigatorRobot.getModelState() + ","
                    + this.navigatorRobot.getNavigatorState() + "," + fps;
        }
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
                fps = (int)(count / (double) delta * 1000d * 1000d * 1000d);
                System.out.println(fps);
                count = 0;
                delta = 0;
            }
            count++;

            synchronized(updateLock)
            {
                if (updateStateInvoked)
                {
                    this.cacheState();
                    updateStateInvoked =false;
                    updateLock.notify();
                }
            }
            Thread.yield();
        }

    }

    public void step() {
        angie.getSonar().updateSonarMovement();
        navigatorRobot.step();
    }
}
