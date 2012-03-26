package penoplatinum.pacman;

/**
 * GhostProtocolHandler
 * 
 * Implementation of a MessageHandler, handling the Ghost Protocol.
 * 
 * @author: Team Platinum
 */
import penoplatinum.Config;

import penoplatinum.model.GhostModel;

import penoplatinum.grid.Agent;
import penoplatinum.grid.Sector;

import penoplatinum.simulator.Bearing;

import penoplatinum.gateway.GatewayClient;

import penoplatinum.util.Utils;
import penoplatinum.util.MyScanner;

public class GhostProtocolHandler implements ProtocolHandler {

  public final static String version = "1.0-partial";
  // counter for received join commands
  private int joins = 0;
  // the client we're using to communicate with the gateway
  private GatewayClient client;
  private boolean connected = false;
  // a reference to the Agent
  private Agent agent;
  private final GhostModel model;
  private final GhostProtocolCommandHandler commandHandler;

  // we need a least a reference to the Agent we're handling for
  // TODO: this should become a Model
  public GhostProtocolHandler(GhostModel model, GhostProtocolCommandHandler commandHandler) {
    this.agent = model.getGridPart().getAgent();
    this.model = model;
    this.commandHandler = commandHandler;
  }

  // keep a reference to the GatewayClient to send messages
  public GhostProtocolHandler useGatewayClient(GatewayClient client) {
    this.client = client;
    this.sendJoin();
    return this;
  }

  // accepts a string, parses it and dispatches it...
  public void receive(String msg) {
    // System.out.println( "RECEIVE: " + this.agent.getName() + " : " + msg );
    try {

      msg = msg.substring(0,msg.length()-1);
      
      MyScanner scanner = new MyScanner(msg);//.useDelimiter("[ ,]");
      String agentName = scanner.next();

      if ("JOIN".equals(agentName) && !scanner.hasNext()) {
        this.handleJoin();
      } else if (this.agent.getName().equals(agentName)) {
        // skip our own messages
      } else {
        String command = scanner.next();
        if ("NAME".equals(command)) {
          this.handleName(agentName, scanner.next());
        } else if ("POSITION".equals(command)) {
          commandHandler.handlePosition(agentName, scanner.nextInt(), scanner.nextInt());
        } else if ("DISCOVER".equals(command)) {
          commandHandler.handleDiscover(agentName, scanner.nextInt(), scanner.nextInt(),
                  scanner.nextInt(), scanner.nextInt(),
                  scanner.nextInt(), scanner.nextInt());
        } else if ("BARCODEAT".equals(command)) {
          commandHandler.handleBarcodeAt(agentName, scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt()-1);
        } else if ("PACMAN".equals(command)) {
          commandHandler.handlePacman(agentName, scanner.nextInt(), scanner.nextInt());
        }
      }
    } catch (Exception e) {
      if (Config.DEBUGMODE) {
        Utils.Log("Protocol error!! (" + msg + ")");
        e.printStackTrace();
      }
    }
  }

  private void handleJoin() {
    // System.out.println( "handleJoin " + this.agent.getName() );
    this.joins++;
    // TODO: if this.connected ... restart sequence

    // init conversation if we have seen 4 joins (we were first)
    if (!this.connected) {
      if (this.joins >= 4) {
        this.begin();
      }
    }
  }

  private void handleName(String agentName, String version) {
    // if we're still waiting for joins, we now can begin...
    if (!this.connected && this.joins < 4) {
      this.begin();
    }

    // TODO: handle name collisions
    // Add a new agent, or ignore when already added
    // TODO: a new agent, create a grid to store its information
  }

  private void begin() {
    this.connected = true;
    this.joins = 4;
    this.sendName();

    this.agent.activate();
  }

  private void send(String msg) {
    msg = msg + "\n";
    this.client.send(msg, Config.BT_GHOST_PROTOCOL);
  }

  private void sendJoin() {
    this.send("JOIN");
  }

  private void sendName() {
    this.send(this.agent.getName() + " NAME " + GhostProtocolHandler.version);
  }
  int prevLeft = -1;
  int prevTop = -1;

  @Override
  public void sendPosition() {
    if (this.agent.getSector().getLeft() == prevLeft && this.agent.getSector().getTop() == prevTop) {
      return;
    }
    //TODO search origin of this bug
    this.prevLeft = this.agent.getSector().getLeft();
    this.prevTop = this.agent.getSector().getTop();
    send(this.agent.getName() + " POSITION "
            + this.agent.getSector().getLeft() + ","
            + this.agent.getSector().getTop());
  }

  // TODO: change North and South
  @Override
  public void sendDiscover(Sector sector) {

    this.send(this.agent.getName() + " DISCOVER "
            + sector.getLeft() + "," + sector.getTop() + " "
            + this.makeTrit(sector.hasWall(Bearing.N)) + " "
            + this.makeTrit(sector.hasWall(Bearing.E)) + " "
            + this.makeTrit(sector.hasWall(Bearing.S)) + " "
            + this.makeTrit(sector.hasWall(Bearing.W)));
  }

  private int makeTrit(Boolean wall) {
    return wall == null ? 2 : (wall ? 1 : 0);
  }

  public static Boolean decodeTrit(int wall) {
    return wall == 2 ? null : (wall == 1);
  }

  @Override
  public void sendBarcodeAt(int left, int top, int code, int bearing) {
    bearing = bearing + 1;
    send(this.agent.getName() + " BARCODEAT "
            + left + "," + top + " " + code + " " + bearing);
  }

  @Override
  public void sendPacman() {
    send(this.agent.getName() + " PACMAN "
            + this.model.getGridPart().getPacmanAgent().getLeft() + ","
            + this.model.getGridPart().getPacmanAgent().getTop());
  }
}
