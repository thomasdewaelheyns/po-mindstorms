package penoplatinum.barcode;

import penoplatinum.movement.*;
import lejos.nxt.*;
import penoplatinum.Utils;
import penoplatinum.ui.UIView;

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
        
        int uiViewDirection = -1;
        
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
                uiViewDirection = UIView.GO_FORWARD;
                // rechtdoor rijden
                break;
            case 2:
                uiViewDirection = UIView.GO_FORWARD;
                // helling omhoog
                break;
            case 3:
                move.MoveStraight(0.325, true);
                move.TurnOnSpotCCW(90);
                uiViewDirection = UIView.GO_RIGHT;
                
                break;
            case 4:
                // helling naar beneden
                uiViewDirection = UIView.GO_FORWARD;
                break;
            case 6:
                move.MoveStraight(0.325, true);
                move.TurnOnSpotCCW(-90);
                break;
            case 9:
                move.MoveStraight(0.20, true);
                move.TurnOnSpotCCW(180);
                break;
            case 12:
                move.MoveStraight(0.20, true);
                move.TurnOnSpotCCW(180);
                break;
            case 14:
                move.MoveStraight(0.20, true);
                move.TurnOnSpotCCW(180);
                break;
        }
    }
}
