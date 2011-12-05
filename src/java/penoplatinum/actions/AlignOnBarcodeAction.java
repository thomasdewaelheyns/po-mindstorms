/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.actions;

import penoplatinum.modelprocessor.ColorInterpreter;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 * TODO: WARNING: UNSAFE CONSTANTS USED
 * @author: Team Platinum
 */
public class AlignOnBarcodeAction extends BaseAction {

  public static double CCW_afwijking = 1.01;
  ColorInterpreter colors;
  public final int WIELOMTREK = 175; //mm
  public final int WIELAFSTAND = 160;//112mm
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
  private int startTacho;
  private int directionModifier;

  @Override
  public int getNextAction() {
    if (correcting) {
      return Navigator.NONE;
    }
    if (!started) {
      //Start!
      directionModifier = -1;//(Math.random() > 0.5 ? 1 : -1);
      setAngle(90 * directionModifier);
      startTacho = getModel().getSensorValue(Model.M1);
      started = true;
      return Navigator.TURN;
    }
    if (!colors.isColor(ColorInterpreter.Color.Brown)) {
      // Found something!! turn other way

      int diffTacho = getModel().getSensorValue(Model.M1) - startTacho;
      int angle = (int) (diffTacho / Math.PI / WIELAFSTAND * WIELOMTREK);
      setAngle((Math.abs(angle) + correctionAngle) * -directionModifier);
      correcting = true;
      return Navigator.TURN;
    }
    if (!getModel().isTurning()) {
      setAngle((90 - correctionAngle) * -directionModifier);
      correcting = true;
      return Navigator.TURN;
    }

    return Navigator.NONE;
  }

  @Override
  public boolean isComplete() {
    return !getModel().isMoving() && correcting;

  }

  @Override
  public String getKind() {
    return "Align to barcode";
  }

  @Override
  public String getArgument() {
    return correctionAngle + "°";
  }
}
