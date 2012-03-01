/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.ghost;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MHGameWork
 */
public class LeftFollowingGhostNavigator implements GhostNavigator {

  private final GhostModel m;


  public LeftFollowingGhostNavigator(GhostModel model) {
    this.m = model;
  }

  private ArrayList<Integer> outputBuffer = new ArrayList<Integer>();
  
  public List<Integer> nextActions() {

    outputBuffer.clear();
    
    if (!m.isWallLeft()) {
      outputBuffer.add(TURNLEFT);
      outputBuffer.add(MOVE);
    } else if (!m.isWallFront()) {
      outputBuffer.add(MOVE);
    } else if (!m.isWallRight()) {
      outputBuffer.add(TURNRIGHT);
      outputBuffer.add(MOVE);
    } else {
      outputBuffer.add(TURNAROUND);
      outputBuffer.add(MOVE);
    }

    return outputBuffer;
  }
}
