package penoplatinum.protocol;

/**
 * GhostProtocolHandler
 * 
 * Implementation of a MessageHandler, handling the Ghost Protocol.
 * 
 * @author: Team Platinum
 */

import penoplatinum.Config;

import penoplatinum.grid.Agent;
import penoplatinum.grid.GhostAgent;
import penoplatinum.grid.PacmanAgent;
import penoplatinum.grid.BarcodeAgent;
import penoplatinum.grid.Sector;

import penoplatinum.gateway.GatewayClient;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.Utils;
import penoplatinum.util.Scanner;


public abstract class GhostProtocolHandler implements ProtocolHandler {

  // this is the version we're implementing
  private final static String VERSION   = "2.0";
  private final static int    MIN_JOINS = 4;

  // counter for received join commands
  private int joins = 0;
  // indicates if we've successfully joined
  private boolean joined = false;

  // the client we're using to communicate with the gateway
  private GatewayClient client;
  private ExternalEventHandler eventHandler;


  // keep a reference to the GatewayClient to send messages
  public GhostProtocolHandler useGatewayClient(GatewayClient client) {
    this.client = client;
    return this;
  }
  
  public ProtocolHandler useExternalEventHandler(ExternalEventHandler handler) {
    this.eventHandler = handler;
    return this;
  }
  
  // accepts a string, parses it and dispatches it...
  public void receive(String msg) {
    // strip off the newline
    msg = msg.substring(0, msg.length() -1);
    Scanner scanner = new Scanner(msg);

    String agentName = scanner.next();
    if( "JOIN".equals(agentName) && ! scanner.hasNext() ) {
      this.handleJoin();
    } else {
      String command = scanner.next();
      if( "NAME".equals(command) ) {
        this.handleName(agentName, scanner.next());
      } else if( "POSITION".equals(command) ) {
        this.handlePosition(agentName, 
                            new Point(scanner.nextInt(), scanner.nextInt()));
      } else if ("DISCOVER".equals(command)) {
        this.handleDiscover(agentName, 
                            new Point(scanner.nextInt(), scanner.nextInt()),
                            scanner.nextInt(), scanner.nextInt(),
                            scanner.nextInt(), scanner.nextInt());
      } else if ("BARCODEAT".equals(command)) {
        this.handleBarcodeAt(agentName, 
                             new Point(scanner.nextInt(), scanner.nextInt()),
                             scanner.nextInt(),
                             this.translateDirectionToBearing(scanner.nextInt()));
      } else if ("PACMAN".equals(command)) {
        this.handlePacman(agentName, 
                          new Point(scanner.nextInt(), scanner.nextInt()));
      }
    }
  }
  
  public ProtocolHandler handleStart() {
    this.sendJoin();
    return this;
  }

  // when we enter a new sector, we need to send out our position
  public ProtocolHandler handleEnterSector(Sector sector) {
    this.sendPosition(sector.getPosition());
    return this;
  }

  // when we find a new sector, we need to send out discover information
  public ProtocolHandler handleFoundSector(Sector sector) {
    this.sendDiscover(sector.getPosition(),
                      sector.hasWall(Bearing.N), sector.hasWall(Bearing.E),
                      sector.hasWall(Bearing.S), sector.hasWall(Bearing.W));
    return this;
  }

  // there are different types of agents:  
  // - Pacman
  public ProtocolHandler handleFoundAgent(Sector sector, PacmanAgent agent) {
    this.sendPacman(sector.getPosition());
    return this;
  }

  // - Barcode
  public ProtocolHandler handleFoundAgent(Sector sector, BarcodeAgent agent) {
    this.sendBarcodeAt(sector.getPosition(), agent.getValue(),
                       agent.getBearing());
    return this;
  }
  
  // internal implementation of the actual protocol
  
  // SENDING
  
  // example: JOIN
  private void sendJoin() {
    this.send("JOIN");
  }

  // example: platinum NAME 2.0
  private void sendName() {
    this.send(this.getName() + " NAME " + VERSION );
  }
  
  private void sendPosition(Point position) {
    this.send(this.getName() + " POSITION " + 
              this.translateToExternalFormat(position));
  }

