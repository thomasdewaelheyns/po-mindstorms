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

  // simple implementation, performing one action per step
  public void step() {
    if( this.isBusy() ) { 
      Integer nextAction = this.todo.remove(0);
      switch(nextAction) {
        case GhostAction.FORWARD:
          // move 20cm
          this.api.move( 0.2 );
          break;
        case GhostAction.TURN_LEFT:
          // turn 90 degrees to the left
          this.api.turn(-90);
          break;
        case GhostAction.TURN_RIGHT:
          // turn 90 degrees to the left
          this.api.turn(90);
          break;
      }
    }
  }
  
  public void perform(List<Integer> actions) {
    this.todo = actions;
    this.step();
  }
}
