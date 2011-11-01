package penoplatinum.sensor;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.movement.IMovement;

public class MuurVolgerTest {

    public static void test(UltrasonicSensor muurSensor, IMovement mov, Motor mot){
        Muurvolger m=new Muurvolger(muurSensor, mov, mot);
        m.run();
    }

    public static void distanceTest(UltrasonicSensor sens){
        int[] rotates = new int[]{5,5,5,15,15,15,30};
        int pos = 0;
        while(true){
            if(Button.readButtons() == 1){
                Motor.A.rotate(5);
                while(Button.readButtons() != 0);
            }
            LCD.drawString(sens.getDistance()+"   ", 0, 0);
        }
    }
}
