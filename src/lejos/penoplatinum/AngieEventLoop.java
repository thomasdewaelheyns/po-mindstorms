/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import penoplatinum.simulator.Navigator;
import penoplatinum.simulator.NavigatorRobot;

/**
 *
 * @author MHGameWork
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
        while (true) {
            step();
            //Utils.Sleep(10);
        }
        
    }

    public void step() {
        angie.getSonar().updateSonarMovement();
        navigatorRobot.step();
    }
}
