import lejos.nxt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Veelhoek {

  public static void main(String [] args) {
  System.out.println("Hello Ruben");
	//AngleMovement a = new AngleMovement();
	//a.Execute();
    //veelhoek(3 , 1000);
  }

  static void veelhoek(int number, int time){
    int angle = 180 * (number-2) / number;
    for(int i=0;i<number;i++){
      forward(time);
      try { Thread.sleep(1000); } catch(Exception e) {}
      turnRotations(angle);
      try { Thread.sleep(1000); } catch(Exception e) {}
    }
  }

  static void forward(int time){
    int speed=720;
    Motor.B.setSpeed(speed);
    Motor.C.setSpeed(speed);
    Motor.B.forward();
    Motor.C.forward();
    try { Thread.sleep(time); } catch(Exception e) {}
  }

  static void turn(int angle){
    int speed=360;
    int sleep=1500*angle/360;
    Motor.B.setSpeed(speed);
    Motor.C.setSpeed(speed);
    Motor.B.forward();
    Motor.C.backward();
    try { Thread.sleep(sleep); } catch(Exception e) {}
  }
  
  static void turnRotations(int angle){
	int speed = 360;
	double lengthAxis = 11;
	double lengthWheel = 17.5;
	double circumference = Math.PI*(lengthAxis);
	double toTravel = (angle*circumference)/360;
	int amountOfRotations = (int) ((toTravel*360)/lengthWheel);
	Motor.B.setSpeed(speed);
	Motor.C.setSpeed(speed);
	System.out.print(amountOfRotations);
	Motor.B.rotate(amountOfRotations, true);
	Motor.C.rotate(-amountOfRotations, true);
	try { Thread.sleep(3000); } catch(Exception e) {}
  }
  
}
