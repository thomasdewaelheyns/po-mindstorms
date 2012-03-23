package penoplatinum.pacman;

/**
 * GhostProtocolHandler
 * 
 * Implementation of a MessageHandler, handling the Ghost Protocol.
 * TODO: integrate
 * 
 * @author: Team Platinum
 */
import penoplatinum.Config;
import penoplatinum.model.GhostModel;
import penoplatinum.util.Utils;
import penoplatinum.grid.Agent;
import penoplatinum.grid.Sector;
import penoplatinum.simulator.Bearing;
import penoplatinum.simulator.mini.Queue;
import penoplatinum.util.MyScanner;

public class GhostProtocolHandler implements ProtocolHandler {

  private static String version = "1.0-partial";
  // counter for received join commands
  private int joins = 0;
  // the queue we're communicating through
  private Queue queue;
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

  // accepts a string, parses it and dispatches it...
  @Override
  public void receive(String msg) {
    try {
      if (Config.PROTOCOL_USE_RETARDEDNEWLINE) {
        msg = msg.substring(0, msg.length() - 2);
      }

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
        } else if ("BARCODE".equals(command)) {
          commandHandler.handleBarcode(agentName, scanner.nextInt(), scanner.nextInt());
        } else if ("PACMAN".equals(command)) {
          commandHandler.handlePacman(agentName, scanner.nextInt(), scanner.nextInt());
        }
      }
    } catch (Exception e) {
      // badly formatted and other stupid issues with the protocol or 
      // formatting are handled here : NOT
      // COMMENT THE FOLLOWING LINE IN "PRODUCTION" environment ;-)
//      e.printStackTrace();
      if (Config.DEBUGMODE) {
        Utils.Log("Protocol error!! (" + msg + ")");
        e.printStackTrace();

      }
    }
  }

  private void handleJoin() {
    this.joins++;
    // TODO: if this.connected ... restart sequence

    // init conversation if we have seen 4 joins (we were first)
    if (!this.connected && this.joins >= 4) {
      this.begin();
    }
  }

  private void handleName(String agentName, String version) {

    // if we're still waiting for joins, we now can begin...
    if (!this.connected) {//&& this.joins < 4 ) 
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

    //TODO: this.agent.activate();
  }

  private void send(String msg)
  {
    if(Config.PROTOCOL_USE_RETARDEDNEWLINE)
    {
      msg = msg + "\\n";
    }
    this.queue.send(msg);
  }
  
  private void sendJoin() {
    send("JOIN");
  }

  private void sendName() {
    send(this.agent.getName() + " NAME "
            + GhostProtocolHandler.version);
  }

  @Override
  public void sendPosition() {
    send(this.agent.getName() + " POSITION "
            + this.agent.getSector().getLeft() + ","
            + this.agent.getSector().getTop());
  }

  // TODO: change North and South
  @Override
  public void sendDiscover(Sector sector) {

    send(this.agent.getName() + " DISCOVER "
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
  public void sendBarcode(int code, int bearing) {
    send(this.agent.getName() + " BARCODE "
            + code + " " + bearing);
  }

  @Override
  public void sendPacman() {
    send(this.agent.getName() + " PACMAN "
            + this.model.getGridPart().getPacmanAgent().getLeft() + ","
            + this.model.getGridPart().getPacmanAgent().getTop());
  }

  // keep a reference to the outgoing queue and JOIN
  @Override
  public void useQueue(Queue queue) {
    this.queue = queue;
    this.sendJoin();
  }
}
