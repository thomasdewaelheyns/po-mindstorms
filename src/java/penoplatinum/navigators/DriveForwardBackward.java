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
public class DriveForwardBackward implements Navigator {

    private Model model;
    private int angle = -120;
    private int[] rotates = new int[]{-20, 120, -720};
    private int timesTurned = 0;

    public DriveForwardBackward setControler(GoalDecider controler) {
        return this;
    }

    // no goal just drive
    public Boolean reachedGoal() {
        return false;
    }
    boolean direction = false;

    public int nextAction() {
        //Utils.Log(model.isMoving()? "WEEEE": "Bored");
        if (!model.isMoving()) {
            direction = !direction;

            return Navigator.MOVE;
        }
        return Navigator.NONE;
    }

    @Override
    public double getDistance() {
        return direction ? 1 : -1;

    }

    @Override
    public double getAngle() {
        return 0;
    }

    @Override
    // the model supplies the lightsensorValues so we can decide to drive or turn
    public DriveForwardBackward setModel(Model model) {
        this.model = model;
        return this;
    }
}
