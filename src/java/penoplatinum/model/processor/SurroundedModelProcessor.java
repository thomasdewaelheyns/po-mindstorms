package penoplatinum.model.processor;

import penoplatinum.grid.GridUtils;
import penoplatinum.grid.Sector;
import penoplatinum.grid.agent.GhostAgent;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class SurroundedModelProcessor extends ModelProcessor {
  private int pacmanID;

  protected void work() {
    GridModelPart gridPart = GridModelPart.from(getModel());
    if(pacmanID <= gridPart.getPacmanID()){
      return;
    }
    pacmanID = gridPart.getPacmanID();
    
    Point pos = gridPart.getFullGrid().getPositionOf(gridPart.getPacmanAgent());
    Sector s = gridPart.getFullGrid().getSectorAt(pos);
    for(Bearing b : Bearing.NESW){
      if(GridUtils.givesAccessTo(s,b)){
        continue;
      }
      Point neighbour = gridPart.getFullGrid().getPositionOf(s.getNeighbour(b));
      if(gridPart.getFullGrid().getAgentAt(neighbour, GhostAgent.class) != null){
        continue;
      }
      return;
    }
    gridPart.pacmanSurrounded = true;
  }
  
}
