package penoplatinum.fullTests.barcode;

import penoplatinum.fullTests.hill.HillRobot;
import penoplatinum.model.processor.BarcodeModelProcessor;
import penoplatinum.model.processor.LightModelProcessor;
import penoplatinum.model.processor.LineModelProcessor;
import penoplatinum.model.processor.ModelProcessor;

public class BarcodeRobot extends HillRobot {

  @Override
  protected ModelProcessor getProcessors() {
    return new LightModelProcessor(
            new LineModelProcessor(
            new BarcodeModelProcessor()));
  }

  @Override
  public String getName() {
    return "Hello, I can read!";
  }
}
