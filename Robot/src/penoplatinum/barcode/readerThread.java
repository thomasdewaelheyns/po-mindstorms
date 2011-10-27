package penoplatinum.barcode;

import penoplatinum.movement.*;

/**
 *
 * @author Thomas
 */
public class readerThread extends Thread {
    BarcodeReader codeReader;
    boolean continueThread;
    IMovement move;
    boolean lineFollower = false;
    
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
            switch(commando){
                case 0:
                    if(lineFollower){move.TurnOnSpotCCW(60);}
                    break;
                case 15:
                    if(lineFollower){move.TurnOnSpotCCW(-60);}
                    break;
                case 1:
                    break;
                case 3:
                    move.MoveStraight(0.325,true);
                    //Utils.Sleep(200);
                    move.TurnOnSpotCCW(90);
                    //Utils.Sleep(200);
                    break;
                case 6:
                    move.MoveStraight(0.325, true);
                    //Utils.Sleep(200);
                    move.TurnOnSpotCCW(-90);
                    //Utils.Sleep(200);
                    break;
                case 9:
                    move.MoveStraight(0.20, true);
                    //Utils.Sleep(200);
                    move.TurnOnSpotCCW(180);
                    //Utils.Sleep(200);
                    break;
                case 12:
                    move.MoveStraight(0.20, true);
                    //Utils.Sleep(200);
                    move.TurnOnSpotCCW(180);
                    //Utils.Sleep(200);
                    break;
                case 14:
                    move.MoveStraight(0.20, true);
                    //Utils.Sleep(200);
                    move.TurnOnSpotCCW(180);
                    //Utils.Sleep(200);
                    break;
        }
       }
    }
    
}
