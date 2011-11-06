package penoplatinum.simulator;

/**
 * VeelhoekRobot
 * 
 * This robor implements a basic polygon-driving algorithm, using only motors
 * and no sensors. It also doesn't use a Model nor a Navigator.
 * 
 * Author: Team Platinum
 */

class VeelhoekRobot implements Robot {

  private RobotAPI api;

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
    // this robot doesn't use a Model, so no state can be returned.
    // it also doesn't support retrieval of the status while it's running
    return "";
  }

  public void run() {
    double angle = 360.0 / this.edgeCount;
    for( int i = 0; i < this.edgeCount; i++ ) {
      this.api.move(this.vertexLength);
      this.api.turn(angle);
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
