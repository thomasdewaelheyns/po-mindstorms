package penoplatinum.navigator;

import penoplatinum.navigators.BarcodeNavigator;
import penoplatinum.simulator.Navigator;

public class Commands {
    public static Command[] BLACKLINE = new Command[]{Commands.BACK_MIDDEN, Commands.TURN_LEFT};
    public static Command[] WHITELINE = new Command[]{Commands.BACK_MIDDEN, Commands.TURN_RIGHT};
    public static Command[] BARCODE_LEFT = new Command[]{Commands.FORWARD_MIDDEN, Commands.TURN_LEFT};
    public static Command[] BARCODE_RIGHT = new Command[]{Commands.FORWARD_MIDDEN, Commands.TURN_RIGHT};
    
    public static Command FORWARD_MIDDEN = new Command(Navigator.MOVE, BarcodeNavigator.DRIVE_FORWARD_DISTANCE, true);
    public static Command BACK_MIDDEN = new Command(Navigator.MOVE, BarcodeNavigator.DRIVE_BACK_DISTANCE, true);
    public static Command TURN_LEFT = new Command(Navigator.MOVE, BarcodeNavigator.TURN_LEFT, true);
    public static Command TURN_RIGHT = new Command(Navigator.MOVE, BarcodeNavigator.TURN_RIGHT, true);
    
}
