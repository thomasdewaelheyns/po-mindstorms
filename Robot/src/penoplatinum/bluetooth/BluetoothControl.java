package penoplatinum.bluetooth;

import java.io.IOException;

public class BluetoothControl {
    BluetoothCommunicatorRobot bt=new BluetoothCommunicatorRobot();
    boolean isActive=false;

    public BluetoothControl() {}

    public void control() throws IOException {
        while(bt.stri.available()>4){
            int read=bt.stri.readInt();
            switch(read){
                case 0:
                    isActive=(bt.stri.readInt()==0?false:true);
                    break;
                case 1:
                    motorControl();
                    break;
                default: //resend unknown data
                    bt.stro.writeInt(read+1);
                    break;
            }
        }
    }
    public void motorControl() throws IOException{
        int mL = bt.stri.readInt();
        int mR = bt.stri.readInt();
        // TODO Add motors
    }

    public boolean controlCatch(){
        try{
            control();
        } catch(IOException ex){
            return false;
        }
        return true;
    }

}
