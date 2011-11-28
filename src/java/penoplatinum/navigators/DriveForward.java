package penoplatinum.navigators;

import penoplatinum.simulator.*;
import penoplatinum.Utils;

/*
 * LineFollowerNavigator
 * 
 * This navigator uses the lightsensor to follow lines accross the track
 * 
 * author: Team Platinum
 */
public class DriveForward implements Navigator {

    private Model model;
    private int angle = -120;
    private int[] rotates = new int[]{-20, 120, -720};
    private int timesTurned = 0;

    public DriveForward setControler(GoalDecider controler) {
        return this;
    }

    // no goal just drive
    public Boolean reachedGoal() {
        return false;
    }
    boolean driveStarted = false;

    public int nextAction() {
        if (!driveStarted) {
            driveStarted = true;
            return Navigator.MOVE;
        }
        return Navigator.NONE;
    }

    @Override
    public double getDistance() {
        return 1;

    }

    @Override
    public double getAngle() {
        return 0;
    }

    @Override
    // the model supplies the lightsensorValues so we can decide to drive or turn
    public DriveForward setModel(Model model) {
        this.model = model;
        return this;
    }
}
