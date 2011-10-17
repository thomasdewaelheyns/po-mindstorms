package penoplatinum.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import penoplatinum.movement.Utils;

/**
 * Responsible for sending and receiving bluetooth packets on the PC
 * 
 */
public class BluetoothCommunicatorRobot {

    BTConnection conn;
    DataInputStream str;
    DataOutputStream stro;
    //private HashMap<Integer, BluetoothPacketTransporter> listenerMap = new HashMap<Integer, BluetoothPacketTransporter>();

    public BluetoothCommunicatorRobot() {
    }

    public void initializeConnection() {
        while (!connect()) {
            Utils.Log("Connection failed, trying again");
            Utils.Sleep(1000);
        }
        // Connected to NXJ, perform packet ID synchronization here

    }

    /*public BluetoothPacketTransporter RegisterTransporter(int packetIdentifier) {
        BluetoothPacketTransporter l = new BluetoothPacketTransporter();
        if (listenerMap.containsKey(packetIdentifier))
            throw new RuntimeException("A listener has already been created with given packetIdentifer");
        
        listenerMap.put(packetIdentifier, l);
        
        return l;
    }*/

    private boolean connect() {
        System.out.println("Connecting.");
        conn = Bluetooth.waitForConnection(5000, NXTConnection.PACKET);
        if (conn == null) {
            str = null;
            stro = null;

            return false;
        }
        str = conn.openDataInputStream();
        stro = conn.openDataOutputStream();
        System.out.println("Connected: " + conn.getAddress());
        return true;
    }

    private int readInt() throws IOException {
        return str.readInt();
    }
}
