package penoplatinum.model.processor;

import java.util.Random;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class ScrewGridUpModelProcessor extends ModelProcessor {
  private Point prevPosition;
  private Bearing prevBearing;

  Random rand = new Random(123);
  @Override
  protected void work() {
    Model model = getModel();
    GridModelPart gridPart = GridModelPart.from(model);
    if(gridPart.getMyPosition().equals(prevPosition) && prevBearing == gridPart.getMyBearing()){
      return;
    }
    prevPosition = gridPart.getMyPosition();
    prevBearing = gridPart.getMyBearing();
    
    if(rand.nextInt(100)<20){
      Grid grid = gridPart.getFullGrid();
      int minLeft = grid.getMinLeft(), minTop = grid.getMinTop();
      int x = rand.nextInt(grid.getWidth()) + minLeft;
      int y = rand.nextInt(grid.getHeight()) + minTop;
      Sector s = grid.getSectorAt(new Point(x, y));
      s.clearWall(Bearing.NESW.get(rand.nextInt(4)));
    }
  }
  
}
