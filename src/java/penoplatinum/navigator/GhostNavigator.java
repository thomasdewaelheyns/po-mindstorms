package penoplatinum.pacman;

/**
 * GhostNavigator
 * 
 * Implementation of Navigator for Ghosts.
 * 
 * @author: Team Platinum
 */

import penoplatinum.model.GhostModel;
import java.util.List;
import java.util.ArrayList;

import java.util.Random;
import penoplatinum.grid.Sector;
import penoplatinum.grid.Agent;
import penoplatinum.simulator.Bearing;
import penoplatinum.simulator.Model;

import penoplatinum.simulator.Navigator;

public class GhostNavigator implements Navigator {
  private GhostModel model;
  private List<Integer> plan = new ArrayList<Integer>();
  private Random r = new Random(1);
  
  public Navigator setModel(Model model) {
    this.model = (GhostModel)model;
    return this;
  }

  public int nextAction() {
    if( this.plan.size() == 0 ) {
      this.createNewPlan();
    }

    return this.plan.remove(0);
  }

  public Boolean reachedGoal() {
    // TODO: implement some termination, we need it to announce the CAPTURE
    return false;
  }

  // get the values of the 4 adjacent sectors and decide were to go
  private void createNewPlan() {
    int[] values = this.getadjacentSectorInfo();

    
    // move towards the higher ground/scent/value
    int max = this.getMax(values);
    // if any of the moves brings us onto the 10000 pos, we hold our position
    if(max == PacmanAgent.VALUE){
      this.plan.add(GhostAction.NONE);
      return;
    }
    
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
    int forMove = moves[(int)(r.nextDouble()*count)];
//    forMove = moves[0];
//    this.log( forMove + " out of " + Arrays.toString(moves) + " / " + count);
    
    // randomly don't do anything (20%)
    if((int)(r.nextDouble()*5)==3) { forMove = Bearing.NONE; }

    if(forMove <= Bearing.NONE) {
      this.plan.add(GhostAction.NONE);
      return;
    }
    
    this.createActions(forMove);
    
    if( this.plan.size() < 1 ) {
      throw new RuntimeException("couldn't create a plan...");
    }
  }

  // get the values of all 4 adjacent Sectors (N,E,S,W)
  // return the Sector's value IF it is accessible (there must be a Sector,
  // and there shouldn't be a wall in between).
  // if there is an agent on the adjacent Sector, we don't go there...
  private int[] getadjacentSectorInfo() {
    Agent  agent  = this.model.getGridPart(). getAgent();
    Sector sector = agent.getSector();

    Boolean hasNeighbour, hasWall;

    int[] info = {-1, -1, -1, -1};
    for(int atLocation=Bearing.N; atLocation<=Bearing.W; atLocation++ ) {
      hasNeighbour = sector.hasNeighbour(atLocation);
      hasWall      = sector.hasWall(atLocation);
      hasWall      = hasWall != null && hasWall;
      info[atLocation] = ! hasNeighbour || hasWall ?
                         -1 : sector.getNeighbour(atLocation).getValue();
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
  
  private void createActions(int target) {
    int current = this.model.getGridPart().getAgent().getBearing();
    
    if( target != current ) {
      int diff = target - current;
      if( Math.abs(diff) == 3 ) { diff /= -3; } // -3 => 1   3 => -1
    
      switch(diff) {
        case -2: this.plan.add(GhostAction.TURN_LEFT);
        case -1: this.plan.add(GhostAction.TURN_LEFT); break;
        case  2: this.plan.add(GhostAction.TURN_RIGHT);
        case  1: this.plan.add(GhostAction.TURN_RIGHT); break;
      }
//      this.log( "turn needed: " + current + " -> " + target + " = " + diff );
    }
    
    // after turning, move forward
    this.plan.add(GhostAction.FORWARD);
  }
}
