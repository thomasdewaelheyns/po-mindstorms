package penoplatinum.robot;

import penoplatinum.model.processor.LightModelProcessor;
import penoplatinum.model.processor.ModelProcessor;

public class GhostRobotNewFactory {
  
  public static GhostRobot make(){
    ModelProcessor procs = new LightModelProcessor();
    return new GhostRobot(procs);
  }
  
}
