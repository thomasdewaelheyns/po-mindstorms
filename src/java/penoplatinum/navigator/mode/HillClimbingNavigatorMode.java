package penoplatinum.navigator.mode;

/**
 * HillClimbingNavigatorMode
 *
 * Implements a NavigatorMode that uses Hill Climbing base on the values of
 * the Sectors.
 *
 * @author Team Platinum
 */

import java.util.List;
import java.util.ArrayList;

import penoplatinum.util.Bearing;


import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;

import penoplatinum.navigator.action.*;


public class HillClimbingNavigatorMode implements NavigatorMode {

  protected GridModelPart grids;

  public HillClimbingNavigatorMode(Model model) {
    this.grids = GridModelPart.from(model);
  }

  // this NavigatorMode still lacks a functional Goal. this needs to be
  // implemented by overriding this never ending goal.
  public boolean reachedGoal() { return false; }

  // a plan consists of looking at the four neighbour Sectors on the Grid
  // and moving towards the one with the highest value
  public final List<NavigatorAction> createNewPlan() {
    // first trigger the update of the values using the collaborate diffusion
    // algorithm
    this.grids.refreshMyGrid();

    return this.constructPlan(this.chooseBestBearing());
  }
  
  private Bearing chooseBestBearing() {
    int highestValue = -1, value;
    Bearing bestBearing = Bearing.UNKNOWN;

    // find highest value and associated bearing
    for(Bearing bearing : Bearing.NESW) {
      value = this.getValueAt(bearing);
      // favour the first found highest value
      if( value > highestValue ) {
        highestValue = value;
        bestBearing = bearing;
      }
    }
    return bestBearing;
  }

  // get the value of an adjacent sector
  // return the Sector's value IF it is accessible (there must be a Sector,
  // and there shouldn't be a wall in between).
  private int getValueAt(Bearing atLocation) {
    if( this.grids.getMySector().givesAccessTo(atLocation) ) {
      return this.grids.getMySector().getNeighbour(atLocation).getValue();
    }
    return -1; // TODO: remove magic number ?
  }
  
  private List<NavigatorAction> constructPlan(Bearing bestBearing) {
    List<NavigatorAction> plan = new ArrayList<NavigatorAction>();

    // not one neighbour has a value above -1 ???? okay ... but this is weird
    if( bestBearing == Bearing.UNKNOWN ) { return plan; }

    // turn towards the bestBearing
    switch(this.getMyBearing().to(bestBearing)) {
      case L90:
        plan.add( new TurnLeftNavigatorAction(this.grids) );
        break;
      case R90:
        plan.add( new TurnRightNavigatorAction(this.grids) );
        break;
      case R180: 
      case L180:
        plan.add( new TurnRightNavigatorAction(this.grids) );
        plan.add( new TurnRightNavigatorAction(this.grids) );
        break;
    }
    // and move forward into the Sector
    plan.add( new ForwardNavigatorAction(this.grids) );
    return plan;
  }
  
  private Bearing getMyBearing() {
    return this.grids.getMyBearing();
  }
}
