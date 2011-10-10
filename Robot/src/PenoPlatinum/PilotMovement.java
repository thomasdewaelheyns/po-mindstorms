package PenoPlatinum;

import lejos.nxt.*;
import lejos.robotics.proposal.DifferentialPilot;

public class PilotMovement implements IMovement {
	
	public static final int SPEEDFORWARD = 400;
	public static final int SPEEDTURN = 400;
    private Object waitLock = new Object();
    public DifferentialPilot pilot=  DifferentialPilot(175, 56, Motor.C, Motor.B);
   

    public void MoveStraight(double distance) {
        distance *= 1000;
        pilot.setSpeed(SPEEDFORWARD);
        pilot.travel(distance);
    }

    public void TurnOnSpotCCW(double angle) {
    	 pilot.setTurnSpeed(SPEEDTURN);
    	 double hoek = angle*(-1);	// positive angle rotates clockwise
    	 pilot.rotate(hoek.floatValue());
    }

    public void TurnAroundWheel(double angle, boolean isLeft) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
