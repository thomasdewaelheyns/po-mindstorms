/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PenoPlatinum;

/**
 *
 * @author MHGameWork
 */
public class MainMichiel {
    public static void main(String[] args)
    {
        //Utils.comp();
        //MainFlorian.testForwardDriving();
        //SpeedBaseMovementTest test = new SpeedBaseMovementTest();
        //test.testForwardDrivingVariableSpeed();
        SpeedBasedMovementTest test = new SpeedBasedMovementTest();

        System.out.println("testForwardSpeed");
        test.testForwardSpeed();
        System.out.println("testForwardDistance");
        test.testForwardDistance();
        System.out.println("testRotateSpeeds");
        test.testRotateSpeeds();
        System.out.println("testRotateFactor");
        test.testRotateAngles();
        
    }
    
}
