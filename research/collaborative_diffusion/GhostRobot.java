public class GhostRobot implements Robot {
  private GhostModel      model;
  private RobotAPI        api;   // provided from the outside
  private ManhattanDriver driver;
  private Navigator       navigator;
  private RobotAgent      communicationAgent;
  
  private boolean waitingForSweep = false;
  
  public GhostRobot(String name) {
    this.setupModel(name);
  }

  public GhostRobot(String name, GridView view) {
    this.setupModel(name);
    this.model.displayGridOn(view);
  }
  
  private void setupModel(String name) {
    this.model = new GhostModel(name);
    ModelProcessor processors =
      new InboxProcessor        (
      new WallDetectorProcessor (
      new GridUpdateProcessor   (
    )));
    this.model.setProcessor(processors);
  }

  public Model getModel() {
    return this.model;
  }
  
  public String getModelState() {
    // TODO
    return "";
  }
  
  public String getNavigatorState() {
    // TODO
    return "";
  }

  public Robot useRobotAPI( RobotAPI api ) {
    this.api       = api;
    this.driver    = new ManhattanDriver(this.model, this.api);
    this.navigator = new GhostNavigator().setModel(this.model);
    return this;
  }

  public Robot useCommunicationAgent( RobotAgent agent ) {
    this.communicationAgent = agent;
    return this;
  }

  // incoming communication from other ghosts, used by RobotAgent to deliver
  // incoming messages from the other ghosts
  public void processCommand(String cmd) {
    this.model.addIncomingMessage(cmd);
  }

  private void log(String msg) {
    System.out.printf( "[%10s] %2d,%2d : %s\n", 
                       this.model.getAgent().getName(),
                       this.model.getLeft(),
                       this.model.getTop(),
                       msg );
  }
  
  // one step in the event-loop of the Robot
  public void step() {
    this.log( "start step" );
    // poll other sensors and update model
    this.model.updateSensorValues(this.api.getSensorValues());

    // let the driver do his thing
    if( this.driver.isBusy() ) { 
      this.driver.step();
      return;
    }
    
    // we want obstacle-information based on Sonar-values
    // as long as this is in progress, we wait
    if( this.api.sweepInProgress() ) { 
      return;
    }
    
    // if the sweep is ready ...
    if( this.waitingForSweep ) {
      this.model.updateSonarValues(this.api.getSweepResult());
      this.waitingForSweep = false;
    } else {
      this.api.sweep( new int[] { -90, 0, 90 } );
      this.waitingForSweep = true;
      return; // to wait for results
    }
    
    System.out.println( this.model.explain() );
    try { System.in.read(); } catch(Exception e) {}

    
    // ask navigator what to do and ...
    // let de driver drive, manhattan style ;-)
    this.driver.perform(this.navigator.nextActions());
    
    // send outgoing messages
    this.log( "sending messages" );
    this.sendMessages();

    this.log( "step done" );
  }
  
  private void sendMessages() {
    if( this.communicationAgent == null ) { return; }
    for( String msg : this.model.getOutgoingMessages() ) {
      this.communicationAgent.send(msg);
    }
    this.model.clearOutbox();
  }
  
  public Boolean reachedGoal() {
    return this.navigator.reachedGoal();
  }
  
  public void stop() {
    this.api.stop();
  }
}
