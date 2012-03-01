import java.util.List;

// copied from original Navigator.java
// modified to also return a list of actions for the driver
// removed GoalDecider
public interface Navigator {
  public static final int NONE = 0;
  public static final int MOVE = 1;
  public static final int TURN = 2;
  public static final int STOP = 4;

  public Boolean       reachedGoal();
  public int           nextAction();
  public List<Integer> nextActions();
  public double        getDistance();
  public double        getAngle();

  public Navigator     setModel(Model model);
}
