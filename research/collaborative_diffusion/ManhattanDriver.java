import java.util.List;
import java.util.ArrayList;

public class ManhattanDriver {
  private GhostModel model;   // TODO: change GhostModel to minimal interface
  private RobotAPI   api;     //       needed by a ManhattanDriver

  private List<Integer> todo = new ArrayList<Integer>();
  
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
    this.log( "starting step" );
    if( this.isBusy() ) { 
      Integer nextAction = this.todo.remove(0);
      switch(nextAction) {
        case GhostAction.FORWARD:
          this.log( "forward" );
          // move 20cm
          this.model.getAgent().moveForward();
          this.api.move( 0.2 );
          break;
        case GhostAction.TURN_LEFT:
          this.log( "turn left" );
          // turn 90 degrees to the left
          this.model.getAgent().turnLeft();
          this.api.turn(-90);
          break;
        case GhostAction.TURN_RIGHT:
          this.log( "turn right" );
          // turn 90 degrees to the right
          this.model.getAgent().turnRight();
          this.api.turn(90);
          break;
      }
    } else {
      this.log("skipping, still moving" );
    }
  }
  
  public void perform(List<Integer> actions) {
    this.todo = actions;
    this.step();
  }
}
