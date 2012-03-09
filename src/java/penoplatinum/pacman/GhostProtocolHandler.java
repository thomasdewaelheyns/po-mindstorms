package penoplatinum.pacman;

/**
 * GhostProtocolHandler
 * 
 * Implementation of a MessageHandler, handling the Ghost Protocol.
 * TODO: integrate
 * 
 * @author: Team Platinum
 */
import penoplatinum.Utils;
import penoplatinum.grid.Agent;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.simulator.mini.Bearing;
import penoplatinum.simulator.mini.MessageHandler;
import penoplatinum.simulator.mini.Queue;


public class GhostProtocolHandler implements MessageHandler {

  private static String version = "1.0-partial";
  // counter for received join commands
  private int joins = 0;
  // the queue we're communicating through
  private Queue queue;
  private boolean connected = false;
  // a reference to the Agent
  private Agent agent;
  private final GhostModel model;

  // we need a least a reference to the Agent we're handling for
  // TODO: this should become a Model
  public GhostProtocolHandler(Agent agent, GhostModel model) {
    this.agent = agent;
    this.model = model;

  }

  // accepts a string, parses it and dispatches it...
  @Override
  public void receive(String msg) {
    try {
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
          this.handlePosition(agentName, scanner.nextInt(), scanner.nextInt());
        } else if ("DISCOVER".equals(command)) {
          this.handleDiscover(agentName, scanner.nextInt(), scanner.nextInt(),
                  scanner.nextInt(), scanner.nextInt(),
                  scanner.nextInt(), scanner.nextInt());
        } else if ("BARCODE".equals(command)) {
          this.handleBarcode(agentName, scanner.nextInt(), scanner.nextInt());
        }
      }
    } catch (Exception e) {
      // badly formatted and other stupid issues with the protocol or 
      // formatting are handled here : NOT
      // COMMENT THE FOLLOWING LINE IN "PRODUCTION" environment ;-)
//      e.printStackTrace();
      Utils.Log("WEEQSDJFMldj");
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

  private void handlePosition(String agentName, int x, int y) {
    // update the agent's position
    int bearing = Bearing.N; //TODO: 
    final Grid grid = model.getGrid(agentName);
    
    Sector sector = grid.getSector(x, y);
    if (sector == null) {
      sector = new Sector();
      sector.setCoordinates(x, y);
      grid.addSector(sector);
    }
    Agent agent = grid.getAgent(agentName);
    if (agent == null)
    {
      // Add the agent when it doesn't exist
      agent = new GhostAgent(agentName);
      grid.addAgent(agent);
    }

    grid.agentsNeedRefresh();
    agent.assignSector(sector, bearing);

    Utils.Log("Grid refresh!");
    grid.refresh(); //TODO: this shouldn't run on the robot
    


  }

  private void handleDiscover(String agentName, int x, int y,
          int n, int e, int s, int w) {

    final Grid grid = model.getGrid(agentName);
    Sector sector = grid.getSector(x, y);


    if (sector == null) {
      sector = new Sector();
      sector.setCoordinates(x, y);
      grid.addSector(sector);
    }


    int[] values = new int[]{n, e, s, w};


    for (int i = 0; i < 3; i++) {

      Boolean newVal = decodeTrit(values[i]);
//      Boolean oldVal = sector.hasWall(i);
//      if (newVal == oldVal) {
//        continue; // No changes
//      }
//      if (newVal == null) {
//        continue; // Remote has no information
//      }
//
//      if (oldVal == null) {
//        // Use remote information (do nothing)
//      } else {
//        // Conflicting information, set to unknown
//        newVal = null;
//      }
//
//
      if (newVal == null) {
        sector.clearWall(i);
      } else if (newVal) {
        sector.addWall(i);
      } else {
        sector.removeWall(i);
      }

    }


    // info set!! :P

    grid.refresh(); //TODO: this shouldn't run on the robot
    
  }

  private void handleBarcode(String agentName, int code, int bearing) {
    // tag the current sector of the agent with the given barcode
    // check if we have it too
    // if so, import the agents map in our grid,
    //        create translators to continously import its information
    //        in our own grid
  }

  private void sendJoin() {
    this.queue.send("JOIN");
  }

  private void sendName() {
    this.queue.send(this.agent.getName() + " NAME "
            + GhostProtocolHandler.version);
  }

  public void sendPosition() {
//    this.queue.send(this.agent.getName() + " POSITION "
//            + this.agent.getSector().getLeft() + ","
//            + this.agent.getSector().getTop());
  }

  // TODO: change North and South
  public void sendDiscover(Sector sector) {
    
//    this.queue.send(this.agent.getName() + " DISCOVER "
//            + sector.getLeft() + "," + sector.getTop() + " "
//            + this.makeTrit(sector.hasWall(Bearing.N)) + " "
//            + this.makeTrit(sector.hasWall(Bearing.E)) + " "
//            + this.makeTrit(sector.hasWall(Bearing.S)) + " "
//            + this.makeTrit(sector.hasWall(Bearing.W)));
  }

  private int makeTrit(Boolean wall) {
    return wall == null ? 2 : (wall ? 1 : 0);
  }

  private Boolean decodeTrit(int wall) {
    return wall == 2 ? null : (wall == 1);
  }

  public void sendBarcode() {
    this.queue.send(this.agent.getName() + " BARCODE "
            + "TODO" + " " + "TODO");
  }

  // keep a reference to the outgoing queue and JOIN
  public void useQueue(Queue queue) {
    this.queue = queue;
    this.sendJoin();
  }
}
