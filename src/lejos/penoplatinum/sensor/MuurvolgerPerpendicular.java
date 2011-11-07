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
    private final int cogMultiplier = 1;
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

        int startTacho = forwardTacho + rotateStart;
        int endTacho = forwardTacho + rotateEnd;

        verticalMotor.setSpeed(100);
        verticalMotor.rotateTo(startTacho, false);
        while (cont) {

            verticalMotor.rotateTo(endTacho,true);
            while (verticalMotor.isMoving()) {
                updateMinimum();
            }
            correctAngle();
            clearMinimum();
            movement.MoveStraight(10, false);

            verticalMotor.rotateTo(startTacho, true);
            while (verticalMotor.isMoving()) {
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

    private void updateMinimum() {
        int dist = ultra.getDistance();
        int tacho = verticalMotor.getTachoCount();


        if (dist < minDistance) {
            minDistance = dist;
            minTacho = tacho;
        }
        if (dist == minDistance) {
            maxTacho = tacho;
        }



        float angle = tacho - forwardTacho;
        sendSonarPacket(-angle, dist);


    }

    private void sendSonarPacket(float angle, int distance) {
        printStream.println((int) (angle / cogMultiplier) + "," + distance);
        endpoint.SendPacket(UIView.SONAR);
    }

    private void correctAngle() {
        int targetAngle = 90;

        if (minDistance < 20) {
            Utils.Log("To close");
            targetAngle += 20;
        } else if (minDistance > 40) {
            Utils.Log("Too far");
            targetAngle -= 20;
//        } else if (minDistance < 10) {
//            Utils.Log("Against wall");
//            targetAngle -= 180;
        } else {
            Utils.Log("Normal");
        }
        Utils.Log(minDistance + "");

        int correctedMinTacho = (minTacho + maxTacho) / 2;

        //Normalize (0 is forwardTacho)
        correctedMinTacho -= forwardTacho;
        Utils.Log(correctedMinTacho + "");
        int correction = targetAngle - correctedMinTacho / cogMultiplier; /// change sign back to -
        Utils.Log("Target: " + targetAngle);
        Utils.Log("Correction: " + correction);

        movement.TurnOnSpotCCW(correction);
    }

    private void clearMinimum() {
        minTacho = Integer.MIN_VALUE;
        maxTacho = Integer.MIN_VALUE;
        minDistance = Integer.MAX_VALUE;
    }

    private void rotateSonar(int angle, boolean immediateReturn) {
        verticalMotor.rotate(angle / cogMultiplier, immediateReturn);
    }
}
