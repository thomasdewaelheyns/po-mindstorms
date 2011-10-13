/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.barcode;
import java.util.ArrayList;
import penoplatinum.movement.Utils;
import lejos.nxt.*;
    
/**
 *
 * @author Thomas
 */
public class BarcodeReader {
    
    private final LightSensor lightSensor;
    //Less is black, more is brown
    private int BlackBrownBorder;
    private int BrownWhiteBorder;
    private boolean isReadingBarcode;
    public BarcodeReader(){
        this.lightSensor = new LightSensor(SensorPort.S4, true);
        isReadingBarcode = false;
    }
    
    
    public int read(){
        ArrayList<Integer> code = new ArrayList<Integer>();
        while(true){
            int test = this.lightSensor.readValue();
            if((isReadingBarcode == false) && (isBrown(test))) isReadingBarcode = true;
            if(isBrown(test)){
                if(isReadingBarcode == true){
                    return translate(code);
                }
            }
            else{
                code.add(test);
            }
            Utils.Sleep(10);
        }
    }
    /*    
    private void calibrate(){
        
    }
    */
    
    private boolean isBrown(int value){
        if((BlackBrownBorder<value) && (value<BrownWhiteBorder)){
            return true;
        }
        return false;
    }
    
    private int isColor(int value){
        if(value<((BlackBrownBorder+BrownWhiteBorder)/2)) return 0;
        else return 1;
    }
    
    private int translate( ArrayList<Integer> list){
        int code = 0;
        int val = 0;
        int pos = 1;
        for(int i = 0; i < 7; i++){
            int sum = 0;
            for(int j = (i*list.size())/7; j <(i+1)*list.size()/7 ; j++){
                sum += list.get(j);
            }
            int averageValue = sum/(((i+1)*list.size()/7)-((i*list.size())/7));
            if(isColor(averageValue) !=0 ){
                val+=pos;
            }
            pos = pos*2;
        }
        return val; // here we can add a hamming correction function
    }
    
}
