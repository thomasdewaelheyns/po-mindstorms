import java.util.List;
import java.util.ArrayList;

public class ManhattanDriver {
  private GhostModel model;   // TODO: change GhostModel to minimal interface
  private RobotAPI   api;     //       needed by a ManhattanDriver

  private List<Integer> todo = new ArrayList<Integer>();
  private Integer nextAction = GhostAction.NONE;
  
  public ManhattanDriver(GhostModel model, RobotAPI api) {
    this.model = model;
    this.api   = api;
  }
  
  // simple implementation, performing one action per step
  public boolean isBusy() {
    // a dangling feedback-update
    if( this.nextAction != GhostAction.NONE ) {
      this.postProcessPreviousStep();
    }
    return ! this.todo.isEmpty();
  }
  
  private void log(String msg) {
    System.out.printf( "[%10s] %2d,%2d / Driver : %s\n", 
                       this.model.getAgent().getName(),
                       this.model.getAgent().getLeft(),
                       this.model.getAgent().getTop(),
                       msg );
  }

  // simple implementation, performing one action per step
  public void step() {
    this.postProcessPreviousStep();
    // at this point, the real-world and the Model are in sync
    this.model.getGrid().show();
    this.log( "in sync" );
    //try { System.in.read(); } catch(Exception e) {}
    this.performNextStep();
  }

  // update the Model after successfully completing a requested action
  private void postProcessPreviousStep() {
    switch(this.nextAction) {
      case GhostAction.FORWARD:
        this.log( "moving model forward..." );
        this.model.moveForward();
        break;
      case GhostAction.TURN_LEFT:
        this.log( "turning model left..." );
        this.model.turnLeft();
        break;
      case GhostAction.TURN_RIGHT:
        this.log( "turning model left..." );
        this.model.turnRight();
        break;
      default:
        this.model.clearLastMovement();
    }
    this.nextAction = GhostAction.NONE;
  }
  
  private void performNextStep() {
    if( ! this.isBusy() ) { return; }

    // get the next action from the list and execute it through the RobotAPI
    this.nextAction = this.todo.remove(0);
    switch(this.nextAction) {
      case GhostAction.FORWARD:
        this.log( "going forward..." );
        if( ! this.api.move( 0.2 ) ) {
          // THIS SHOULDN'T HAPPEN, it happens when we want to move forward 
          // and an agent has moved into our targetted spot while we were
          // turning --> solution: don't do combined actions, but navigate
          // only one step at a time, including turning
          // if we can't perform this action, so don't let it be processed
          // on the Model and therefore reset it here
          this.nextAction = GhostAction.RESET;
        }
        break;
      case GhostAction.TURN_LEFT:
        this.log( "turning left..." );
        this.api.turn(-90);
        break;
      case GhostAction.TURN_RIGHT:
        this.log( "turn right..." );
        this.api.turn(90);
        break;
    }
  }
  
  public void perform(List<Integer> actions) {
    this.todo = actions;
    this.step();
  }
}
