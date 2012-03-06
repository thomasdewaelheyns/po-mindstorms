/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.actions;

import penoplatinum.modelprocessor.ColorInterpreter;
import penoplatinum.modelprocessor.LightColor;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 *
 * @author: Team Platinum
 */
public class AlignNotLineAction extends BaseAction {
  
  public static final int TARGET_ANGLE = 20;
  public static final int SWEEP_ANGLE = 250;
  ColorInterpreter colors;
  private int directionModifier;
  
  public AlignNotLineAction(Model model, boolean CCW) {
    super(model);
    directionModifier = CCW ? 1 : -1;
    colors = new ColorInterpreter().setModel(model);
    
    
  }
  private boolean lineStartFound = false;
  private boolean sweeping = false;
  private boolean sweepBack = false;
  private boolean abort = false;
  
  @Override
  public int getNextAction() {
    if (sweepBack)
    {
      if (getModel().isTurning())
        return Navigator.NONE;
      
      abort = true;
      return Navigator.STOP;
    }
    if (!lineStartFound && !sweeping) {
      // Start sweep
      setAngle(directionModifier * SWEEP_ANGLE);
      sweeping = true;
      return Navigator.TURN;
    }
    if (sweeping) {
      if (!getModel().isMoving()) {
        // Sweep ended, robot did not find a line end. Sweep back
        sweepBack = true;
        setAngle(SWEEP_ANGLE * -directionModifier);
        return Navigator.TURN;
      }
      
      if (!lineStartFound) {
        // Searching start
        if (!colors.isColor(LightColor.Brown)) {
          // Start found!
          lineStartFound = true;
        }
      } else {
        if (colors.isColor(LightColor.Brown)) {
          // End found!
          sweeping = false;
          
          setAngle(TARGET_ANGLE * directionModifier);
          
          return Navigator.TURN;
        }
      }
    }
    return Navigator.NONE;
    
  }
  
  @Override
  public boolean isComplete() {
    return (!getModel().isMoving() && !sweeping) || abort;
    
  }
  
  @Override
  public String getKind() {
    return "Align to line";
  }
  
  @Override
  public String getArgument() {
    return directionModifier > 0 ? "CCW" : "CW";
  }
}
