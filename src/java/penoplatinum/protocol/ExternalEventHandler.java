package penoplatinum.protocol;

/**
 * Interface for handlers of ExternalEvents.
 * These are received by a ProtocolHandler, which dispatches them to this 
 * handler. This is normally the actual Robot.
 * 
 * @author Team Platinum
 */
 
import penoplatinum.grid.Sector;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;


public interface ExternalEventHandler {
  public void handleActivation();
  public void handleSectorInfo(String agentName, Point position, 
                               Boolean n, Boolean e, Boolean s, Boolean w);
  public void handleNewAgent(String agentName);
  public void handleAgentInfo(String agentName, Point position, 
                              int value, Bearing bearing);
  public void handleTargetInfo(String agentName, Point position);
}
