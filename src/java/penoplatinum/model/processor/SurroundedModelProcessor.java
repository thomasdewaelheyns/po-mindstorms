package penoplatinum.model.processor;

import penoplatinum.grid.Sector;
import penoplatinum.grid.agent.GhostAgent;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class SurroundedModelProcessor extends ModelProcessor {
  private int pacmanID;

  protected void work() {
    GridModelPart gridPart = GridModelPart.from(getModel());
    if(pacmanID >= gridPart.getPacmanID()){
      return;
    }
    pacmanID = gridPart.getPacmanID();
    
    Point pos = gridPart.getFullGrid().getPositionOf(gridPart.getPacmanAgent());
    Sector s = gridPart.getFullGrid().getSectorAt(pos);
    gridPart.pacmanSurrounded = true;
    for(Bearing b : Bearing.NESW){
      if(!s.knowsWall(b)){
        gridPart.pacmanSurrounded = false;
        break;
      }
      if(s.hasNoWall(b)){
        gridPart.pacmanSurrounded = false;
        break;
      }
      Point neighbour = gridPart.getFullGrid().getPositionOf(s.getNeighbour(b));
      if(gridPart.getFullGrid().getAgentAt(neighbour, GhostAgent.class) == null){
        gridPart.pacmanSurrounded = false;
        break;
      }
    }
  }
  
}
