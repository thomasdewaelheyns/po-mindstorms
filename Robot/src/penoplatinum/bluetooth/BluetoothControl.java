package penoplatinum.bluetooth;

import java.io.IOException;

public class BluetoothControl {
    BluetoothCommunication bt=new BluetoothCommunication();
    boolean isActive=false;

    public BluetoothControl() {}

    public void control() throws IOException {
        while(bt.str.available()>4){
            int read=bt.str.readInt();
            switch(read){
                case 0:
                    isActive=(bt.str.readInt()==0?false:true);
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
        int mL = bt.str.readInt();
        int mR = bt.str.readInt();
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
