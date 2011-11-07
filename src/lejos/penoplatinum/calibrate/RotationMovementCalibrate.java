package penoplatinum.calibrate;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import penoplatinum.movement.RotationMovement;
import penoplatinum.sensor.WrappedLightSensor;

public class RotationMovementCalibrate {

    public static void calibrateCCW(RotationMovement mov, WrappedLightSensor light) {
        mov.CCW_afwijking = 1;

        Button.waitForPress();
        double angle = 90;
        int turnCount = 0;
        int executeCount = 0;
        boolean blackNext = true;
        while (true) {
            if (mov.isStopped()) {
                Sound.beep();
                if (executeCount != turnCount) {
                    break;
                }
                executeCount += 2;
                mov.TurnOnSpotCCW(360.0, false);
            }
            if (blackNext && !light.isColor(WrappedLightSensor.Color.Brown)) {
                turnCount++;
                blackNext = false;
            }
            if (!blackNext && light.isColor(WrappedLightSensor.Color.Brown)) {
                blackNext = true;
            }
        }
        System.out.println(turnCount + " " + executeCount);
        double afwijkingTeller = executeCount * 180;
        double afwijkingNoemer = executeCount * 180 + (turnCount - executeCount) * angle;
        double afwijking = afwijkingTeller / afwijkingNoemer;
        System.out.println("" + afwijking);
        mov.CCW_afwijking *= afwijking;
        System.out.println("mov: "+mov.CCW_afwijking);
    }
}
