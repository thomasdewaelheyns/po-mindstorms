package penoplatinum.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class BluetoothCommunication {
    BTConnection conn;
    private DataInputStream str;
    private DataOutputStream stro;
    
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
    public void printTest(){
        connect();
        try{
            while(true){
                while(str.available()<4){}
                System.out.println(str.readInt());
            }
        } catch(IOException e){
            System.out.println("Connection Lost");
            connect();
        }
    }


}
