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

   
}
