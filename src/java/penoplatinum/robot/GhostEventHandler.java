package penoplatinum.robot;

import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.protocol.ExternalEventHandler;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class GhostEventHandler implements ExternalEventHandler {

  private GridModelPart gridPart;

  public GhostEventHandler(AdvancedRobot robot) {
    this.gridPart = GridModelPart.from(robot.getModel());
  }

  @Override
  public void handleActivation() {
    System.out.println("Activate!");
    gridPart.getMyAgent().activate();
  }

  @Override
  public void handleSectorInfo(String agentName, Point position, boolean knowsN, boolean n, boolean knowsE, boolean e, boolean knowsS, boolean s, boolean knwosW, boolean w) {
    Grid gridOfAgent = gridPart.getGridOf(agentName);
    Sector sector = gridOfAgent.getSectorAt(position);
  }

  @Override
  public void handleNewAgent(String agentName) {
  }

  @Override
  public void handleAgentInfo(String agentName, Point position, int value, Bearing bearing) {
    throw new UnsupportedOperationException("Not supported yet.");
    //TODO learn how barcodes work
    /*Agent agent = gridPart.getGridOf(agentName).getAgent(agentName);
    gridPart.getGridOf(agentName).moveTo(agent, position, bearing);
     * 
     */
  }

  @Override
  public void handleTargetInfo(String agentName, Point position) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void handleSendGridInformation() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void handleCaptured(String agentName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void handleRemoveAgent(String agentName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
