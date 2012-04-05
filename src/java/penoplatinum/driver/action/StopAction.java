package penoplatinum.actions;

import penoplatinum.simulator.Navigator;

public class StopAction extends ActionSkeleton {

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


  public boolean isComplete() {
    boolean temp = (startTime+lengthTime<System.currentTimeMillis()); // Never complete!
    return temp;
  }

  public String getKind() {
     return "Stop";
  }


  public String getArgument() {
    return lengthTime + "ms";
  }
}