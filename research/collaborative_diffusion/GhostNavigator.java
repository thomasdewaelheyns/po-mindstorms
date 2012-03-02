import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class GhostNavigator implements Navigator {
  private GhostModel model;
  
  // get the values of the 4 adjectant sectors and decide were to go
  public List<Integer> nextActions() {
    int[] values = this.getAdjectantSectorInfo();

    // if any of the moves brings us onto the 1000 pos, we hold our position
    for(int bearing=Bearing.N;bearing<=Bearing.W;bearing++) {
      if( values[bearing] == -1000 ) {
        return Arrays.asList( GhostAction.NONE );
      }
    }
    
    // else move towards the higher ground/scent/value
    int max = this.getMax(values);
    
    // retain moves with highest value
    int[] moves = {-1, -1, -1, -1};
    int count = 0;
    for( int i=Bearing.N; i<=Bearing.W; i++ ) {
      if( values[i] == max ) {
        moves[count] = i;
        count++;
      }
    }
    
    // TODO: maybe take into account the move with least turns ?
    
    // choose randomly one of the best moves and create the required actions
    int forMove = moves[(int)(Math.random()*count)];
    System.out.println( "NAVIGATOR: " + forMove );
    return this.createActions(forMove);
  }

  // get the values of all 4 adjectant Sectors (N,E,S,W)
  // return the Sector's value IF it is accessible (there must be a Sector,
  // and there shouldn't be a wall in between.
  // if there is an agent on the adjectant Sector, we subtract a lot, making
  // it not "nice" to go there 
  // TODO: maybe turn an agent also in a simple, absolute NO-GO ?
  private int[] getAdjectantSectorInfo() {
    Agent  agent  = this.model.getAgent();
    Sector sector = agent.getSector();

    Boolean hasNeighbour, hasWall;

    int[] info = {-1, -1, -1, -1};
    for(int atLocation=Bearing.N; atLocation<=Bearing.W; atLocation++ ) {
      hasNeighbour = sector.hasNeighbour(atLocation);
      hasWall      = sector.hasWall(atLocation);
      hasWall      = hasWall != null && hasWall;
      info[atLocation] = ! sector.hasNeighbour(atLocation) || hasWall ?
                         -1 : sector.getNeighbour(atLocation).getValue();
      // if we know there is an agent on the next sector, lower the value
      // drastically
      if( sector.hasNeighbour(atLocation) && 
          sector.getNeighbour(atLocation).hasAgent() )
      {
        info[atLocation] -= 2000;
      }
    }

    return info;
  }
  
  private int getMax(int[] values) {
    // find max
    int max = 0;
    for( int value : values ) {
      if( value > max ) { max = value; }
    }
    return max;
  }
  
  private List<Integer> createActions(int target) {
    List<Integer> actions = new ArrayList<Integer>();
    int current = this.model.getAgent().getBearing();
    
    if( target != current ) {
      int diff = target - current;
      if( Math.abs(diff) == 3 ) { diff /= 3; } // -3 => 1   3 => -1
    
      switch(diff) {
        case -2: actions.add(GhostAction.TURN_LEFT);
        case -1: actions.add(GhostAction.TURN_LEFT); break;
        case  2: actions.add(GhostAction.TURN_RIGHT);
        case  1: actions.add(GhostAction.TURN_RIGHT); break;
      }
      System.out.println( "ACTIONS TURN: " + current + " -> " + target + " = " + diff );
    }
    
    // after turning, move forward
    actions.add(GhostAction.FORWARD);
    return actions;
  }

  public Boolean reachedGoal() {
    // TODO: implement some termination, we need it to announce
    //       the CAPTURE
    return false;
  }

  public int nextAction() {
    throw new RuntimeException( "Not supported, use nextActions()" );
  }

  public double getDistance() {
    // not used, but if it were, it would always be half a Sector = 20cm
    return 20;
  }
  
  public double getAngle() {
    // not used, but if it were, it would always be a quarter of a Sector
    return 90;
  }
  
  public Navigator setModel(Model model) {
    this.model = (GhostModel)model;
    return this;
  }
}