  // example: platinum DISCOVER 10,13
  private void sendDiscover(Point position, 
                            Boolean n, Boolean e, Boolean s, Boolean w)
  {
    this.send(this.getName() + " DISCOVER " +
              this.translateToExternalFormat(position) + " " + 
              this.encodeTrit(n) + " " + this.encodeTrit(e) + " " +
              this.encodeTrit(s) + " " + this.encodeTrit(w) );
  }

  // example: platinum PACMAN 10,13
  private void sendPacman(Point position) {
    this.send(this.getName() + " PACMAN " + 
              this.translateToExternalFormat(position));
  }
  
  // example: platinum BARCODEAT 10,13 24 2
  private void sendBarcodeAt(Point position, int code, Bearing bearing) {
    this.send(this.getName() + " BARCODEAT " + 
              this.translateToExternalFormat(position) + " " + 
              code + " " +
              this.translateBearingToDirection(bearing));
  }
  
  // we use a left/top oriented coordinate system, the outside world uses
  // a system where the Y-axis points up
  // invert the Y-axis
  private Point translateToExternalFormat(Point point) {
    return new Point(point.getX(), -1 * point.getY());
  }

  // we use a left/top oriented coordinate system, the outside world uses
  // a system where the Y-axis points up
  // invert the Y-axis
  private Point translateToInternalFormat(Point point) {
    return new Point(point.getX(), -1 * point.getY());
  }

  // 1=N>S 2=E>W 3=S>N 4=W>E  
  private int translateBearingToDirection(Bearing bearing) {
    switch(bearing) {
      case N: return 1;
      case E: return 2;
      case S: return 3;
      case W: return 4;
    }
    throw new RuntimeException( "Invalid Bearing: " + bearing );
  }

  private Bearing translateDirectionToBearing(int direction) {
    switch(direction) {
      case 1: return Bearing.N;
      case 2: return Bearing.E;
      case 3: return Bearing.S;
      case 4: return Bearing.W;
    }
    return Bearing.UNKNOWN;
    // let's not throw an Exception here, this is based on other team's input
    // we can't trust them and don't want to provide them with a DOS tool ;-)
    // throw new RuntimeException( "Invalid direction: " + direction );
  }

  private void send(String msg) {
    this.client.send(msg + "\n", Config.BT_GHOST_PROTOCOL);
  }

  // RECEIVING

  // we need to count incoming JOINs, after MIN_JOINS we can begin
  private void handleJoin() {
    this.joins++;

    // init conversation if we have seen 4 joins (we were first)
    if( ! this.joined && this.joins >= MIN_JOINS ) {
      this.begin();
    }
  }

  private void handleName(String agentName, String version) {
    // if we're still waiting for joins, we now can begin...
    if( ! this.joined && this.joins < MIN_JOINS ) {
      this.begin();
    }

    // raise new Agent event
    this.eventHandler.handleNewAgent(agentName);
  }
  
  private void handlePosition(String agentName, Point position) {
    this.eventHandler.handleAgentInfo(agentName,
                                      this.translateToInternalFormat(position),
                                      0, Bearing.UNKNOWN);
  }
  
  private void handleDiscover(String agentName, Point position,
                              int n, int e, int s, int w )
  {
    // raise SectorInfo event
    this.eventHandler.handleSectorInfo(agentName, 
                                       this.translateToInternalFormat(position), 
                                       this.decodeTrit(n), this.decodeTrit(e),
                                       this.decodeTrit(s), this.decodeTrit(w));
  }
  
  private void handleBarcodeAt(String agentName, Point position, int code,
                               Bearing direction)
  {
    // raise AgentInfo event
    this.eventHandler.handleAgentInfo(agentName,
                                      this.translateToInternalFormat(position),
                                      code, direction);
  }
  
  private void handlePacman(String agentName, Point position) {
    // raise AgentInfo event
    this.eventHandler.handleTargetInfo(agentName, 
                                      this.translateToInternalFormat(position));
  }

  private void begin() {
    this.joined = true;
    this.joins  = MIN_JOINS;

    // send out own name
    this.sendName();
    
    // raise the Activtion event
    this.eventHandler.handleActivation();
  }

  private int encodeTrit(Boolean wall) {
    return wall == null ? 2 : (wall ? 1 : 0);
  }

  protected static Boolean decodeTrit(int wall) {
    return wall == 2 ? null : (wall == 1);
  }

  // abstract callback to retrieve own name
  public abstract String getName();
}
