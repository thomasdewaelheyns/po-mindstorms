public class GridUpdateProcessor extends ModelProcessor {

  public GridUpdateProcessor() { super(); }
  public GridUpdateProcessor( ModelProcessor nextProcessor ) {
    super(nextProcessor);
  }

  // update the agent
  protected void work() {
    this.updateWallInfo();
    this.addNewSectors();
    this.repositionAgent();
  }

  // update the current Sector on the Grid to reflect the currently selected
  private void updateWallInfo() {
    GhostModel model = (GhostModel)this.model;
    model.getCurrentSector().addWalls(model.getDetectedSector().getWalls());
  }

  // if there are bearing without walls, providing access to unknown Sectors,
  // add such Sectors to the 
  private void addNewSectors() {
    
  }
  
  // remove the agent from its "current" position and put him on the newly
  // detected position based on its relative position
  private void repositionAgent() {
    Agent  agent   = ((GhostModel)this.model).getAgent();
    Sector current = agent.getSector();
    Sector target  = current.getGrid()
                            .getSector(agent.getLeft(), agent.getTop());

    current.removeAgent();
    target.putAgent(agent, agent.getBearing());
  }
}
