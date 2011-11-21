package penoplatinum.navigator;

import java.util.ArrayList;
import penoplatinum.Utils;
import penoplatinum.barcode.BarcodeData;
import penoplatinum.sensor.ColorInterpreter;
import penoplatinum.sensor.ColorInterpreter.Color;
import penoplatinum.simulator.Barcode;
import penoplatinum.simulator.ModelProcessor;

/**
 * FrontPushModelProcessor
 * 
 * Implements a ModelProcessor that detects frontpushes based on a central
 * front pushsensor.
 * 
 * Author: Team Platinum
 */

public class BarcodeModelProcessor extends ModelProcessor {
  private BarcodeBuffer buffer;
  private int bufferSize = 1000;
  private boolean isReadingBarcode;
  boolean continueWhile;
  private int counter;
  private int startCounter;
  private BarcodeData interpreter;
  
  private ColorInterpreter colorInterpreter;
    
  public BarcodeModelProcessor() {
    super();
    this.colorInterpreter = new ColorInterpreter(model);
    buffer = new BarcodeBuffer(bufferSize);
    for( int i = 0; i<1000; i++)
        buffer.insert(colorInterpreter.getLightValue());
    interpreter = new BarcodeData(colorInterpreter);
  }

  public BarcodeModelProcessor( ModelProcessor nextProcessor ) {
    super( nextProcessor );
  }
  
  
  protected void work() {
    buffer.insert(colorInterpreter.getLightValue());
    this.model.setBarcode(read());
    
    
  }
  
  
  
  
  
  
  
  
  
  
  public int read() {
        ArrayList<Integer> code = new ArrayList<Integer>();
        counter = 0;
        startCounter = 0;
        this.continueWhile = true;
        this.isReadingBarcode = false;
        int index = 0;
        while (index < this.buffer.getSize()) {
            int test = this.buffer.get(index);
            if (!colorInterpreter.isColor(Color.Brown, test)) {
                isReadingBarcode = true;
                counter = 0;
                startCounter++;
            } else {
                counter++;
                if (isReadingBarcode && (startCounter <= 3) && (counter > 5)) {
                    isReadingBarcode = false;
                    startCounter = 0;
                    counter = 0;
                    code.clear();
                }
                if (isReadingBarcode && counter >= 20) {
                    // Was reading barcode but is now completed.
                    for (int i = 0; i < 20; i++) {
                        code.remove(code.size() - 1);
                    }
                    int temp = interpreter.translate(code);
                    if (temp == -1) {
                        return -1;
                    }
                    int val = interpreter.correct(temp);
                    return val;
                }
            }
            if (isReadingBarcode) {
                code.add(test);
            }
            index++;
        }
        return Barcode.None;
    }

    
    
    
    
    
    
    
    

}
