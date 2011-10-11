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
public class RubenMovementTest {

    public void testForwardDistances() {
        double[] distances = new double[]{0.20, 0.40, 0.60, 0.80};
        RubenMovement mov = new RubenMovement();
        mov.SPEEDFORWARD = 250;
        for (int i = 0; i < distances.length; i++) {
            System.out.println(distances[i]);
            Button.waitForPress();
            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                mov.MoveStraight(distances[i]);
                Utils.Sleep(3000);

            }
        }
    }

    public void testForwardSpeeds() {
        double distance = 0.20;

        int[] speeds = new int[]{125, 250, 500, 750, 1000};
        RubenMovement mov = new RubenMovement();

        for (int i = 0; i < speeds.length; i++) {
            System.out.println(speeds[i]);
            Button.waitForPress();
            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                mov.SPEEDFORWARD = speeds[i];
                mov.MoveStraight(distance);
                Utils.Sleep(3000);

            }
        }
    }

    public void testRotateAngles() {
        int speed = 200;

        int[] angles = new int[]{15, 30, 45, 60, 90};
        RubenMovement mov = new RubenMovement();

        mov.SPEEDTURN = speed;

        for (int i = 0; i < angles.length; i++) {

            int a = angles[i];

            System.out.println(angles[i]);
            Button.waitForPress();
            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {

                mov.TurnOnSpotCCW(a);
                Utils.Sleep(3000);

            }
        }
    }

    public void testRotateSpeeds() {
        int angle = 45;// TODO


        int[] speeds = new int[]{50, 75, 125, 250, 500, 750, 1000};
        RubenMovement mov = new RubenMovement();


        for (int i = 0; i < speeds.length; i++) {

            int speed = speeds[i];

            System.out.println(speeds[i]);
            Button.waitForPress();
            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                mov.SPEEDTURN = speed;

                mov.TurnOnSpotCCW(angle);
                Utils.Sleep(3000);

            }
        }
    }
}
