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
public class SpeedBasedMovementTest {

    private SpeedBasedMovement movement;
    private SpeedBasedMovement mov;

    public SpeedBasedMovementTest() {
        movement = new SpeedBasedMovement();
        mov = movement;
    }


    public void testForwardTime() {
        float speed, time;
        speed = 250;
        time = 2;

        float[] values = new float[]{0.5f, 1f, 1.5f, 2.0f};

        for (int i = 0; i < values.length; i++) {

            time = values[i];

            System.out.println(values[i]);
            Button.waitForPress();



            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                mov.forward(speed, time);
                Utils.Sleep(3000);

            }
        }
    }

    public void testForwardSpeed() {
        float speed, time;
        int factor;
        time = 2;
        factor = 2000;

        int[] values = new int[]{125, 250, 500, 750, 1000};

        for (int i = 0; i < values.length; i++) {

            speed = values[i];

            System.out.println(values[i]);
            Button.waitForPress();



            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                mov.forward(speed, time);
                Utils.Sleep(3000);

            }
        }
    }

    public void testRotateSpeeds() {
        int speed;
        int angle = 45;
        int factor = 2000;


        int[] speeds = new int[]{50, 75, 125, 250, 500, 750, 1000};

        for (int i = 0; i < speeds.length; i++) {

            speed = speeds[i];

            System.out.println(speeds[i]);
            Button.waitForPress();
            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                mov.turn(speed, angle, true);

                mov.TurnOnSpotCCW(angle);
                Utils.Sleep(3000);

            }
        }
    }

    public void testRotateAngles() {
        int speed = 200;
        int angle = 45;


        int[] values = new int[]{15, 30, 45, 60, 90};

        for (int i = 0; i < values.length; i++) {

            angle = values[i];

            System.out.println(values[i]);
            Button.waitForPress();
            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                mov.turn(speed, angle, true);

                mov.TurnOnSpotCCW(angle);
                Utils.Sleep(3000);

            }
        }
    }
}
