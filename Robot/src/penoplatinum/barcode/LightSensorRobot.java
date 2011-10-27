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
    public static int BlackBrownBorder = 30;
    public static int BrownWhiteBorder = 90;

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
        
        System.out.println("Zet de sensor op zwart en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        int black = sensor.readValue();
        Sound.beep();
        Utils.Sleep(1000);
        
        System.out.println("Zet de sensor op bruin en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(sensor.readValue(), 3, 0);
        int brown = sensor.readValue();
        Sound.beep();
        Utils.Sleep(1000);
        
        BlackBrownBorder = (int)((black+brown)/2);
        BrownWhiteBorder = (int)((brown+white)/2);
    }
    
    public static boolean isBrown(int value) {
        return (BlackBrownBorder < value) && (value < BrownWhiteBorder);
    }
    public static int isColor(int value) {
        if (value < ((BlackBrownBorder + BrownWhiteBorder) / 2)) {
            return 0;
        } else {
            return 1;
        }
    }
    public static boolean isBlack(int value){
        return BlackBrownBorder > value;
    }
}
