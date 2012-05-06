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

import penoplatinum.grid.agent.Agent;
import penoplatinum.grid.Sector;


public interface Reporter {
  public Reporter useGatewayClient(GatewayClient client);

  // the reporter can process updates to the information
  public Reporter reportModelUpdate();
  public Reporter reportSectorUpdate(Sector sector);
  public Reporter reportAgentUpdate(Agent agent);

  // the reporter can process events
  public Reporter reportEnterSector(Agent me, Sector sector);
  public Reporter reportFoundSector(Agent me, Sector sector);
  public Reporter reportFoundAgent(Agent me, Sector sector, Agent agent);
}
