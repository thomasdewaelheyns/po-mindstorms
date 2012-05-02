package penoplatinum.fullTests.hill;

import penoplatinum.fullTests.line.LineModel;
import penoplatinum.fullTests.line.LineRobot;
import penoplatinum.model.processor.LightModelProcessor;
import penoplatinum.model.processor.LineModelProcessor;

public class HillRobot extends LineRobot {


  public HillRobot() {
    setModel(new LineModel());
    getModel().setProcessor(new LightModelProcessor(
                            new LineModelProcessor()));
  }

  @Override
  public String getName() {
    return "Hello, I climb hills!";
  }
}
