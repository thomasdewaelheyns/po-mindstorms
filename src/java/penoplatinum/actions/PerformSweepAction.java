/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.actions;

import penoplatinum.Utils;
import penoplatinum.modelprocessor.ColorInterpreter;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;
import penoplatinum.simulator.RobotAPI;

/**
 *
 * @author: Team Platinum
 */
public class PerformSweepAction extends BaseAction {

  private Model model;

  private final RobotAPI api;

  public PerformSweepAction(RobotAPI api, Model model) {
    super(model);
    this.api = api;
    this.model = model;


  }
  
  int state = -1;

  @Override
  public int getNextAction() {
    
    if (state == 0)
    {
      if (api.sweepInProgress())
        return Navigator.STOP;
    }
    
    
    
    state++;
    return getStateStart();

  }

  private int getStateStart() {
    switch(state)
    {
      case 0:
        
        int step = 5;
        int start = -120;
        int end = 120;
        int[] array = new int[(end - start) / step +1];
        
        for (int i = 0; i < array.length; i++) {
          array[i] = i * step + start;
        }
        api.sweep(array);
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
