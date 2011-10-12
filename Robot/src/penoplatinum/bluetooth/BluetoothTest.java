package penoplatinum.bluetooth;

import java.io.IOException;
import lejos.nxt.*;

public class BluetoothTest {
    BluetoothCommunication bt=new BluetoothCommunication();

    public void printTest(){
        bt.connect();
        try{
            while(true){
                while(bt.str.available()<4){}
                System.out.println(bt.str.readInt());
            }
        } catch(IOException e){
            System.out.println("Connection Lost");
            bt.connect();
        }
    }

}
