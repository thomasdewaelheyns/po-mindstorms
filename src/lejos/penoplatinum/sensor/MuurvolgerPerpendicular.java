package penoplatinum.sensor;

import java.io.PrintStream;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.Utils;
import penoplatinum.bluetooth.IConnection;
import penoplatinum.bluetooth.PacketTransporter;
import penoplatinum.movement.IMovement;
import penoplatinum.ui.UIView;

public class MuurvolgerPerpendicular {

    private UltrasonicSensor ultra;
    public IMovement movement;
    private Motor verticalMotor;
    public boolean cont = true;
    private int minTacho = Integer.MIN_VALUE;
    private int maxTacho = Integer.MIN_VALUE;
    private int minDistance = Integer.MAX_VALUE;
    private int forwardTacho = Integer.MIN_VALUE;
    private PrintStream printStream;
    PacketTransporter endpoint;

    public MuurvolgerPerpendicular(UltrasonicSensor ultra, IMovement m, Motor v, IConnection connection) {
        this.ultra = ultra;
        movement = m;
        verticalMotor = v;
        endpoint = new PacketTransporter(connection);
        connection.RegisterTransporter(endpoint, UIView.SONAR);

        printStream = new PrintStream(endpoint.getSendStream());


    }

    public void run() {
        int rotateStart = -10;
        int rotateEnd = 145;

        forwardTacho = verticalMotor.getTachoCount();
        Motor.A.setSpeed(250);
        Motor.A.rotate(rotateStart);
        while (cont) {
            Motor.A.rotate(rotateEnd - rotateStart, true);
            while (Motor.A.isMoving()) {
                updateMinimum();
            }
            //correctAngle();
            //clearMinimum();
            //movement.MoveStraight(10, false);

            Motor.A.rotate(rotateStart - rotateEnd, true);
            while (Motor.A.isMoving()) {
                updateMinimum();
            }
            correctAngle();
            clearMinimum();
            movement.MoveStraight(10, false);

            if (Button.ENTER.isPressed()) {
                Button.LEFT.waitForPressAndRelease();
            }
        }
    }
    private int[] robotCorrections = new int[]{0, 0, 0, 5, 20, 5, 0, 0, 0};
    private float correctionResolution = 45 / 2f;

    private void updateMinimum() {
        UltrasonicSensor sens = ultra;

        Motor m = verticalMotor;

        int dist = sens.getDistance();
        int tacho = m.getTachoCount();


        // Correct the distance to distances from the robot

        float angle = tacho - forwardTacho;

        float factor = 90 - angle;
        factor /= correctionResolution;
        if (factor < 0) {
            factor = 0;
        }
        if (factor >= robotCorrections.length - 1) {
            factor = robotCorrections.length - 1 - 0.0001f;
        }

        int start = (int) factor;
        float lerp = factor - start;

        //Utils.Log("angle: " + angle);
        //Utils.Log("Factor: " + factor);

        /*if (angle < 90 && angle > -90) {
        //Utils.Log("Correction: " + correction);
        Utils.Log("WEE: " + correction);
        //Utils.Log(dist + "");
        }*/





        //correction = 0;

        //dist -= correction;

        sendSonarPacket(angle, dist);

        //Utils.Log("C: " + correction + "A: " + angle + "D: " + dist);

        if (dist < minDistance) {
            minDistance = dist;
            minTacho = tacho;
        }
        if (dist == minDistance) {
            maxTacho = tacho;
        }

    }

    private void sendSonarPacket(float angle, int distance) {
        printStream.println(angle + "," + distance);
        endpoint.SendPacket(UIView.SONAR);
    }

    private void correctAngle() {
        int targetAngle = 90;

        if (minDistance < 20) {
            Utils.Log("To close");
            targetAngle += 20;
        } else if (minDistance > 50) {
            Utils.Log("Too far");
            targetAngle -= 20;
        } else {
            Utils.Log("Normal");
        }

        Utils.Log(minDistance + "");
        if (minDistance > 80) {
            return;
        }
        Motor m = Motor.A;
        int correctedMinTacho = (minTacho + maxTacho) / 2;

        //Normalize (0 is forwardTacho)
        correctedMinTacho -= forwardTacho;
        Utils.Log(correctedMinTacho + "");
        final int correction = -(targetAngle - correctedMinTacho);
        Utils.Log("Target: " + targetAngle);
        Utils.Log("Correction: " + correction);

        movement.TurnOnSpotCCW(correction);
    }

    private void clearMinimum() {
        minTacho = Integer.MIN_VALUE;
        maxTacho = Integer.MIN_VALUE;
        minDistance = Integer.MAX_VALUE;
    }
}
