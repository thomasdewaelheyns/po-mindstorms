package penoplatinum.protocol;

/**
 * This interface defines a handler for a protocol for exchanging information
 * with the outsideworld. It is used by the GatewayClient to parse incoming
 * messages and format outgoing messages.
 * 
 * @author Team Platinum
 */

import penoplatinum.grid.Sector;
import penoplatinum.grid.BarcodeAgent;
import penoplatinum.grid.PacmanAgent;

import penoplatinum.gateway.GatewayClient;


public interface ProtocolHandler {
  // interface to communicate with the outside world
  public ProtocolHandler useGatewayClient(GatewayClient client);
  public ProtocolHandler useExternalEventHandler(ExternalEventHandler handler);
  
  // functional commands to send out information about findings about the
  // environment
  // events that need to be handled by
  public ProtocolHandler handleStart();
  public ProtocolHandler handleEnterSector(Sector sector);
  public ProtocolHandler handleFoundSector(Sector sector);
  // TODO: find a way to turn this into ONE method
  public ProtocolHandler handleFoundAgent(Sector sector, BarcodeAgent agent);
  public ProtocolHandler handleFoundAgent(Sector sector, PacmanAgent agent);

  // used to manage incoming messages, called by GatewayClient
  public void receive(String msg);

  // callback to retrieve own name
  public String getName();
}
