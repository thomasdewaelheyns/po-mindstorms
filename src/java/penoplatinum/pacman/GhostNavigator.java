package penoplatinum.pacman;

/**
 * GhostNavigator
 * 
 * Implementation of Navigator for Ghosts.
 * 
 * @author: Team Platinum
 */

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import penoplatinum.grid.Sector;
import penoplatinum.grid.Agent;
import penoplatinum.simulator.mini.Bearing;
import penoplatinum.simulator.Model;
import penoplatinum.pacman.GhostModel;

import penoplatinum.simulator.mini.Navigator;
// TEMP TRICK
import penoplatinum.simulator.mini.MiniSimulation;

public class GhostNavigator implements Navigator {
  private GhostModel model;
  private List<Integer> plan = new ArrayList<Integer>();
  
  public Navigator setModel(Model model) {
    this.model = (GhostModel)model;
    return this;
  }
  
  private void log(String msg) {
    System.out.printf( "[%10s] %2d,%2d / Navigator  : %s\n", 
                       this.model.getAgent().getName(),
                       this.model.getAgent().getLeft(),
                       this.model.getAgent().getTop(),
                       msg );
  }

  public int nextAction() {
    // TODO: add check if next planned action is still valid, else also
    //       create a new plan based on the new situation
    if( this.plan.isEmpty() ) {
      this.createNewPlan();
      this.log("Got a new Plan : " + this.plan );
    }
    if( this.plan.isEmpty() ) {
      throw new RuntimeException( "Out of plans ..." );
    }
    this.log("Executing plan: " + this.plan);
    return this.plan.remove(0);
  }

  public Boolean reachedGoal() {
    // TODO: implement some termination, we need it to announce
    //       the CAPTURE
    return false;
  }

  public double getDistance() {
    // not used, but if it were, it would always be half a Sector = 20cm
    return 20;
  }
  
  public double getAngle() {
    // not used, but if it were, it would always be a quarter of a Sector
    return 90;
  }

  // get the values of the 4 adjacent sectors and decide were to go
  private void createNewPlan() {
    int[] values = this.getadjacentSectorInfo();

    // if any of the moves brings us onto the 10000 pos, we hold our position
    for(int bearing=Bearing.N;bearing<=Bearing.W;bearing++) {
      if( values[bearing] == 10000 ) {
        this.plan.add(GhostAction.NONE);
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
    this.log( forMove + " out of " + Arrays.toString(moves) + " / " + count);
    
    // randomly don't do anything (20%)
    if(Math.random()*5==3) { forMove = Bearing.NONE; }

    if(forMove <= Bearing.NONE) {
      this.plan.add(GhostAction.NONE);
    }
    
    this.createActions(forMove);
  }

  // get the values of all 4 adjacent Sectors (N,E,S,W)
  // return the Sector's value IF it is accessible (there must be a Sector,
  // and there shouldn't be a wall in between).
  // if there is an agent on the adjacent Sector, we don't go there...
  private int[] getadjacentSectorInfo() {
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
        info[atLocation] = -1;
      } else {
        // TEMPORARY CHEATING TO SOLVE DETECT-OTHER-AGENT-AS-WALL PROBLEM
        // we don't want to allow multiple agents on the same sector
        Agent proxy = MiniSimulation.goalGrid.getAgent(agent.getName());
        int proxyOrigin = proxy.getOriginalBearing();
        int bearingOfProxy = Bearing.withOrigin(atLocation, proxyOrigin);
        Sector neighbour = proxy.getSector().getNeighbour(bearingOfProxy);
        if( neighbour != null && neighbour.hasAgent() ) {
          // System.out.println( agent.getName() + " : other agent @ " + atLocation + " == " + bearingOfProxy );
          // try { System.in.read(); } catch(Exception e) {}
          info[atLocation] = -1;
        }
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
  
  private void createActions(int target) {
    int current = this.model.getAgent().getBearing();
    
    if( target != current ) {
      int diff = target - current;
      if( Math.abs(diff) == 3 ) { diff /= -3; } // -3 => 1   3 => -1
    
      switch(diff) {
        case -2: this.plan.add(GhostAction.TURN_LEFT);
        case -1: this.plan.add(GhostAction.TURN_LEFT); break;
        case  2: this.plan.add(GhostAction.TURN_RIGHT);
        case  1: this.plan.add(GhostAction.TURN_RIGHT); break;
      }
      this.log( "turn needed: " + current + " -> " + target + " = " + diff );
    }
    
    // after turning, move forward
    this.plan.add(GhostAction.FORWARD);
  }
}
