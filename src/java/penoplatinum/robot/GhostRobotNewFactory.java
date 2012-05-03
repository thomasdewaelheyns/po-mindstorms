package penoplatinum.robot;

import penoplatinum.model.processor.BarcodeModelProcessor;
import penoplatinum.model.processor.FreeDistanceModelProcessor;
import penoplatinum.model.processor.InboxModelProcessor;
import penoplatinum.model.processor.LightModelProcessor;
import penoplatinum.model.processor.LineModelProcessor;
import penoplatinum.model.processor.ModelProcessor;

public class GhostRobotNewFactory {

  public static GhostRobot make() {
    ModelProcessor procs = 
            new LightModelProcessor(
            new FreeDistanceModelProcessor(
            new LineModelProcessor(
            new BarcodeModelProcessor(
            new InboxModelProcessor()))));

    return new GhostRobot(procs);
  }
}
