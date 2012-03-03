public class WallDetectorProcessor extends ModelProcessor {

  private boolean first = true;

  public WallDetectorProcessor() { super(); }
  public WallDetectorProcessor( ModelProcessor nextProcessor ) {
    super(nextProcessor);
  }

  // we only work with new SonarValues, which can only be obtained after
  // a moveForward and we're back in the middle of a new tile
  // TODO: abstract this, removing the "knowledge" about how the values
  // are obtained
  protected void work() {
    GhostModel model = (GhostModel)this.model;

    // clear the certainty of the current Sector
    model.getDetectedSector().clearCertainty();

    // only update when we have a complete set of sensorvalues
    if( ! model.hasNewSonarValues() ) { return; }
    
    Agent agent = model.getAgent();
    int bearing = agent.getBearing();

    Sector sector = new Sector();

    // front = bearing
    if( model.getFrontFreeDistance() < 35 ) {
      sector.addWall(bearing);
    } else {
      sector.removeWall(bearing);
    }
    
    // back = reverse(bearing) = no, I just came from there
    // TODO: find a way to solve the initial back-wall
    if( !this.first ) {
      sector.removeWall(Bearing.reverse(bearing));
    }
    first = false;
    
    // left = leftFrom(bearing)
    if( model.getLeftFreeDistance() < 35 ) {
      sector.addWall(Bearing.leftFrom(bearing));
    } else {
      sector.removeWall(Bearing.leftFrom(bearing));
    }

    // right = rightFrom(bearing)
    if( model.getRightFreeDistance() < 35 ) {
      sector.addWall(Bearing.rightFrom(bearing));
    } else {
      sector.removeWall(Bearing.rightFrom(bearing));
    }

    model.updateSector(sector);
    model.markSonarValuesProcessed();
  }
}
