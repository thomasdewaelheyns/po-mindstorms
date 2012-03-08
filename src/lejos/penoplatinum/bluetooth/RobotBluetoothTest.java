package penoplatinum.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.nxt.Button;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import penoplatinum.Utils;

public class RobotBluetoothTest {
    
    RobotBluetoothConnection bt = new RobotBluetoothConnection();
    BTConnection conn;
    DataInputStream stri;
    DataOutputStream stro;
    
    public void testInitializeConnection() {
        bt.initializeConnection();
        Utils.Log("Success!");
    }
    
    public void testBasicBluetooth() {
        
        while (!connect()) {
            Utils.Log("Connection failed, trying again");
            Utils.Sleep(1000);
        }
        // Connected to NXJ, perform packet ID synchronization here

        while (true) {
            try {
                Utils.Log(stri.readInt() + "");
                
                stro.writeInt(34);
                stro.flush();
                Utils.Log("Packet sent!");
                
            } catch (IOException ex) {
                Utils.Log("AIJOOOOO");
                if (ex.getMessage() != null) {
                    Utils.Log(ex.getMessage());
                }
            }
        }
    }
    
    private boolean connect() {
        Utils.Log("Connecting.");
        BTConnection conn = Bluetooth.waitForConnection(5000, NXTConnection.PACKET);
        if (conn == null) {
            return false;
        }
        stri = conn.openDataInputStream();
        stro = conn.openDataOutputStream();
        Utils.Log("Connected: " + conn.getAddress());
        return true;
    }
    
    public void testBluetoothButtonSend() {
        int buttonPacketId = 5988695;
        
        RobotBluetoothConnection conn = new RobotBluetoothConnection();
        
        QueuedPacketTransporter t = new QueuedPacketTransporter(conn);
        
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
    
    public void testSendSpeed() throws IOException {
        RobotBluetoothConnection conn = new RobotBluetoothConnection();
        conn.initializeConnection();
        
        penoplatinum.bluetooth.BluetoothPerformanceTests test = new BluetoothPerformanceTests();
        test.testSendSpeed_Receive(conn);
        
    }
    
    public void testBluetoothLogging() throws IOException {
        
        RobotBluetoothConnection conn = new RobotBluetoothConnection();
        conn.initializeConnection();
        Utils.EnableRemoteLogging(conn);
        Utils.Log("Success!");
        
        Button.waitForPress();
        
    }
}
