package penoplatinum.movement;

import lejos.nxt.*;

public class TimeMovement implements IMovement {

    static final float FORWARD_DEFAULT_SPEED = 0.10f;
    static final float turn_DEFAULT_SPEED = 0.05f;
    static final Motor MotorLeft = Motor.B;
    static final Motor MotorRight = Motor.C;

    void forward(float time) {
        forward(FORWARD_DEFAULT_SPEED, time);
    }

    void forward(float speed, float time) {
        int motorSpeed = (int) (speed);
        MotorLeft.setSpeed(motorSpeed);
        MotorRight.setSpeed(motorSpeed);
        MotorLeft.forward();
        MotorRight.forward();
        Utils.Sleep((int)(time * 1000));
        Stop();
    }

    void turn(float angle) {
        turn(turn_DEFAULT_SPEED, angle, true);
    }

    void turn(float speed, float angle, boolean regulate) {
        /*if(!regulate){
        MotorLeft.suspendRegulation();
        MotorRight.suspendRegulation();
        }*/
        int motorSpeed = (int) (speed);
        int sleep = (int) (1500 * angle / 360);
        MotorLeft.setSpeed(motorSpeed);
        MotorRight.setSpeed(motorSpeed);
        MotorLeft.forward();
        MotorRight.backward();
        Utils.Sleep(sleep);
    }

    void wait(int time) {
        MotorLeft.stop();
        MotorRight.stop();
        Utils.Sleep(time);
    }

    public void TurnAroundWheel(double angle, boolean isLeft) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void MoveStraight(double distance) {
        forward((float) distance / FORWARD_DEFAULT_SPEED);
    }

    public void TurnOnSpotCCW(double angle) {
        turn((float) angle);
    }

    public void Stop() {
        MotorLeft.stop();
        MotorRight.stop();
    }
}
