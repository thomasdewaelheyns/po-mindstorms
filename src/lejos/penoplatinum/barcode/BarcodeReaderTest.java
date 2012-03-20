/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.barcode;
import java.io.*;
import lejos.nxt.*;
import penoplatinum.util.Utils;
    
/**
 *
 * @author: Team Platinum
 */
public class BarcodeReaderTest {
    
    private final LightSensor lightSensor;
    //Less is black, more is brown
    private int BlackBrownBorder;
    private int BrownWhiteBorder;
    private boolean isReadingBarcode;
    boolean continueWhile;
    public BarcodeReaderTest(){
        throw new RuntimeException();
        //this.lightSensor = new LightSensor(SensorPort.S1, true);
        //isReadingBarcode = false;
    }
    
    
    public void read(){
        FileOutputStream out = null; // declare outside the try block
        File data = new File("log.txt");
        continueWhile = true;
        try {
            out = new FileOutputStream(data,true);
        } catch (Exception e) {}
        DataOutputStream dataOut = new DataOutputStream(out);
        while(continueWhile){
            int test = this.lightSensor.readValue();
            LCD.drawString(test+"  ", 0, 0);
            try {
               dataOut.writeChars(test+"\n");
            } catch (Exception e) {}
            Utils.Sleep(10);
        }
        try {
            out.close();
        } catch (Exception e) {}
        
    }
}