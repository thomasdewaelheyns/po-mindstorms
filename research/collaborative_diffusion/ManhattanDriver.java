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
    this.performNextStep();
  }

  // update the Model after successfully completing a requested action
  private void postProcessPreviousStep() {
    switch(this.nextAction) {
      case GhostAction.FORWARD:
        this.model.moveForward();
        break;
      case GhostAction.TURN_LEFT:
        this.model.turnLeft();
        break;
      case GhostAction.TURN_RIGHT:
        this.model.turnRight();
        break;
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
        this.api.move( 0.2 );
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
