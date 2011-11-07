package penoplatinum.barcode;

import java.io.PrintStream;
import penoplatinum.movement.*;
import lejos.nxt.*;
import penoplatinum.Utils;
import penoplatinum.bluetooth.IConnection;
import penoplatinum.bluetooth.PacketTransporter;
import penoplatinum.ui.UIView;

/**
 *
 * @author Thomas
 */
public class BarcodeDemoThread extends Thread {
    
    BarcodeReader codeReader;
    boolean continueThread;
    IMovement movement;
    boolean lineFollower = false;
    boolean drivingEnabled;
    private PacketTransporter transporter;
    private PrintStream printStream;
    
    public BarcodeDemoThread(BarcodeReader codeReader, Boolean drivingEnabled, IConnection connection) {
        this(codeReader, drivingEnabled);
        
        transporter = new PacketTransporter(connection);
        connection.RegisterTransporter(transporter, UIView.BARCODE);
        printStream = new PrintStream(transporter.getSendStream());
        
    }
    
    public BarcodeDemoThread(BarcodeReader codeReader, Boolean drivingEnabled) {
        this.codeReader = codeReader;
        this.continueThread = true;
        final RotationMovement temp = new RotationMovement();
        temp.SPEEDFORWARD = 400;
        movement = temp;
        
        this.drivingEnabled = drivingEnabled;
    }
    
    public void run() {
        int commando = 1;
        while (continueThread) {
            movement.MoveStraight(100, false);
            commando = this.codeReader.read();
            
            if (drivingEnabled) {
                driveParcour(commando);
            } else {
                movement.Stop();
                System.out.println("" + BarcodeData.getBarcodesString(commando));
                Button.ENTER.waitForPressAndRelease();
                Utils.Sleep(1000);
            }
            
        }
        
        movement.Stop();
    }
    
    public void stopLoop() {
        continueThread = false;
        codeReader.stopLoop();
    }
    
    private void driveParcour(int commando) {
        
        int uiViewDirection = -1;
        
        switch (commando) {
            case 0:
                if (lineFollower) {
                    movement.TurnOnSpotCCW(60);
                }
                Utils.Log("LineFix");
                break;
            case 15:
                if (lineFollower) {
                    movement.TurnOnSpotCCW(-60);
                }
                Utils.Log("LineFix");
                break;
            case 1:
                sendBarcodePacket(UIView.GO_FORWARD);
                // rechtdoor rijden

                break;
            case 2:
                sendBarcodePacket(UIView.GO_FORWARD);
                // helling omhoog
                break;
            case 3:
                sendBarcodePacket(UIView.GO_LEFT);
                movement.MoveStraight(0.325, true);
                movement.TurnOnSpotCCW(90);
                break;
            case 4:
                // helling naar beneden
                sendBarcodePacket(UIView.GO_FORWARD);
                break;
            case 6:
                sendBarcodePacket(UIView.GO_RIGHT);
                movement.MoveStraight(0.325, true);
                movement.TurnOnSpotCCW(-90);
                break;
            
            case 9:
                movement.MoveStraight(0.20, true);
                movement.TurnOnSpotCCW(180);
                Utils.Log("Wrong direction");
                break;
            case 12:
                movement.MoveStraight(0.20, true);
                movement.TurnOnSpotCCW(180);
                Utils.Log("Wrong direction");
                break;
            case 14:
                movement.MoveStraight(0.20, true);
                movement.TurnOnSpotCCW(180);
                Utils.Log("Wrong direction");
                break;
            
            default:
                Utils.Log("Unknown barcode: " + commando);
                break;
        }
        
        
    }
    
    private void sendBarcodePacket(int direction) {
        if (printStream == null) {
            return;
        }
        Utils.Log("SendBarcode");
        printStream.print(codeReader.getLastRawRead());
        printStream.print(",");
        printStream.println(direction);
        Utils.Log(UIView.BARCODE+"");
        transporter.SendPacket(UIView.BARCODE);
    }
}
