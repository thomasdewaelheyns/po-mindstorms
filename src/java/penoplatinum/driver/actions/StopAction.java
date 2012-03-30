package penoplatinum.actions;

import penoplatinum.simulator.Navigator;

public class StopAction extends BaseAction {

  public StopAction() {
    setDistance(0);
    setAngle(0);
  }
  public StopAction(long lengthTime){
    this();
    this.lengthTime = lengthTime;
  }

  boolean first = true;
  long startTime = 0;
  long lengthTime = 0;
  @Override
  public int getNextAction() {
    if(first){
      first = false;
      startTime = System.currentTimeMillis();
      return Navigator.STOP;
    }
    return Navigator.NONE;
  }

  @Override
  public boolean isComplete() {
    boolean temp = (startTime+lengthTime<System.currentTimeMillis()); // Never complete!
    return temp;
  }
   @Override
  public String getKind() {
     return "Stop";
  }

  @Override
  public String getArgument() {
    return lengthTime + "ms";
  }
}