import java.util.List;
import java.util.ArrayList;

public class GhostDriver {
  private GhostModel model;
  private RobotAPI   api;

  private List<int> todo = new ArrayList<int>();
  
  public GhostDriver(GhostModel model, RobotAPI api) {
    this.model = model;
    this.api   = api;
  }
  
  public boolean isBusy() {
    // TODO: implement reality, now we abuse every step to process one action
    this.step();
    return this.todo.empty();
  }
  
  private void step() {
    if( ! this.todo.empty() ) { 
      switch(this.todo.get(1)) {
        case Ghost.FORWARD:
          // move 20cm
          this.api.move( 0.2 );
          break;
        case Ghost.TURN_LEFT:
          // turn 90 degrees to the left
          this.api.turn(-90);
          break;
        case Ghost.TURN_RIGHT:
          // turn 90 degrees to the left
          this.api.turn(90);
          break;
      }
    }
  }
  
  public void perform(List<int> actions) {
    this.todo = actions;
  }
}
