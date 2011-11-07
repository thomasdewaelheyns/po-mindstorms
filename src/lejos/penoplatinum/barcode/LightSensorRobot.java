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
public class LightSensorRobot implements ILightSensor {

    LightSensor sensor;
    public static int BlackBrownBorder = 30;
    public static int BrownWhiteBorder = 90;

    public LightSensorRobot(SensorPort port) {
        sensor = new LightSensor(port, true);
    }

    public byte readValue() {
        return (byte) sensor.readValue();
    }
//
//    public static boolean isBrown(int value) {
//        
//        
//        return (BlackBrownBorder < value) && (value < BrownWhiteBorder);
//    }
//    public static int isColor(int value) {
//        if (value < ((BlackBrownBorder + BrownWhiteBorder) / 2)) {
//            return 0;
//        } else {
//            return 1;
//        }
//    }
//    public static boolean isBlack(int value){
//        return BlackBrownBorder > value;
//    }
}
