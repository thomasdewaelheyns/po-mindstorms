/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import lejos.nxt.Button;

/**
 *
 * @author MHGameWork
 */
public class RobotRunner {
    public static void Run(Runnable runnable)
    {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        t.start();
        
        while (!Button.ESCAPE.isPressed()) {
            Utils.Sleep(500);
        }
        System.exit(0);

    }
}
