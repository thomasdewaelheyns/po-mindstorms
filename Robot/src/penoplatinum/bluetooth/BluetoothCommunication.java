package penoplatinum.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class BluetoothCommunication {
    BTConnection conn;
    DataInputStream str;
    DataOutputStream stro;
    
    public BluetoothCommunication() {
    }
    public boolean connect() {
        System.out.println("Connecting.");
        conn = Bluetooth.waitForConnection(5000, NXTConnection.RAW);
        if(conn==null){
            return false;
        }
        str = conn.openDataInputStream();
        stro = conn.openDataOutputStream();
        System.out.println("Connected: "+conn.getAddress());
        return true;
    }
    public int readInt() throws IOException{
        return str.readInt();
    }

}
