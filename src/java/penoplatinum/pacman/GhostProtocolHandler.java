package penoplatinum.pacman;

/**
 * GhostProtocolHandler
 * 
 * Implementation of a MessageHandler, handling the Ghost Protocol.
 * TODO: integrate
 * 
 * @author: Team Platinum
 */

import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

public class GhostProtocolHandler implements MessageHandler {
  private static String version = "1.0-partial";

  // counter for received join commands
  private int joins = 0;
  
  // the queue we're communicating through
  private Queue queue;
  private boolean connected = false;
  // a reference to the Agent
  private Agent agent;

  // we need a least a reference to the Agent we're handling for
  // TODO: this should become a Model
  public GhostProtocolHandler(Agent agent) {
    this.agent = agent;
  }
  
  // keep a reference to the outgoing queue and JOIN
  public void useQueue(Queue queue) {
    this.queue = queue;
    this.sendJoin();
  }
  
  // accepts a string, parses it and dispatches it...
  public void receive(String msg) {
    try {
      Scanner scanner = new Scanner(msg).useDelimiter("[ ,]");
      String agentName = scanner.next();
      if( "JOIN".equals(agentName) && !scanner.hasNext() ) {
        this.handleJoin();
      } else if( this.agent.getName().equals(agentName) ) {
        // skip our own messages
      } else {
        String command = scanner.next();
        if( "NAME".equals(command) ) {
          this.handleName(agentName, scanner.next());
        } else if( "POSITION".equals(command) ) {
          this.handlePosition(agentName, scanner.nextInt(), scanner.nextInt());
        } else if( "DISCOVER".equals(command) ) {
          this.handleDiscover(agentName, scanner.nextInt(), scanner.nextInt(),
                                         scanner.nextInt(), scanner.nextInt(),
                                         scanner.nextInt(), scanner.nextInt());
        } else if( "BARCODE".equals(command) ) {
          this.handleBarcode(agentName, scanner.nextInt(), scanner.nextInt());
        }
      }
    } catch(Exception e) {
      // badly formatted and other stupid issues with the protocol or 
      // formatting are handled here : NOT
      // COMMENT THE FOLLOWING LINE IN "PRODUCTION" environment ;-)
      e.printStackTrace();
    }
  }
  
  private void handleJoin() {
    this.joins++;
    // TODO: if this.connected ... restart sequence

    // init conversation if we have seen 4 joins (we were first)
    if( ! this.connected && this.joins >= 4 ) { 
      this.begin();
    }
  }
  
  private void handleName(String agentName, String version) {
    // if we're still waiting for joins, we now can begin...
    if( ! this.connected && this.joins < 4 ) {
      this.begin();
    } else {
      // TODO: a new agent, create a grid to store its information
    }
  }

  private void begin() {
    this.connected = true;
    this.joins = 4;
    this.sendName();
    this.agent.activate();
  }

  private void handlePosition(String agentName, int x, int y) {
    // update the agent's position
    
  }
  
  private void handleDiscover(String agentName, int x, int y, 
                              int n, int e, int s, int w)
  {
    // add a sector with the provided wall information
    
  }
  
  private void handleBarcode (String agentName, int code, int bearing) {
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
    this.queue.send(this.agent.getName() + " NAME " + 
                    GhostProtocolHandler.version);
  }
  
  public void sendPosition() {
    this.queue.send(this.agent.getName() + " POSITION " + 
                    this.agent.getSector().getLeft() + "," + 
                    this.agent.getSector().getTop() );
  }
  
  // TODO: change North and South
  public void sendDiscover(Sector sector) {
    this.queue.send(this.agent.getName() + " DISCOVER " + 
                    sector.getLeft() + "," + sector.getTop() + " " + 
                    this.makeTrit(sector.hasWall(Bearing.N)) + " " + 
                    this.makeTrit(sector.hasWall(Bearing.E)) + " " + 
                    this.makeTrit(sector.hasWall(Bearing.S)) + " " + 
                    this.makeTrit(sector.hasWall(Bearing.W)));
  }

  private int makeTrit(Boolean wall) {
    return wall == null ? 2 : ( wall ? 1 : 0 );
  }

  public void sendBarcode() {
    this.queue.send(this.agent.getName() + " BARCODE " + 
                    "TODO" + " " + "TODO");
  }
}
