package penoplatinum.simulator;

/**
 * VeelhoekNavigatorRobot
 * 
 * This robot implements a basic polygon-driving algorithm, using only motors
 * and no sensors. It uses an event loop and a Navigator.
 * 
 * Author: Team Platinum
 */

class VeelhoekNavigatorRobot implements Robot {

  private RobotAPI  api;
  private Navigator navigator;

  private int   edgeCount    = 1; // just turn around
  private float vertexLength = 0; // stay in place

  public void useRobotAPI( RobotAPI api ) {
    this.api = api;
  }
  
  public void processCommand( String cmd ) {
    // this robot doesn't support the reception of commands while it's running
    // TODO: add errorchecking, we now only deal with correct input
    String[] parts    = cmd.split( ";" );
    this.edgeCount    = Integer.parseInt( parts[0] );
    this.vertexLength = Float.parseFloat( parts[1] );
  }
  
  public String getModelState() {
    // this robot doesn't use a Model, so no state can be returned.
    // it also doesn't support retrieval of the status while it's running
    return "";
  }
  
  public String getNavigatorState() {
    // TODO
    return "";
  }

  /**
   * This implements the Event Loop. The Robot continously asks the navigator
   * what to do.
   */
  public void run() {
    this.navigator = new VeelhoekNavigator(this.edgeCount, this.vertexLength);
    while( ! this.navigator.reachedGoal() ) {
      switch( this.navigator.nextAction() ) {
        case Navigator.MOVE:
          this.api.move(this.navigator.getDistance());
          break;
        case Navigator.TURN:
          this.api.turn(this.navigator.getAngle());
          break;
        default:
          // do nothing
      }
    }
  }
  
  public void stop() {
    // this robot doesn't support stopping, because its run method is executed
    // synchronously
  }

    @Override
    public void step() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean reachedGoal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
