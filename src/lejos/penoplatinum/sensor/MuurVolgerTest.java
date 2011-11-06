package penoplatinum.sensor;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.Utils;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.movement.RotationMovement;

public class MuurVolgerTest {

    public static void test() {
        Muurvolger m = new Muurvolger(new UltrasonicSensor(SensorPort.S2), new RotationMovement(), Motor.A);
        m.run();
    }

    public static void testPerpendicular() {

        RobotBluetoothConnection conn = new RobotBluetoothConnection();
        conn.initializeConnection();
        Utils.EnableRemoteLogging(conn);

        SonarTest test = new SonarTest();
        final UltrasonicSensor sens = new UltrasonicSensor(SensorPort.S2);
        final RotationMovement mov = new RotationMovement();
        test.orientSonarHead(sens, Motor.A);
        Button.waitForPress();

        mov.setMovementDisabled(true);

        mov.SPEEDFORWARD = 250;
        mov.SPEEDTURN = 120;

        MuurvolgerPerpendicular v = new MuurvolgerPerpendicular(sens, mov, Motor.A, conn);
        v.run();
    }
}
