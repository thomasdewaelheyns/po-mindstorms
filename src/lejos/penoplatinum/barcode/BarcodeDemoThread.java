package penoplatinum.barcode;

import java.io.PrintStream;
import penoplatinum.movement.*;
import lejos.nxt.*;
import penoplatinum.Utils;
import penoplatinum.bluetooth.IConnection;
import penoplatinum.bluetooth.PacketTransporter;
import penoplatinum.sensor.WrappedLightSensor;
import penoplatinum.ui.UIView;

/**
 *
 * @author Thomas
 */
public class BarcodeDemoThread extends Thread {

    BarcodeReader codeReader;
    boolean continueThread;
    RotationMovement movement;
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
            movement.driveDistance(100);
            commando = this.codeReader.read();

            if (drivingEnabled) {
                driveParcour(commando);
            } else {
                movement.stop();
                System.out.println("" + BarcodeData.getBarcodesString(commando));
                Button.ENTER.waitForPressAndRelease();
                Utils.Sleep(1000);
            }

        }

        movement.stop();
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
                    movement.turnCCW(60);
                    movement.waitForMovementComplete();
                }
                Utils.Log("LineFix");
                break;
            case 15:
                if (lineFollower) {
                    movement.turnCCW(-60);
                    movement.waitForMovementComplete();
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
                movement.driveDistance(0.325);
                movement.waitForMovementComplete();
                movement.turnCCW(90);
                movement.waitForMovementComplete();
                break;
            case 4:
                // helling naar beneden
                sendBarcodePacket(UIView.GO_FORWARD);
                break;
            case 6:
                sendBarcodePacket(UIView.GO_RIGHT);
                movement.driveDistance(0.325);
                movement.waitForMovementComplete();
                movement.turnCCW(-90);
                movement.waitForMovementComplete();
                break;

            case 9:
                movement.driveDistance(0.20);
                movement.waitForMovementComplete();
                movement.turnCCW(180);
                movement.waitForMovementComplete();
                Utils.Log("Wrong direction");
                break;
            case 12:
                movement.driveDistance(0.20);
                movement.waitForMovementComplete();
                movement.turnCCW(180);
                movement.waitForMovementComplete();
                Utils.Log("Wrong direction");
                break;
            case 14:
                movement.driveDistance(0.20);
                movement.waitForMovementComplete();
                movement.turnCCW(180);
                movement.waitForMovementComplete();
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
        Utils.Log(UIView.BARCODE + "");
        transporter.SendPacket(UIView.BARCODE);
    }

    /**
     * NOT USED
     * @param conn
     * @param sensor 
     */
    public static void runBarcodeDemo(IConnection conn, WrappedLightSensor sensor) {

        BarcodeDemoThread reader = new BarcodeDemoThread(new BarcodeReader(sensor), true, conn);
        reader.start();
        while (true) {
            int read = Button.readButtons();
            if ((read & 1) != 0) {
                reader.lineFollower = !reader.lineFollower;
                System.out.println("" + reader.lineFollower);
            }
            if ((read & 2) != 0) {
                reader.codeReader.continueWhile = false;
                reader.continueThread = false;
                break;
            }
            while (Button.readButtons() != 0);
        }
    }
}
