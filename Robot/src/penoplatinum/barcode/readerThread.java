package penoplatinum.barcode;

import penoplatinum.movement.*;
import lejos.nxt.*;

/**
 *
 * @author Thomas
 */
public class readerThread extends Thread {

    BarcodeReader codeReader;
    boolean continueThread;
    IMovement move;
    boolean lineFollower = false;
    boolean drivingEnabled;

    public readerThread(BarcodeReader codeReader, Boolean drivingEnabled) {
        this.codeReader = codeReader;
        this.continueThread = true;
        move = new RotationMovement();
        this.drivingEnabled = drivingEnabled;
    }

    public void run() {
        int commando = 1;
        while (continueThread) {
                move.MoveStraight(2, false);
                commando = this.codeReader.read();
                 if(drivingEnabled){
                    driveParcour(commando);
                 }
                 else{
                    move.Stop();
                    System.out.println(""+BarcodeData.getBarcodesString(commando));
                    Button.ENTER.waitForPressAndRelease();
                    Utils.Sleep(1000);
                }
            
        }
    }

    private void driveParcour(int commando) {
        switch (commando) {
            case 0:
                if (lineFollower) {
                    move.TurnOnSpotCCW(60);
                }
                break;
            case 15:
                if (lineFollower) {
                    move.TurnOnSpotCCW(-60);
                }
                break;
            case 1:
                // rechtdoor rijden
                break;
            case 2:
                // helling omhoog
                break;
            case 3:
                move.MoveStraight(0.325, true);
                //Utils.Sleep(200);
                move.TurnOnSpotCCW(90);
                //Utils.Sleep(200);
                break;
            case 4:
                // helling naar beneden
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
