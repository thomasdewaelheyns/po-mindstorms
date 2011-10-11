/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PenoPlatinum;

import lejos.nxt.Button;

/**
 *
 * @author MHGameWork
 */
public class MainMichiel {

    public static void main(String[] args) {
        //Utils.comp();
        //MainFlorian.testForwardDriving();
        //SpeedBaseMovementTest test = new SpeedBaseMovementTest();
        //test.testForwardDrivingVariableSpeed();
        SpeedBasedMovementTest test = new SpeedBasedMovementTest();
        
        //System.out.println("testForwardSpeed");
        //test.testForwardSpeed();
        System.out.println("testForwardTime");
        test.testForwardTime();
        System.out.println("testRotateSpeeds");
        test.testRotateSpeeds();
        System.out.println("testRotateAngles");
        test.testRotateAngles();

        /*RubenMovement mov = new RubenMovement();

        for (int i = 0; i < 10; i++) {
            Button.waitForPress();
            mov.SPEEDFORWARD = 500;
            mov.MoveStraight(3);
        }*/



    }
}
