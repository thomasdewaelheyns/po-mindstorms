package penoplatinum.barcode;

import java.util.ArrayList;
import penoplatinum.Utils;
import penoplatinum.sensor.WrappedLightSensor;
import penoplatinum.sensor.WrappedLightSensor.Color;

public class BarcodeReader {

    private final WrappedLightSensor lightSensor;
    //Less is black, more is brown
    private boolean isReadingBarcode;
    boolean continueWhile;
    private int counter;
    private int startCounter;
    private BarcodeData interpreter;

    private int lastRawRead;

    public int getLastRawRead() {
        return lastRawRead;
    }
    
    public BarcodeReader(WrappedLightSensor sensor) {
        interpreter = new BarcodeData(sensor);
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
            if (! lightSensor.isColor(Color.Brown,test)) {
                isReadingBarcode = true;
                counter = 0;
                startCounter++;
            } else {
                counter++;
                if(isReadingBarcode && (startCounter<=3) && (counter >5)){
                    isReadingBarcode = false;
                    startCounter = 0;
                    counter =0;
                    code.clear();
                }
                if (isReadingBarcode && counter >= 20) {
                    // Was reading barcode but is now completed.
                    for(int i = 0; i<20; i++){
                        code.remove(code.size()-1);
                    }
                    int temp = interpreter.translate(code);
                    lastRawRead = temp;
                    if(temp==-1){
                        return -1;
                    }
                    int val = interpreter.correct(temp);
                    System.out.println(val+" "+temp);
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

}
