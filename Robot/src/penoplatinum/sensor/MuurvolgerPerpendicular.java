package penoplatinum.sensor;

import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.Utils;
import penoplatinum.movement.IMovement;

public class MuurvolgerPerpendicular {

    private UltrasonicSensor ultra;
    public IMovement movement;
    private Motor verticalMotor;
    public boolean cont = true;
    private int minTacho = Integer.MIN_VALUE;
    private int maxTacho = Integer.MIN_VALUE;
    private int minDistance = Integer.MAX_VALUE;
    private int forwardTacho = Integer.MIN_VALUE;

    public MuurvolgerPerpendicular(UltrasonicSensor ultra, IMovement m, Motor v) {
        this.ultra = ultra;
        movement = m;
        verticalMotor = v;
    }

    public void run() {
        int rotateStart = -10;
        int rotateEnd = 145;

        forwardTacho = verticalMotor.getTachoCount();
        Motor.A.setSpeed(80);
        Motor.A.rotate(rotateStart);
        while (cont) {
            Motor.A.rotate(rotateEnd - rotateStart, true);
            while (Motor.A.isMoving()) {
                updateMinimum();
            }
            correctAngle();
            clearMinimum();
            movement.MoveStraight(10, false);

            Motor.A.rotate(rotateStart - rotateEnd, true);
            while (Motor.A.isMoving()) {
                updateMinimum();
            }
            correctAngle();
            clearMinimum();
            movement.MoveStraight(10, false);
        }
    }

    private void updateMinimum() {
        UltrasonicSensor sens = ultra;

        Motor m = Motor.A;

        int dist = sens.getDistance();
        //Utils.Log(dist + "");
        int tacho = m.getTachoCount();
        if (dist < minDistance) {
            minDistance = dist;
            minTacho = tacho;
        }
        if (dist == minDistance) {
            maxTacho = tacho;
        }

    }

    private void correctAngle() {

        int targetAngle = 90;

        if (minDistance < 30) {
            targetAngle +=20;
        } else if (minDistance > 50) {
            targetAngle -=20;
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


        movement.TurnOnSpotCCW(-(targetAngle - correctedMinTacho));
    }

    private void clearMinimum() {
        minTacho = Integer.MIN_VALUE;
        maxTacho = Integer.MIN_VALUE;
        minDistance = Integer.MAX_VALUE;
    }
}