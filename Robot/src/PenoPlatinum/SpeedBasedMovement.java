package PenoPlatinum;

import lejos.nxt.*;

public class SpeedBasedMovement implements IMovement {
        
        static final float FORWARD_DEFAULT_SPEED = 0.10f;
        static final float turn_DEFAULT_SPEED = 0.05f;
	static final NXTRegulatedMotor MotorLeft = Motor.B;
	static final NXTRegulatedMotor MotorRight = Motor.C;
	public static void main(String [] args) {
		veelhoek(3 , 1000);
	}
	static void veelhoek(int number, int time){
		int angle=180*(number-2)/number;
		for(int i=0;i<number;i++){
			forward(time);
			wait(500);
			turn(angle);
			wait(500);
		}
	}
        static void forward(float time){
            forward(FORWARD_DEFAULT_SPEED, time);
        }
        
	static void forward(float speed, float time){
                float motorSpeed = speed*2070f;
                MotorLeft.setSpeed(motorSpeed);
		MotorRight.setSpeed(motorSpeed);
		MotorLeft.forward();
		MotorRight.forward();
		try{
			Thread.sleep((int)time);
		} catch(Exception e){}
	}
        static void turn(float angle){
            turn(turn_DEFAULT_SPEED ,angle,true);
        }
	static void turn(float speed, float angle, boolean regulate){
		wait(150);
                if(!regulate){
                   MotorLeft.suspendRegulation();
                   MotorRight.suspendRegulation();
                }
                float motorSpeed = speed*2070;
		int sleep=(int) (1500*angle/360);
		MotorLeft.setSpeed(motorSpeed);
		MotorRight.setSpeed(motorSpeed);
		MotorLeft.forward();
		MotorRight.backward();
		try{
			Thread.sleep(sleep);
		} catch(Exception e){}
	}
        
        static void wait(int time)
        {
            MotorLeft.stop();
            MotorRight.stop();
            try{
			Thread.sleep(time);
		} catch(Exception e){}
        }

    public void MoveStraight(float distance) {
        
    }

    public void TurnOnSpotCCW(float angle) {
        
    }

    public void TurnAroundWheel(double angle, boolean isLeft) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void MoveStraight(double distance) {
        forward((float)distance/FORWARD_DEFAULT_SPEED);
    }

    public void TurnOnSpotCCW(double angle) {
        turn((float)angle);
    }

	
}
