package penoplatinum.navigator;

import penoplatinum.simulator.GoalDecider;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;


/*
 * LineFollowerNavigator
 * 
 * This navigator uses the lightsensor to follow lines accross the track
 * 
 * author: Team Platinum
 */
public class AllInOneNavigator implements Navigator {

    public static final int NONE = 0;
    
    //This is a bitmask
    public static final int DRIVE_TO_MIDDLE= 0;
    public static final int DRIVE_DIRECTION_BACKWARD = 5;
    public static final int TURN = 1;
    public static final int LEFT = 2;
    public static final int ONE_EIGHTY = 3;
    
    private Model model;
    private int next = AllInOneNavigator.NONE;
    private int current = AllInOneNavigator.NONE;

    @Override
    //no goal just drive
    public Boolean reachedGoal() {
        return false;
    }

    @Override
    public int nextAction() {
        if(!this.model.isMoving() && ((next>>AllInOneNavigator.TURN)&1) == 1){
            System.out.println("Turning: ");
            current = next;
            next = AllInOneNavigator.NONE;
            return Navigator.TURN;
        }

        int barcode = model.getBarcode();
        model.setBarcode(-1);
        if(barcode == -1){
            if(this.model.isMoving()){
                return Navigator.NONE;
            }
            return Navigator.MOVE;
        }
        //System.out.println("Barcode found: "+barcode);
        next = AllInOneNavigator.NONE;
        current = AllInOneNavigator.NONE;
        switch (barcode) {
            case 0:
                current |= 1<<AllInOneNavigator.DRIVE_DIRECTION_BACKWARD;
            case 3:
                next |= 1<<AllInOneNavigator.LEFT;
            case 6:
                next |= 1<<AllInOneNavigator.TURN;
                current |= 1<<AllInOneNavigator.DRIVE_TO_MIDDLE;
                System.out.println("Turn! "+next+", "+current);
                return Navigator.MOVE;
                
            case 15:
                current |= 1<<AllInOneNavigator.DRIVE_DIRECTION_BACKWARD;
                next |= 1<<AllInOneNavigator.TURN;
                current |= 1<<AllInOneNavigator.DRIVE_TO_MIDDLE;
                System.out.println("Turn! "+next+", "+current);
                return Navigator.MOVE;
                
            case 9:  //Wrong direction
            case 12: //Wrong direction
            case 14: //Wrong direction
                next |= 1<<AllInOneNavigator.TURN;
                next |= 1<<AllInOneNavigator.ONE_EIGHTY;
                return Navigator.MOVE;
            case 1: //forward
            case 2: //forward
            case 4: //forward
                
            case 5:
            case 7:
            case 8:
            case 10:
            case 11:
            case 13:
            default:
                return Navigator.MOVE;
        }
    }
    @Override
    public double getDistance() {
        int direction = 1-((current>>AllInOneNavigator.DRIVE_DIRECTION_BACKWARD<<1)&2);
        double distance = (((current>>AllInOneNavigator.DRIVE_TO_MIDDLE)&1)==1? 0.200 : 0.325);
        //System.out.println("distance calculated: "+distance*direction);
        return distance*direction;
    }

    @Override
    public double getAngle() {
        int direction = ((current>>AllInOneNavigator.LEFT<<1)&2)-1;
        int angle = 90+(current>>AllInOneNavigator.ONE_EIGHTY)&1*90;
        return direction*angle;
    }
    
    // the model supplies the sensor values so we can decide to drive or turn
    @Override
    public AllInOneNavigator setModel(Model model) {
        this.model = model;
        return this;
    }

    @Override
    public Navigator setControler(GoalDecider controler) {
        return this; //TODO: check this
    }
}
