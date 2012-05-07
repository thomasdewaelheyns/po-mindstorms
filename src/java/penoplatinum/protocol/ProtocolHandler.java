package penoplatinum.protocol;

/**
 * This interface defines a handler for a protocol for exchanging information
 * with the outsideworld. It is used by the GatewayClient to parse incoming
 * messages and format outgoing messages.
 * 
 * @author Team Platinum
 */

import penoplatinum.gateway.GatewayClient;

import penoplatinum.grid.Grid;
import penoplatinum.grid.agent.BarcodeAgent;
import penoplatinum.grid.agent.PacmanAgent;
import penoplatinum.grid.Sector;

import penoplatinum.util.Point;


public interface ProtocolHandler {
  // what's the version of the protocol we're implementing
  public String getVersion();
  
  // interface to communicate with the outside world
  public ProtocolHandler useGatewayClient(GatewayClient client);
  public ProtocolHandler useExternalEventHandler(ExternalEventHandler handler);
  
  // functional commands to send out information about findings about the
  // environment
  // events that need to be handled by
  public ProtocolHandler handleStart();
  public ProtocolHandler handleEnterSector(Sector sector);
  public ProtocolHandler handleFoundSector(Sector sector);

  public ProtocolHandler handleFoundAgent(Grid grid, BarcodeAgent agent);
  public ProtocolHandler handleFoundAgent(Grid grid, PacmanAgent agent);
  public ProtocolHandler handleResendData(Iterable<Sector> sectors, Point pacmanPoint, Point position);
  
  public ProtocolHandler handleCaptured();

  // used to manage incoming messages
  public void receive(String msg);

  // callback to retrieve own name
  public String getName();
}
