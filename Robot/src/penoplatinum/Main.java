package penoplatinum;

import lejos.nxt.*;
import penoplatinum.movement.IMovement;
import penoplatinum.movement.RotationMovement;
import penoplatinum.movement.RotationMovementTest;
import penoplatinum.sensor.BarcodeRuben;
import penoplatinum.sensor.LijnVolgerRuben;
import penoplatinum.sensor.MuurVolgerTest;

public class Main {
    public static UltrasonicSensor muurSensor = new UltrasonicSensor(SensorPort.S3);
    public static LightSensor light = new LightSensor(SensorPort.S1);
    public static Motor l = Motor.C;
    public static Motor r = Motor.B;
    public static Motor movingSonar = Motor.A;
    public static IMovement mov = new RotationMovement();
   
    public static void main(String[] args){
        mov.MoveStraight(10, false);
        MuurVolgerTest.distanceTest(muurSensor);

        //BarcodeRuben b = new BarcodeRuben(light, mov);
        //b.run();
        //MuurVolgerTest.test(muurSensor, mov, movingSonar);
        //LijnVolgerRuben r = new LijnVolgerRuben(mov, light);
        //r.calibrate();
        //r.run();

    }


}
