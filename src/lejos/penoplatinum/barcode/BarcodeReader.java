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
        //TODO: this needs refactoring
        interpreter = new BarcodeData(null);
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
            int test = this.lightSensor.getLightValue();
            if (!lightSensor.isColor(Color.Brown, test)) {
                if (!isReadingBarcode) {
                    Utils.Log("Start Barcode");
                }
                isReadingBarcode = true;
                counter = 0;
                startCounter++;
            } else {
                Utils.Log(counter+"");
                counter++;
                if (isReadingBarcode && (startCounter <= 3) && (counter > 5)) {
                    Utils.Log("False positive");
                    isReadingBarcode = false;
                    startCounter = 0;
                    counter = 0;
                    code.clear();
                }
                if (isReadingBarcode && counter >= 20) {
                    Utils.Log("Barcode interpretation");
                    // Was reading barcode but is now completed.
                    for (int i = 0; i < 20; i++) {
                        code.remove(code.size() - 1);
                    }
                    int temp = interpreter.translate(code);
                    lastRawRead = temp;
                    if (temp == -1) {
                        return -1;
                    }
                    int val = interpreter.correct(temp);
                    Utils.Log(val + " " + temp);
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

    public void stopLoop() {
        continueWhile = false;
    }
}
