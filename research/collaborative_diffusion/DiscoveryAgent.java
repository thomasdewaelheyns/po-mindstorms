import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class DiscoveryAgent extends MovingAgent {
  private boolean finished = false;
  private Grid goalGrid;
  private Stack<Integer> stack = new Stack<Integer>();
  private List<Sector> visited = new ArrayList<Sector>();
    
  public DiscoveryAgent(Grid grid) {
    super( "discoverer" );
    this.goalGrid = grid;
  }
  
  public boolean isHolding() {
    return this.finished;
  }
  
  public void move(int n, int e, int s, int w) {
    // init first sector
    if( this.visited.isEmpty() ) { this.visited.add(this.getSector()); }
    
    if( this.isMoving() ) {
      this.log("continue movement...");
      this.processMovement();
    } else if( this.currentTileHasUnknownWalls() ) {
      this.log("detecting tile...");
      this.detectTile();
    } else if( !this.stack.empty() ) {
      this.log("inspecting unknown walls...");
      this.processNextSector();
    } else {
      this.finished = true;
      this.log("nothing more to discover...");
      return;
    }
    this.log( "stack  : " + this.stack + " / " + this.getOrientation());
  }
  
  private boolean currentTileHasUnknownWalls() {
    Sector sector = this.getSector();
    return sector != null && sector.getCertainty() != 15;
  }
  
  private void detectTile() {
    // TODO: externalize access to goalGrid
    Sector sector = this.getSector();
    // look at each wall that isn't known yet
    for(int bearing=Bearing.W; bearing>=Bearing.N; bearing-- ) {
      if( ! sector.isKnown(bearing) ) {
        if( this.getOrientation() != bearing ) {
          this.log( "turing to " + bearing + " to detect it" );
          this.turnTo(bearing);
        } else {
          this.log( "looking at " + bearing );
          Sector goalSector = 
            this.goalGrid.getSector(sector.getLeft(), sector.getTop());
          Boolean hasWall = goalSector.hasWall(bearing);
          if( hasWall != null ) {
            if( hasWall ) {
              this.log( "Wall @ " + bearing + " -> moving on" );
              sector.addWall(bearing);
            } else {
              sector.removeWall(bearing);
              // no wall, this is interesting, let's put it on the stack
              this.log( "NO Wall @ " + bearing + " -> adding to stack" );
              this.stack.push(bearing);
            }
          }
        }
        break;
      }
    }
  }
  
  private void processNextSector() {
    int nextOrientation = this.peekNext();
      
    // different situations
    // 1. next is forward
    // --> only if forward isn't visited, skip it
    // 2. next is backtracking
    // --> turn and go there
    while( this.nextIsForward() && 
           this.visited.contains(this.getSector().getNeighbour(nextOrientation)) )
    {
      // this means we've already visited this next Sector, we can skip it
      this.log("Backtracking...");
      this.popNext();
      if(this.stack.empty()) { return; }
      nextOrientation = this.peekNext();
    }

    // we need to visit it, so turn towards it and visit it
    if( this.getOrientation() != nextOrientation ) {
      this.log( "first aligning to ... " + nextOrientation );
      this.turnTo(nextOrientation);
    } else {
      this.log( "moving towards next target, forward" );
      this.popNext();
      this.visitAheadSector();
    }
  }
  
  private boolean nextIsBacktrack() {
    return this.stack.peek() < 0;
  }

  private boolean nextIsForward() {
    return this.stack.peek() > 0;
  }
  
  private int peekNext() {
    
    int next = this.stack.peek();
    if( next < 0 ) { next = (next + 1 ) * -1; }
    return next;
  }

  private int popNext() {
    int next = this.stack.pop();
    if( next < 0 ) { next = (next + 1 ) * -1; }
    return next;
  }

  private void visitAheadSector() {
    // if there's now access to the ahead sector, we're done ;-)
    if( ! this.canMoveAhead() ) { return; }

    // move into the unknown sector
    this.moveForward();

    // push an action to backtrack this step
    // unless we're already backtracking
    // backtracking means we've moved towards a Sector we've visited before
    if( ! this.visited.contains(this.getSector()) ) {
      this.addBacktracking();
      this.visited.add(this.getSector());
    } else {
      this.log("Backtracking...");
    }
  }

  // backtracking is marked with a negative bearing (and shifted -1)
  private void addBacktracking() {
    this.stack.push(-1 * (Bearing.reverse(this.getOrientation())+1));
  }
  
  private boolean canMoveAhead() {
    int atLocation = this.getOrientation();
    Sector sector  = this.getSector();
    
    if( sector.hasWall(atLocation) ) {
      // mark a wall, we can't move ahead
      sector.addWall(atLocation);
      return false;
    }
    // mark no wall, we can move ahead
    sector.removeWall(atLocation);
    // if the goal has a sector behind the open wall, add a new empty sector
    // if we don't already have it (createNeighbour does this check)
    sector.createNeighbour(atLocation);
    return true;
  }
}
