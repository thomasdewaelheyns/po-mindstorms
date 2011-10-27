/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.barcode;

import lejos.nxt.*;
import penoplatinum.movement.IMovement;
import penoplatinum.movement.RotationMovement;
import penoplatinum.movement.Utils;

/**
 *
 * @author Thomas
 */
public class Main {
    public static void main(String[] Args){
        LightSensorRobot sensor = new LightSensorRobot(SensorPort.S1);
        
        sensor.calibrate();
        readerThread reader = new readerThread(new BarcodeReader(sensor));
        reader.start();
        boolean Unlimited = true;
        while(Unlimited){
        if(Unlimited == false){ Unlimited = false;}
       }
        reader.codeReader.continueWhile = false;
        reader.continueThread = false;
        
    }
    
    
}
