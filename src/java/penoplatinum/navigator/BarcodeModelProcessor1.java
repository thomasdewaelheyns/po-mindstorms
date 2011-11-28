package penoplatinum.navigator;

import penoplatinum.navigator.ColorInterpreter.Color;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.ModelProcessor;

/**
 * FrontPushModelProcessor
 * 
 * Implements a ModelProcessor that detects frontpushes based on a central
 * front pushsensor.
 * 
 * Author: Team Platinum
 */

public class BarcodeModelProcessor1 extends ModelProcessor {
  private BarcodeBuffer buffer;
  private int bufferSize = 1000;
  private BarcodeDataNav interpreter;
  private int brownCounter = 0;
  
  int state = 0;
  private static final int WAITING = 0;
  private static final int RECORDING = 1;
  private static final int INTERPRET = 2;
  
  private ColorInterpreter colorInterpreter;
    
  public BarcodeModelProcessor1() {
    super();
    this.colorInterpreter = new ColorInterpreter();
    buffer = new BarcodeBuffer(bufferSize);
    for( int i = 0; i<1000; i++)
        buffer.insert(colorInterpreter.getLightValue());
    interpreter = new BarcodeDataNav(colorInterpreter);
  }

  public BarcodeModelProcessor1( ModelProcessor nextProcessor ) {
    super(nextProcessor);
  }

    @Override
    public void setModel(Model model) {
        super.setModel(model);
        colorInterpreter.setModel(model);
    }
  
  protected void work() {
    switch(state){
        case WAITING:
            if(!colorInterpreter.isColor(Color.Brown)){
                state = RECORDING;
                buffer.setCheckPoint();
                System.out.println("RECORDING");
                brownCounter = 0;
                buffer.insert(colorInterpreter.getLightValue());
            }
            break;
        case RECORDING:
            if(colorInterpreter.isColor(Color.Brown)){
                brownCounter++;
                if(brownCounter>5 && buffer.getCheckpointSize()<3){
                    state = WAITING;
                    System.out.println("STOP");
                } else if(brownCounter>19){
                    state = INTERPRET;
                }
            } else {
                brownCounter = 0;
                buffer.insert(colorInterpreter.getLightValue());
            }
            break;
        case INTERPRET:
            state = WAITING;
            int barcode = interpreter.translate(buffer.getBarcodeBufferSubset());
            System.out.println("Barcode: "+barcode);
            int corrected = interpreter.correct(barcode);
            model.setBarcode(corrected);
    }
  }
}
