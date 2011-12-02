/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.actions;

import penoplatinum.navigator.ColorInterpreter;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 * TODO: WARNING: UNSAFE CONSTANTS USED
 * @author MHGameWork
 */
public class AlignOnBarcodeAction extends BaseAction {

  public static final int TARGET_ANGLE = 40;
  public static final int SWEEP_ANGLE = 250;
  ColorInterpreter colors;
  private int directionModifier;
  private final int correctionAngle;

  public AlignOnBarcodeAction(Model model, int correctionAngle) {
    super(model);
    colors = new ColorInterpreter().setModel(model);
    this.correctionAngle = correctionAngle;


  }
  private boolean started = false;
  private boolean correcting = false;
  private boolean lineStartFound = false;
  private boolean sweeping = false;
  private boolean abort = false;

  @Override
  public int getNextAction() {
    if (!started) {
      //Start!
      setAngle(90 * (Math.random() > 0.5 ? 1 : -1));
      started = true;
      return Navigator.TURN;
    }
    if (!colors.isColor(ColorInterpreter.Color.Brown)) {
      // Found something!! turn other way

      int diffTacho;
      //TODO
//      angleCCW *= CCW_afwijking;
//
//      int angle =  h / Math.PI / WIELAFSTAND*WIELOMTREK

    }
    if (!getModel().isTurning()) {
      setAngle(90 - correctionAngle);
      return Navigator.TURN;
    }

    if (0 == 0) {
      return Navigator.NONE;
    }


    if (!lineStartFound && !sweeping) {
      // Start sweep
      setAngle(directionModifier * SWEEP_ANGLE);
      sweeping = true;
      return Navigator.TURN;
    }
    if (sweeping) {
      if (!getModel().isMoving()) {
        // Sweep ended, robot did not find a line end, just abort
        abort = true;
        return Navigator.STOP;
      }

      if (!lineStartFound) {
        // Searching start
        if (!colors.isColor(ColorInterpreter.Color.Brown)) {
          // Start found!
          lineStartFound = true;
        }
      } else {
        if (colors.isColor(ColorInterpreter.Color.Brown)) {
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
}
