package penoplatinum.bluetooth;

import java.io.IOException;


public class BluetoothTest {
    BluetoothCommunicatorRobot bt=new BluetoothCommunicatorRobot();

    public void printTest(){
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
    
    public void testInitializeConnection()
    {
        bt.initializeConnection();
        System.out.println("Success!");
    }

}
