package penoplatinum.model.processor;

/**
 * GridUpdateProcessor
 * 
 * This modelprocessor adds new sectors to the wall-less boundaries of the grid
 * 
 * @author: Team Platinum
 */
import penoplatinum.grid.LinkedSector;
import penoplatinum.grid.Sector;

import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.Position;

public class UnknownSectorModelProcessor extends ModelProcessor {

  public UnknownSectorModelProcessor() {
    super();
  }

  public UnknownSectorModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }
  
  private Point prevPosition;
  private Bearing prevBearing;

  @Override
  public void work() {
    Model model = getModel();
    GridModelPart gridPart = GridModelPart.from(model);
    if(gridPart.getMyPosition().equals(prevPosition) && prevBearing == gridPart.getMyBearing()){
      return;
    }
    prevPosition = gridPart.getMyPosition();
    prevBearing = gridPart.getMyBearing();
    this.addNewSectors();
  }

  // if there are bearing without walls, providing access to unknown Sectors,
  // add such Sectors to the Grid
  private void addNewSectors() {
    GridModelPart gridPart = GridModelPart.from(this.getModel());
    Sector current = gridPart.getMySector();

    for (Bearing bearing : Bearing.NESW) {
      if( current.givesAccessTo(bearing) && ! current.hasNeighbour(bearing) ) {
        Sector neighbour = new LinkedSector();
        neighbour.setValue(5000);
        //Utils.Log("Adding wall: "+bearing);
        Point p = gridPart.getMyPosition();
        int left = Position.moveLeft(bearing, p.getX());
        int top = Position.moveTop(bearing, p.getY());
        gridPart.getMyGrid().add(neighbour, new Point(left, top));
      }
    }
  }
}
