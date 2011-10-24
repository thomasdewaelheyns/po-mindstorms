/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.barcode;

import lejos.nxt.*;

/**
 *
 * @author Thomas
 */
public class LightSensorRobot implements iLightSensor {

    LightSensor sensor;

    public LightSensorRobot(SensorPort port) {
        sensor = new LightSensor(port, true);
    }

    public byte readValue() {
        return (byte) sensor.readValue();
    }

    public void calibrate() {
        // calibreer de lage waarde.
        System.out.println("Zet de sensor op zwart en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(sensor.readValue(), 1, 0);
        sensor.calibrateLow();
        int black = sensor.readValue();

        Sound.beep();
        Utils.Sleep(1000);

        // calibreer de hoge waarde
        System.out.println("Zet de sensor op wit en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(sensor.readValue(), 3, 0);
        sensor.calibrateHigh();
        int white = sensor.readValue();

        Sound.beep();
        Utils.Sleep(1000);
        
        System.out.println("Zet de sensor op bruin en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(sensor.readValue(), 3, 0);
        int brown = sensor.readValue();
        Sound.beep();
        BarcodeInterpreter.BlackBrownBorder = (int)((black+brown)/2);
        BarcodeInterpreter.BrownWhiteBorder = (int)((brown+white)/2);
        Utils.Sleep(2000);
    }
}
