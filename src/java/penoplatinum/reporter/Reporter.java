package penoplatinum.reporter;

/**
 * Reporter
 * 
 * interface for class that reports information about a Robot/Model/... 
 * using a GatewayClient
 * 
 * @author Team Platinum
 */

import penoplatinum.gateway.GatewayClient;
import penoplatinum.grid.Sector;
import penoplatinum.grid.agent.Agent;

import penoplatinum.robot.Robot;


public interface Reporter {
  public Reporter useGatewayClient(GatewayClient client);
  public Reporter reportFor(Robot robot);

  // the reporter can process updates to the information
  public Reporter reportModelUpdate();
  public Reporter reportSectorUpdate(Sector sector, String dashboardGridName);
  public Reporter reportValueUpdate(Sector sector);
  public Reporter reportAgentUpdate(Agent agent);

  public void reportCaptured();
}
