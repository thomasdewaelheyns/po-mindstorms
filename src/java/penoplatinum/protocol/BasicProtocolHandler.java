package penoplatinum.protocol;

import penoplatinum.gateway.GatewayClient;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.grid.agent.BarcodeAgent;
import penoplatinum.grid.agent.PacmanAgent;
import penoplatinum.util.Point;

public class BasicProtocolHandler implements ProtocolHandler {

  @Override
  public String getVersion() {
    return "2.3";
  }

  @Override
  public ProtocolHandler useGatewayClient(GatewayClient client) {
    return this;
  }

  @Override
  public ProtocolHandler useExternalEventHandler(ExternalEventHandler handler) {
    return this;
  }

  @Override
  public ProtocolHandler handleStart() {
    return this;
  }

  @Override
  public ProtocolHandler handleEnterSector(Sector sector) {
    return this;
  }

  @Override
  public ProtocolHandler handleFoundSector(Sector sector) {
    return this;
  }

  @Override
  public ProtocolHandler handleFoundAgent(Grid grid, BarcodeAgent agent) {
    return this;
  }

  @Override
  public ProtocolHandler handleFoundAgent(Grid grid, PacmanAgent agent) {
    return this;
  }

  @Override
  public ProtocolHandler handleResendData(Iterable<Sector> sectors, Point pacmanPoint, Point position) {
    return this;
  }

  @Override
  public void receive(String msg) {
  }

  @Override
  public String getName() {
    return "";
  }

  @Override
  public ProtocolHandler handleCaptured() {
    return this;
  }
  
}
