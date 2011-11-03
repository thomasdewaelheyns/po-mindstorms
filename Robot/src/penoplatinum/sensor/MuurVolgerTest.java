package penoplatinum.sensor;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.movement.RotationMovement;

public class MuurVolgerTest {
    
    public static void test() {
        Muurvolger m = new Muurvolger(new UltrasonicSensor(SensorPort.S3), new RotationMovement(), Motor.A);
        m.run();
    }
    
    public static void testPerpendicular() {
        SonarTest test = new SonarTest();
        final UltrasonicSensor sens = new UltrasonicSensor(SensorPort.S3);
        final RotationMovement mov = new RotationMovement();
        test.orientSonarHead(sens, Motor.A);
        Button.waitForPress();
        
        mov.SPEEDFORWARD = 500;
        mov.SPEEDTURN = 120;
        
        MuurvolgerPerpendicular v = new MuurvolgerPerpendicular(sens, mov, Motor.A);
        v.run();
    }
}
