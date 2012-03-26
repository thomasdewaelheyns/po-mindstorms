package penoplatinum.model;

/**
 * Reporter
 * 
 * interface for class that reports information about a Robot/Model/... 
 * using a GatewayClient
 * 
 * @author Team Platinum
 */

import penoplatinum.gateway.GatewayClient;
import penoplatinum.simulator.Robot;

import penoplatinum.grid.Agent;
import penoplatinum.grid.Sector;


public interface Reporter {
  public Reporter useGatewayClient(GatewayClient client);
  public Reporter setRobot(Robot robot);
  public Reporter report();
  public Reporter reportWalls(Sector sector);
  public Reporter reportAgent(Agent agent);
}
