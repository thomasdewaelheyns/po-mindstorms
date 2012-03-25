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


public interface Reporter {
  public Reporter useGatewayClient(GatewayClient client);
  public Reporter setRobot(Robot robot);
  public Reporter report();
}
