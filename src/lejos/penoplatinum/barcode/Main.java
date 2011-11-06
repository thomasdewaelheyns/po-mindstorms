/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.barcode;

import lejos.nxt.*;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.movement.RotationMovement;

/**
 *
 * @author Thomas
 */
public class Main {

    public static void main(String[] Args) {

        RobotBluetoothConnection conn = new RobotBluetoothConnection();
        conn.initializeConnection();
        RotationMovement m = new RotationMovement();
        LightSensorRobot sensor = new LightSensorRobot(SensorPort.S4);
        sensor.calibrate();

        //CalibratieTurnOnSpot c = new CalibratieTurnOnSpot(sensor, m);
        //c.run();
        //Button.waitForPress();

        BarcodeDemoThread reader = new BarcodeDemoThread(new BarcodeReader(sensor), true, conn);
        reader.start();
        while (true) {
            int read = Button.readButtons();
            if ((read & 1) != 0) {
                reader.lineFollower = !reader.lineFollower;
                System.out.println("" + reader.lineFollower);
            }
            if ((read & 2) != 0) {
                reader.codeReader.continueWhile = false;
                reader.continueThread = false;
                break;
            }
            while (Button.readButtons() != 0);
        }

    }
}
