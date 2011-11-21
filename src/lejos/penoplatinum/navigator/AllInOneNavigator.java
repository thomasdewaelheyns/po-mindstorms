package penoplatinum.navigator;

import penoplatinum.simulator.Barcode;
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
  public static final int DRIVE = 1;
  public static final int TURN = 2;
  public static final int UTURN= 3;

    private Model model;
    private int   status = AllInOneNavigator.NONE;
    @Override
    //no goal just drive
    public Boolean reachedGoal() {
        return false;
    }

    @Override
    public int nextAction() {
        
        int barcode = model.getBarcode();
        switch( barcode ){
            
    }
    
    // start driving ...
    if( this.status == AllInOneNavigator.NONE ) {
      return this.drive();
    }
    
    
    }

    // if we're driving and we haven't bumped into something ... carry on
    return Navigator.NONE;
  }
  
    private int drive() {
        this.status = AllInOneNavigator.DRIVE;
        return Navigator.MOVE;
     }

    public double getDistance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getAngle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Navigator setModel(Model m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
    @Override
    // the model supplies the sensor values so we can decide to drive or turn
    public AllInOneNavigator setModel(Model model) {
        this.model = model;
        return this;
    }
}

