package penoplatinum.movement;

import lejos.nxt.Button;
import penoplatinum.Utils;

public class TimeMovementTest {

    private TimeMovement movement;

    public TimeMovementTest() {
        movement = new TimeMovement();
    }

    public void testForwardTime() {
        float speed = 250;
        float time = 2;

        float[] values = new float[]{0.5f, 1f, 1.5f, 2.0f};

        for (int i = 0; i < values.length; i++) {
            time = values[i];

            System.out.println(values[i]);
            Button.waitForPress();

            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                movement.forward(speed, time);
                Utils.Sleep(3000);

            }
        }
    }

    public void testForwardSpeed() {
        float time = 2;
        int factor = 2000;
        int[] values = new int[]{125, 250, 500, 750, 1000};

        for (int i = 0; i < values.length; i++) {
            int speed = values[i];

            System.out.println(speed);
            Button.waitForPress();

            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                movement.forward(speed, time);
                Utils.Sleep(3000);
            }
        }
    }

    public void testRotateSpeeds() {
        int angle = 45;
        int factor = 2000;
        int[] speeds = new int[]{50, 75, 125, 250, 500, 750, 1000};
        
        for (int i = 0; i < speeds.length; i++) {
            int speed = speeds[i];

            System.out.println(speeds[i]);
            Button.waitForPress();
            
            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                movement.turn(speed, angle, true);
                movement.TurnOnSpotCCW(angle);
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
                movement.turn(speed, angle, true);
                movement.TurnOnSpotCCW(angle);
                Utils.Sleep(3000);
            }
        }
    }
}
