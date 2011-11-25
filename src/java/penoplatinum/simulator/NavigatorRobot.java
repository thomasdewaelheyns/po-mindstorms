package penoplatinum.simulator;

/**
 * NavigatorRobot
 * 
 * General purpose Navigator-based Robot implementation.
 *  
 * Author: Team Platinum
 */

public class NavigatorRobot implements Robot {

  private RobotAPI  api;
  private Navigator navigator;
  private Model     model;
  
  public NavigatorRobot() {
    this.setupModel();
  }

  public NavigatorRobot(Navigator navigator) {
    this();
    this.useNavigator(navigator);
  }
  
  private void setupModel() {
    // setup a model with the required ModelProcessors
    this.model     = new Model();
    ModelProcessor sonar = new SonarModelProcessor();
    this.model.setProcessor( new FrontPushModelProcessor(sonar) );
  }
  
  public Robot useNavigator(Navigator navigator) {
    // setup the navigator using the same model
    this.navigator = navigator;
    this.navigator.setModel(this.model);
    return this;
  }

  public void useRobotAPI( RobotAPI api ) {
    this.api = api;
    this.initAPI();
  }
  
  // method to perform initializing actions when an API is available
  private void initAPI() {
    if( this.api == null ) { return; }
    this.api.setSpeed(Model.M3, 500); // set sonar speed to double of default
  }
  
  public void processCommand( String cmd ) {
    // no input required
  }
  
  public String getModelState() {
    return this.model.toString();
  }
  
  public String getNavigatorState() {
    return this.navigator.toString();
  }

  /**
   * This implements one step in the Event Loop. The Robot polls its sensors
   * updates its model and asks the navigator what to do.
   */
  public void step() {
    // get sensor data and update the model (motors are sensors too)
    this.model.updateSensorValues( this.api.getSensorValues() );

    // ask the navigator what to do next
    switch( this.navigator.nextAction() ) {
      case Navigator.MOVE:
        this.api.move(this.navigator.getDistance());
        break;
      case Navigator.TURN:
        this.api.turn((int)this.navigator.getAngle());
        break;
      case Navigator.STOP:
        this.api.stop();
        break;
      case Navigator.NONE:
      default:
        // do nothing
    }
  }

  public Boolean reachedGoal() {
    return this.navigator.reachedGoal();
  }
  
  public void stop() {
    this.api.stop();
  }
}
