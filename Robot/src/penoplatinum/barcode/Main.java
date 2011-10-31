/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.barcode;

import lejos.nxt.*;
import penoplatinum.movement.RotationMovement;
import penoplatinum.sensor.CalibratieTurnOnSpot;

/**
 *
 * @author Thomas
 */
public class Main {

    public static void main(String[] Args) {
        RotationMovement m = new RotationMovement();
        LightSensorRobot sensor = new LightSensorRobot(SensorPort.S1);
        sensor.calibrate();

        CalibratieTurnOnSpot c = new CalibratieTurnOnSpot(sensor, m);
        //c.run();
        //Button.waitForPress();

        readerThread reader = new readerThread(new BarcodeReader(sensor),false);
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
            while(Button.readButtons()!=0);
        }

    }
}
