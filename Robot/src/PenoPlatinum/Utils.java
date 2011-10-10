/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PenoPlatinum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import lejos.nxt.Motor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

/**
 *
 * @author MHGameWork
 */
public class Utils {
    public static void Sleep(long milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
        }
    }
    
    public static void comp(){
        System.out.println("Bluetooth");
        BTConnection conn = Bluetooth.waitForConnection();
        DataInputStream str = conn.openDataInputStream();
        DataOutputStream stro = conn.openDataOutputStream();
        System.out.println("Connected");
        boolean cont=true;
        while(cont){
            try{
                switch(str.readByte()){
                    case 4:
                        int dist=str.readByte();
                        int hoeken=str.readByte();
                        int speed=str.readByte();
                        System.out.println(hoeken+" "+dist);
                        System.out.println(speed);
                        VeelhoekAction.veelhoek(hoeken,dist);
                        System.out.println("Done.");
                        break;
                    case 3:
                        cont=false;
                        break;
                    case 2:
                        int l=str.readByte()*10;
                        Motor.B.setSpeed(l);
                        if(l>0){
                            Motor.B.forward();
                        } else {
                            Motor.B.backward();
                        }
                        break;
                    case 1:
                        int r=str.readByte()*10;
                        Motor.C.setSpeed(r);
                        if(r>0){
                            Motor.C.forward();
                        } else {
                            Motor.C.backward();
                        }
                        break;
                    case 0:
                        byte ml = str.readByte();
                        byte mr = str.readByte();
                        System.out.println(ml+" "+mr);
                        break;
                }
            } catch(Exception e){
                conn = Bluetooth.waitForConnection();
                str = conn.openDataInputStream();
                stro = conn.openDataOutputStream();
            }
        }
    }
}
