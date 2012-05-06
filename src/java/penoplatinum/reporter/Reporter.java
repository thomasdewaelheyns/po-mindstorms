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
import penoplatinum.grid.Agent;
import penoplatinum.grid.Sector;


public interface Reporter {
  public Reporter useGatewayClient(GatewayClient client);

  // the reporter can process updates to the information
  public Reporter reportModelUpdate(Agent agent);
  public Reporter reportSectorUpdate(Sector sector);
  public Reporter reportAgentUpdate(Agent agent);
}
