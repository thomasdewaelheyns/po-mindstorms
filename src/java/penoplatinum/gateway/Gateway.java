package penoplatinum.gateway;

/**
 * Gateway
 * 
 * Connects to a Robot and dispatches all incoming information to log4j.
 *
 * Author: Team Platinum
 */

import org.apache.log4j.Logger;

import penoplatinum.bluetooth.SimulatedConnection;
import penoplatinum.util.Utils;

public class Gateway {
  static Logger logger = Logger.getLogger("Gateway");

  // setup some specific loggers
  static Logger modelLogger  = Logger.getLogger("model");  // 123
  static Logger wallsLogger  = Logger.getLogger("walls");  // 124
  static Logger valuesLogger = Logger.getLogger("values"); // 125
  static Logger agentsLogger = Logger.getLogger("agents"); // 126

  private Connection connection;
  private Queue  queue;

  public Gateway connect(Connection connection) {
    this.connection = connection;
    return this;
  }

  public Gateway useQueue(Queue queue) {
    this.queue = queue;
    return this;
  }

  // start a loop that continues to fetch and dispatch messages
  public void start() {
    logger.info("Starting logging...");
    while(this.connection.hasNext()) {
      String msg = this.connection.getMessage();
      try {
        switch(this.connection.getType()) {
          case Utils.PACKETID_LOG: logger.debug(msg); break;
          // TODO: now it makes sens to do this using a logger ;-)
          case GatewayConfig.MQRelayPacket: this.queue.sendMessage(msg); break;
          case 123: modelLogger.info(msg);  break;
          case 124: wallsLogger.info(msg);  break;
          case 125: valuesLogger.info(msg); break;
          case 126: agentsLogger.info(msg); break;
        }
      } catch (Exception e) {
        logger.error( "Failed to log message: " + msg);
      }
    }
  }
}
