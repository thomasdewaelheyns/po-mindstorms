package penoplatinum.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.nxt.Button;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import penoplatinum.Utils;

public class BluetoothTest {

    RobotBluetoothConnection bt = new RobotBluetoothConnection();
    BTConnection conn;
    DataInputStream stri;
    DataOutputStream stro;

    public void printTest() {
        /*while (!bt.connect())
        {
        System.out.println("Connection timed out.");
        }
        try{
        while(true){
        while(bt.str.available()<4){}
        System.out.println(bt.str.readInt());
        }
        } catch(IOException e){
        System.out.println("Connection Lost");
        bt.connect();
        }*/
    }

    public void testInitializeConnection() {
        bt.initializeConnection();
        System.out.println("Success!");
    }

    public void testBasicBluetooth() {

        while (!connect()) {
            Utils.Log("Connection failed, trying again");
            Utils.Sleep(1000);
        }
        // Connected to NXJ, perform packet ID synchronization here

        while (true) {
            try {
                while (stri.available() == 0) {
                    Utils.Sleep(20);
                }

                System.out.println(stri.readInt());

            } catch (IOException ex) {
                Logger.getLogger(BluetoothTest.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    private boolean connect() {
        System.out.println("Connecting.");
        BTConnection conn = Bluetooth.waitForConnection(5000, NXTConnection.PACKET);
        stri = conn.openDataInputStream();
        stro = conn.openDataOutputStream();
        System.out.println("Connected: " + conn.getAddress());
        return true;
    }

    public void testBluetoothButtonSend() {
        int buttonPacketId = 5988695;

        RobotBluetoothConnection conn = new RobotBluetoothConnection();

        PacketTransporter t = new PacketTransporter(conn);

        conn.RegisterTransporter(t, buttonPacketId);

        conn.initializeConnection();

        while (!Button.ESCAPE.isPressed()) {
            if (Button.ENTER.isPressed()) {

                try {
                    t.getSendStream().writeDouble(3.14159265358979);
                    t.SendPacket(buttonPacketId);
                    Utils.Log("SendButton");
                } catch (IOException ex) {
                    Utils.Log("IO EXCEPTION!");
                }
            }

            Utils.Sleep(500);

        }

    }
}
