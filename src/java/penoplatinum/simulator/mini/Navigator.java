package penoplatinum.simulator.mini;

import java.util.List;

import penoplatinum.simulator.Model;

// copied from original Navigator.java
// removed GoalDecider, reordered, grouped logically
public interface Navigator {
  public static final int NONE = 0;
  public static final int MOVE = 1;
  public static final int TURN = 2;
  public static final int STOP = 4;

  public Navigator     setModel(Model model);

  public int           nextAction();
  public double        getDistance();
  public double        getAngle();

  public Boolean       reachedGoal();
}
