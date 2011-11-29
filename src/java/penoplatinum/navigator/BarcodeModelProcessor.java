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

public class BarcodeModelProcessor extends ModelProcessor {
  private static final int WAITING = 0;
  private static final int RECORDING = 1;
  private static final int INTERPRET = 2;
  
  private BarcodeDataNav interpreter;
  private int brownCounter = 0;
  
  int state = WAITING;
  private ColorInterpreter colorInterpreter;
    
  
  public BarcodeModelProcessor() {
    super();
    this.colorInterpreter = new ColorInterpreter();
    interpreter = new BarcodeDataNav(colorInterpreter);
  }

  public BarcodeModelProcessor( ModelProcessor nextProcessor ) {
    super(nextProcessor);
  }

    @Override
    public void setModel(Model model) {
        super.setModel(model);
        colorInterpreter.setModel(model);
    }
  
    @Override
  protected void work() {
    Buffer tempBuffer = this.model.getLightValueBuffer();
    switch(state){
        case WAITING:
            if(!colorInterpreter.isColor(Color.Brown)){
                state = RECORDING;
                tempBuffer.setCheckPoint();
                System.out.println("RECORDING");
                brownCounter = 0;
            }
            break;
        case RECORDING:
            if(colorInterpreter.isColor(Color.Brown)){
                brownCounter++;
                if(brownCounter>5 && tempBuffer.getCheckpointSize()<3){
                    state = WAITING;
                    System.out.println("STOP");
                } else if(brownCounter>19){
                    state = INTERPRET;
                }
            } else {
                brownCounter = 0;
            }
            break;
        case INTERPRET:
            state = WAITING;
            System.out.println(tempBuffer.getBufferSubset().size());
            int barcode = interpreter.translate(tempBuffer.getBufferSubset());
            System.out.println("Barcode: "+barcode);
            if(barcode == -1){
                model.setBarcode(-1);
            } else {
                int corrected = interpreter.correct(barcode);
                model.setBarcode(corrected);
            }
    }
  }
}
