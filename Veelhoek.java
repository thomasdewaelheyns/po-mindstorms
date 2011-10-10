import lejos.nxt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Veelhoek {

  public static void main(String [] args) {
  forward(1000);
  
  //  veelhoek(3 , 1000);
  }

  static void veelhoek(int number, int time){
	
    int angle = 180 * (number-2) / number;
    for(int i=0;i<number;i++){
      forward(time);
      try { Thread.sleep(1000); } catch(Exception e) {}
      turn(angle);
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
}
