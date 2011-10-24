/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.barcode;

import lejos.nxt.SensorPort;
import penoplatinum.movement.*;

/**
 *
 * @author Thomas
 */
public class readerThread extends Thread {
    BarcodeReader codeReader;
    boolean continueThread;
    public readerThread(BarcodeReader codeReader){
        this.codeReader = codeReader;
        this.continueThread = true;
    }
    
    public void run(){
        while(continueThread){
            switch(this.codeReader.read()){
            case 0:
                
            case 1:
            case 2:
            case 3:
                IMovement move = new RotationMovement();
                move.MoveStraight(0.44);
                move.TurnOnSpotCCW(90);
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
        }
       }
    }
    
}
