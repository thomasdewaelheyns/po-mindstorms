package penoplatinum.model.processor;

import penoplatinum.grid.Sector;
import penoplatinum.grid.agent.GhostAgent;
import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class SurroundedModelProcessor extends ModelProcessor {
  
  private Point prevPosition = new Point(0,0);
  private Bearing prevBearing = Bearing.N;

  protected void work() {
    Model model = getModel();
    GridModelPart gridPart = GridModelPart.from(model);
    if(gridPart.getMyPosition().equals(prevPosition) && prevBearing == gridPart.getMyBearing()){
      return;
    }
    prevPosition = gridPart.getMyPosition();
    prevBearing = gridPart.getMyBearing();
    
    Point pos = gridPart.getFullGrid().getPositionOf(gridPart.getPacmanAgent());
    Sector s = gridPart.getFullGrid().getSectorAt(pos);
    for(Bearing b : Bearing.NESW){
      if(s.givesAccessTo(b)){
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
