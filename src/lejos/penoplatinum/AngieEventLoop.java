/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import lejos.util.Stopwatch;
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

    public AngieEventLoop() {
        this.angie = new Angie();

        navigatorRobot = new NavigatorRobot();
        navigatorRobot.useRobotAPI(angie);
        
    }

    public void useNavigator(Navigator navigator) {
        this.navigatorRobot.useNavigator(navigator);
        this.navigator = navigator;
    }

    public void runEventLoop() {
        int count = 0;
        int delta = 0;
        while (true) {
            long start = System.nanoTime();
            step();
            delta += System.nanoTime() - start;
            if (delta > 1000L * 1000 * 1000) {
                System.out.println(count / (double) delta * 1000d * 1000d*1000d);
                count = 0;
                delta = 0;
            }
            count++;
        }

    }

    public void step() {
        angie.getSonar().updateSonarMovement();
        navigatorRobot.step();
    }
}
