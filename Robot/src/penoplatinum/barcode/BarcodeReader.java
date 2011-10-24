package penoplatinum.barcode;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import lejos.nxt.*;

/**
 *
 * @author Thomas
 */
public class BarcodeReader {

    private final iLightSensor lightSensor;
    //Less is black, more is brown
    private boolean isReadingBarcode;
    boolean continueWhile;
    private int counter;
    private int startCounter;
    private BarcodeInterpreter interpreter;

    public BarcodeReader(iLightSensor sensor) {
        interpreter = new BarcodeInterpreter();
        this.lightSensor = sensor;
        isReadingBarcode = false;
        
    }

    public int read() {
        ArrayList<Integer> code = new ArrayList<Integer>();
        counter = 0;
        startCounter = 0;
        this.continueWhile = true;
        this.isReadingBarcode = false;
        while (continueWhile) {
            int test = this.lightSensor.readValue();
            if (!isBrown(test)) {
                isReadingBarcode = true;
                counter = 0;
                startCounter++;
            } else {
                counter++;
                if(isReadingBarcode && (startCounter<=4) && (counter >3)){
                    isReadingBarcode = false;
                    startCounter = 0;
                    counter =0;
                    code.clear();
                }
                if (isReadingBarcode && counter >= 10) {
                    // Was reading barcode but is now completed.
                    for(int i = 0; i<10; i++){
                        code.remove(code.size()-1);
                    }
                    int temp = interpreter.translate(code);
                    if(temp==-1){
                        return -1;
                    }
                    int val = interpreter.correct(temp);
                    System.out.println(""+val);
                    return val;
                }
            }

            if (isReadingBarcode) {
                code.add(test);
            }
            Utils.Sleep(5);
        }
        return -1;
    }
    /*    
    private void calibrate(){
    
    }
     */

    private boolean isBrown(int value) {
        return (interpreter.BlackBrownBorder < value) && (value < interpreter.BrownWhiteBorder);
    }

//    public void calibrate() {
//        // calibreer de lage waarde.
//        System.out.println("Zet de sensor op zwart en druk enter.");
//        Button.ENTER.waitForPressAndRelease();
//        LCD.drawInt(lightSensor.readValue(), 1, 0);
//        lightSensor.calibrateLow();
//
//        Sound.beep();
//        Utils.Sleep(1000);
//
//        // calibreer de hoge waarde
//        System.out.println("Zet de sensor op wit en druk enter.");
//        Button.ENTER.waitForPressAndRelease();
//        LCD.drawInt(lightSensor.readValue(), 3, 0);
//        lightSensor.calibrateHigh();
//
//        Sound.beep();
//        Utils.Sleep(1000);
//    }
}
