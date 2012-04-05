package penoplatinum.actions;

/**
 *
 * @author: Team Platinum
 */

import java.util.ArrayList;

import penoplatinum.model.Model;
import penoplatinum.navigator.Navigator;
import penoplatinum.robot.RobotAPI;


public class PerformSweepAction extends ActionSkeleton {

  private Model model;
  private final RobotAPI api;
  int[] array;
  ArrayList<Integer> arrayList = new ArrayList<Integer>();

  public PerformSweepAction(RobotAPI api, Model model) {
    super(model);
    this.api = api;
    this.model = model;

    int step = 5;
    int start = -120;
    int end = 120;
    array = new int[(end - start) / step + 1];

    for (int i = 0; i < array.length; i++) {
      array[i] = i * step + start;
      arrayList.add(array[i]);
    }

  }
  int state = -1;

  @Override
  public int getNextAction() {

    if (api.sweepInProgress()) {
      return Navigator.STOP;
    }


    state++;
    return getStateStart();

  }

  private int getStateStart() {
    switch (state) {
      case 0:
        api.sweep(array);
        break;
      case 1:
        this.model.getSonarPart().updateSonarValues(this.api.getSweepResult(), arrayList);
        break;
    }
    return Navigator.STOP;
  }

  @Override
  public boolean isComplete() {
    return state > 0;
  }

  @Override
  public String getKind() {
    return "PerformSweep";
  }

  @Override
  public String getArgument() {
    return "";
  }
}
