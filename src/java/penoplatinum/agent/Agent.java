package penoplatinum.agent;

/**
 * Agent
 * 
 * Connects to a Robot and dispatches all incoming information to log4j.
 *
 * Author: Team Platinum
 */

import org.apache.log4j.Logger;

public class Agent {
  // setup the logger
  static Logger log = Logger.getLogger(Agent.class.getName());

  // the connection to the Robot
  BluetoothConnection source;
  
  // connects to a Robot (by bluetooth name => currently ignored)
  public Agent connect(String name) {
    this.source = new BluetoothConnection();
    return this;
  }
  
  // start a loop that continues to fetch and dispatch messages
  public void start() {
    while( this.source.hasNext() ) {
      log.info( source.getMessage() );
    }
  }
}
