package penoplatinum.fullTests.barcode;

import penoplatinum.fullTests.hill.HillRobot;
import penoplatinum.model.processor.BarcodeModelProcessor;
import penoplatinum.model.processor.LightModelProcessor;
import penoplatinum.model.processor.LineModelProcessor;

public class BarcodeRobot extends HillRobot {

  public BarcodeRobot() {
    setModel(new BarcodeModel());
    getModel().setProcessor(new LightModelProcessor(
                            new LineModelProcessor(
                            new BarcodeModelProcessor(
                                    ))));
  }

  @Override
  public String getName() {
    return "Hello, I can read!";
  }
}
