/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.actions;

import penoplatinum.simulator.Navigator;

/**
 *
 * @author MHGameWork
 */
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
      startTime = System.currentTimeMillis();
      return Navigator.STOP;
    }
    return Navigator.NONE;
  }

  @Override
  public boolean isComplete() {
    return (startTime+lengthTime<System.currentTimeMillis()); // Never complete!
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