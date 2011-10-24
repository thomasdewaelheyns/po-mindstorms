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
    IMovement move;
    public readerThread(BarcodeReader codeReader){
        this.codeReader = codeReader;
        this.continueThread = true;
        move = new RotationMovement();
    }
    
    public void run(){
        int commando = 1;
        while(continueThread){
            move.MoveStraight(2, false);
            commando = this.codeReader.read();
            System.out.println(""+commando);
            switch(commando){
                case 1:
                    break;
                case 3:
                    move.MoveStraight(0.25,true);
                    move.TurnOnSpotCCW(90);
                    break;
                case 6:
                    move.MoveStraight(0.25, true);
                    move.TurnOnSpotCCW(-90);
                    break;
                case 9:
                    move.TurnOnSpotCCW(180);
                    break;
                case 12:
                    move.TurnOnSpotCCW(180);
                    break;
                case 15:
                    move.TurnOnSpotCCW(180);
                    break;
        }
       }
    }
    
}
