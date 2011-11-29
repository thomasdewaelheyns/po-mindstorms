package penoplatinum.navigator;

import penoplatinum.simulator.GoalDecider;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

public class BarcodeNavigator implements Navigator{
    public static final double DRIVE_BACK_DISTANCE = 0.200;
    public static final double DRIVE_FORWARD_DISTANCE = 0.300;
    public static final double TURN_LEFT = 90;
    public static final double TURN_RIGHT = 90;
    
    private int commandPosition = -1;
    private Command[] nextCommand = new Command[]{};
    private Model model;

    @Override
    public Boolean reachedGoal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int nextAction() {
        if(commandPosition!=nextCommand.length-1 && nextCommand[commandPosition].isBlocked()){
            if(model.isMoving()){
                return Navigator.NONE;
            }
            return nextCommand[++commandPosition].getNavigator();
        }
        if(commandPosition == nextCommand.length-1){
            resetCommands(new Command[0]);
        }
        Command[] override = getNewCommands();
        if(override != null){
            resetCommands(override);
        }
        return nextCommand[commandPosition].getNavigator();
    }

    private void resetCommands(Command[] commands) {
        nextCommand = commands;
        commandPosition = -1;
    }

    @Override
    public double getDistance() {
        if(nextCommand[commandPosition].getData() == Navigator.MOVE){
            return nextCommand[commandPosition].getData();
        } else {
            return 0.300;
        }
    }

    @Override
    public double getAngle() {
        if(nextCommand[commandPosition].getNavigator() == Navigator.TURN){
            return nextCommand[commandPosition].getData();
        } else {
            return 0;
        }
    }

    @Override
    public Navigator setModel(Model model) {
        this.model = model;
        return this;
    }

    @Override
    public Navigator setControler(GoalDecider controler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Command[] getNewCommands() {
        switch(model.getBarcode()){
            case -1:
            default:
                return null;
            case 0:
                return Commands.BLACKLINE;
            case 15:
                return Commands.WHITELINE;
            case 3:
                return Commands.BARCODE_LEFT;
            case 6:
                return Commands.BARCODE_RIGHT;
            case 1:
            case 2:
            case 4:
                return null;
        }
    }
    
    
    
}
