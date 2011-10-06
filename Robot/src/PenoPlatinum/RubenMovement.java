package PenoPlatinum;

import lejos.nxt.*;

public class RubenMovement implements IMovement{
    public static final int SPEEDFORWARD=400;
    public static final int SPEEDTURN=400;
    public static final int WIELOMTREK=175; //mm
    public static final int WIELAFSTAND=56;//mm
    public static final int SPEED=350;
    public Motor motorLeft=Motor.C;
    public Motor motorRight=Motor.B;
    
    public void MoveStraight(double distance) {
        int r=(int) (distance*360/WIELOMTREK);
        motorLeft.setSpeed(SPEEDFORWARD);
        motorRight.setSpeed(SPEEDFORWARD);
        motorLeft.rotate(r, true);
        motorRight.rotate(r, true);
        try{Thread.sleep(r*1500/SPEEDFORWARD);} catch(Exception e){}
    }

    public void TurnOnSpotCCW(double angle) {
        int h=(int) (angle*2*Math.PI*WIELAFSTAND/WIELOMTREK);
        motorLeft.rotate(h, true);
        motorRight.rotate(-h, true);
        try{Thread.sleep(h*1500/SPEEDTURN);} catch(Exception e){}
    }

    public void TurnAroundWheel(double angle, boolean isLeft) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
