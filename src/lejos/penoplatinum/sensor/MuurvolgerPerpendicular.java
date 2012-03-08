package penoplatinum.sensor;

import java.io.PrintStream;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.Utils;
import penoplatinum.bluetooth.IConnection;
import penoplatinum.bluetooth.QueuedPacketTransporter;
import penoplatinum.movement.RotationMovement;
import penoplatinum.ui.UIView;

public class MuurvolgerPerpendicular {

    private UltrasonicSensor ultra;
    public RotationMovement movement;
    private Motor verticalMotor;
    public boolean cont = true;
    private int minTacho = Integer.MIN_VALUE;
    private int maxTacho = Integer.MIN_VALUE;
    private int minDistance = Integer.MAX_VALUE;
    private int forwardTacho = Integer.MIN_VALUE;
    private final int cogMultiplier = 1;
    private PrintStream printStream;
    QueuedPacketTransporter endpoint;
    private boolean isRight = false;
    private final QueuedPacketTransporter commandTransporter;

    public MuurvolgerPerpendicular(UltrasonicSensor ultra, RotationMovement m, Motor v, IConnection connection,QueuedPacketTransporter commandTransporter) {
        this.ultra = ultra;
        movement = m;
        verticalMotor = v;
        endpoint = new QueuedPacketTransporter(connection);
        connection.RegisterTransporter(endpoint, UIView.SONAR);
        printStream = new PrintStream(endpoint.getSendStream());
        this.commandTransporter = commandTransporter;
    }

    public void run() {
        
        verticalMotor.setSpeed(300);
        
        forwardTacho = verticalMotor.getTachoCount();

        verticalMotor.rotateTo(forwardTacho - 45, false);
        verticalMotor.rotateTo(forwardTacho + 45, true);
        while (verticalMotor.isMoving()) {
            updateMinimum();
        }
        verticalMotor.rotateTo(forwardTacho, false);

        int wallPos = (minTacho + maxTacho) / 2 - forwardTacho;

        isRight = wallPos > 0;









        int rotateStart = 0;
        int rotateEnd = 145;

        if (!isRight) {
            rotateStart = -145;
            rotateEnd = 0;
        }


        int startTacho = forwardTacho + rotateStart;
        int endTacho = forwardTacho + rotateEnd;

        
        verticalMotor.rotateTo(startTacho, false);
        while (commandTransporter.ReceiveAvailablePacket() == -1) {
            Utils.Log("StartTacho" + verticalMotor.getTachoCount() + " : " + endTacho);
            verticalMotor.rotateTo(endTacho, true);
            while (verticalMotor.isMoving()) {
                updateMinimum();
            }
            Utils.Log("EndTacho" + verticalMotor.getTachoCount());
            //correctAngle();
            clearMinimum();
            movement.driveDistance(10);

            verticalMotor.rotateTo(startTacho, true);
            while (verticalMotor.isMoving()) {
                updateMinimum();
            }

            correctAngle();
            clearMinimum();
            movement.driveDistance(10);

            if (Button.ENTER.isPressed()) {
                Button.LEFT.waitForPressAndRelease();
            }
        }
        
        movement.stop();
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

        if (minDistance < 25) { // was changed from 20
            Utils.Log("To close");
            targetAngle += 30;
        } else if (minDistance > 40) {
            Utils.Log("Too far");
            targetAngle -= 20;
//        } else if (minDistance < 10) {
//            Utils.Log("Against wall");
//            targetAngle -= 180;
        } else {
            Utils.Log("Normal");
        }

        if (!isRight) {
            targetAngle = -targetAngle;
        }






        Utils.Log(minDistance + "");

        Utils.Log("ForwardTacho: " + forwardTacho);
        Utils.Log("Tachos: " + minTacho + "," + maxTacho);

        int correctedMinTacho = (minTacho + maxTacho) / 2;

        Utils.Log("Mean: " + correctedMinTacho);


        //Normalize (0 is forwardTacho)
        correctedMinTacho -= forwardTacho;
        Utils.Log(correctedMinTacho + "");



        if (correctedMinTacho < 30) {
            if (minDistance > 30) {

                if (isRight) {
                    movement.turnAngle(-50);
                } else {
                    movement.turnAngle(50);
                }
                return;
            }
        }







        int correction = targetAngle - correctedMinTacho / cogMultiplier; /// change sign back to -
        Utils.Log("Target: " + targetAngle);
        Utils.Log("Correction: " + correction);






        if (Math.abs(correction) < 10) {
            return;
        }

        movement.turnAngle(correction); // TODO : maybe /2
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
