package penoplatinum.simulator;

import lejos.robotics.Color;

/*
 * LineFollowerNavigator
 * 
 * This navigator uses the lightsensor to follow lines accross the track
 * 
 * author: Team Platinum
 */
public class LineFollowerNavigator implements Navigator {

    private Boolean idle = true;
    private Model model;
    private int angle = -120;
    private int[] rotates = new int[]{-5, 10, -20, 30, -35, 40, -50, 60, -70, 80, -90, 100, -110, 120, -130, 140, -150, 160, -170, 180, -190, 200, -210, 220, -230, 240, -250, 260, -270, 280, -290, 300};
    private int timesturned = 0;

    @Override
    //no goal just drive
    public Boolean reachedGoal() {
        return false;
    }

    @Override
    public int nextAction() {
        //start
        if (this.idle) {
            this.idle = false;
            return Navigator.MOVE;
        }
        //get the color we are on
        int lightValue = model.getSensorValue(Model.S4);
        //
        if (lightValue == 100 || lightValue == 0) {
            timesturned = 0;
            return Navigator.MOVE;
        } else {
//            try {
//                angle = (int) rotates[timesturned];
//            } catch (IndexOutOfBoundsException e) {
//                timesturned = 0;
//                angle = (int) rotates[timesturned];
//            }
            timesturned++;
            if(timesturned == angle/3){
                angle = angle*-2;
                timesturned = 0;
            }
            return Navigator.TURN;
        }
        


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
