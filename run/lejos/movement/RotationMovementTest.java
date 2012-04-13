package penoplatinum.movement;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import penoplatinum.util.Utils;

public class RotationMovementTest {

    public void testForwardDistances() {
        double[] distances = new double[]{0.20, 0.40, 0.60, 0.80};
        RotationMovement mov = new RotationMovement(Motor.B, Motor.C);
        mov.SPEEDFORWARD = 500;
        for (int i = 0; i < distances.length; i++) {
            System.out.println(distances[i]);
            Button.waitForPress();
            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                mov.driveDistance(distances[i]);
                Utils.Sleep(3000);

            }
        }
    }

    public void testForwardSpeeds() {
        double distance = 0.20;

        int[] speeds = new int[]{125, 250, 500, 750, 1000};
        RotationMovement mov = new RotationMovement(Motor.B, Motor.C);

        for (int i = 0; i < speeds.length; i++) {
            System.out.println(speeds[i]);
            Button.waitForPress();
            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                mov.SPEEDFORWARD = speeds[i];
                mov.driveDistance(distance);
                Utils.Sleep(3000);

            }
        }
    }

    public void testRotateAngles() {
        int speed = 200;

        int[] angles = new int[]{15, 30, 45, 60, 90};
        RotationMovement mov = new RotationMovement(Motor.B, Motor.C);
        mov.SPEEDTURN = speed;

        for (int i = 0; i < angles.length; i++) {
            int a = angles[i];

            System.out.println(angles[i]);
            Button.waitForPress();
            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                mov.turnAngle(a);
                Utils.Sleep(3000);
            }
        }
    }

    public void testRotateSpeeds() {
        int angle = 45;// TODO

        int[] speeds = new int[]{50, 75, 125, 250, 500, 750, 1000};
        RotationMovement mov = new RotationMovement(Motor.B, Motor.C);

        for (int i = 0; i < speeds.length; i++) {
            int speed = speeds[i];

            System.out.println(speeds[i]);
            Button.waitForPress();
            Utils.Sleep(1000);
            for (int j = 0; j < 5; j++) {
                mov.SPEEDTURN = speed;
                mov.turnAngle(angle);
                Utils.Sleep(3000);
            }
        }
    }
}
