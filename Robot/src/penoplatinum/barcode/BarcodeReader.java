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
    private int BlackBrownBorder =50;
    private int BrownWhiteBorder=80;
    private boolean isReadingBarcode;
    boolean continueWhile;
    private int counter;

    public BarcodeReader() {
        this.lightSensor = new LightSensor(SensorPort.S1, true);
        isReadingBarcode = false;
        calibrate();
    }

    public String read() {
        ArrayList<Integer> code = new ArrayList<Integer>();
        counter = 0;
        this.continueWhile = true;
        this.isReadingBarcode = false;
        while (continueWhile) {
            int test = this.lightSensor.readValue();
            if (!isBrown(test)) {
                isReadingBarcode = true;
                counter = 0;
            } else {
                counter++;
                if (isReadingBarcode && counter >= 3) {
                    // Was reading barcode but is now completed.
                    return translate(code);
                }
            }

            if (isReadingBarcode) {
                code.add(test);
            }
            Utils.Sleep(10);
        }
        return "Terminated";
    }
    /*    
    private void calibrate(){
    
    }
     */

    private boolean isBrown(int value) {
        return(BlackBrownBorder < value) && (value < BrownWhiteBorder);
    }

    private int isColor(int value) {
        if (value < ((BlackBrownBorder + BrownWhiteBorder) / 2)) {
            return 0;
        } else {
            return 1;
        }
    }

    private String translate(ArrayList<Integer> list) {
        int code = 0;
        int val = 0;
        int pos = 1;
        for (int i = 0; i < 7; i++) {
            int sum = 0;
            for (int j = (i * list.size()) / 7; j < (i + 1) * list.size() / 7; j++) {
                sum += list.get(j);
            }
            int averageValue = sum / (((i + 1) * list.size() / 7) - ((i * list.size()) / 7));
            if (isColor(averageValue) != 0) {
                val += pos;
            }
            pos = pos * 2;
        }
        return correct(val); // here we can add a hamming correction function
    }

    private String correct(int value) {
        BarcodeData temp = new BarcodeData();
        byte corrected = (byte) (temp.getBarcodesRepair(value) / 8);
        return temp.getBarcodesString(corrected);

    }

    public void calibrate() {
        // calibreer de lage waarde.
        System.out.println("Zet de sensor op zwart en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(lightSensor.readValue(), 1, 0);
        lightSensor.calibrateLow();

        Sound.beep();
        Utils.Sleep(1000);

        // calibreer de hoge waarde
        System.out.println("Zet de sensor op wit en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(lightSensor.readValue(), 3, 0);
        lightSensor.calibrateHigh();

        Sound.beep();
        Utils.Sleep(1000);
    }
}
