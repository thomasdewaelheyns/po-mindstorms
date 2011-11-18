package penoplatinum.simulator;

/*
 * LineFollowerNavigator
 * 
 * This navigator uses the lightsensor to follow lines accross the track
 * 
 * author: Team Platinum
 */
public class LineFollowerNavigator implements Navigator {

    private Model model;
    private int angle = -120;
    private int[] rotates = new int[]{-20, 120, -720};
    private int timesTurned = 0;

    @Override
    //no goal just drive
    public Boolean reachedGoal() {
        return false;
    }

    @Override
    public int nextAction() {
        int lightValue = model.getSensorValue(Model.S4);
        if (lightValue < 30 || lightValue > 85) {
            timesTurned = 0;
            return Navigator.MOVE;
        }
        if (model.isMoving()) {
            if(model.isTurning()){
                return Navigator.NONE;
            } else {
                return Navigator.STOP;
            }
        }
        angle = rotates[timesTurned] + (timesTurned > 0 ? -rotates[timesTurned - 1] : 0);
        timesTurned++;
        return Navigator.TURN;
    }

    @Override
    public double getDistance() {
        return 0.1;

    }

    @Override
    public double getAngle() {
        return angle;
    }

    @Override
    // the model supplies the lightsensorValues so we can decide to drive or turn
    public LineFollowerNavigator setModel(Model model) {
        this.model = model;
        return this;
    }
}
