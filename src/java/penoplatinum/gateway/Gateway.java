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

import penoplatinum.Config;

public class Gateway implements MessageReceiver {
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
    this.queue.setMessageReceiver(this);
    return this;
  }
  
  private Gateway sendToClient(String message, int channel) {
    this.connection.send(message, channel);
    return this;
  }
  
  public Gateway receive(String message) {
    this.sendToClient(message, Config.BT_MQ_RELAY);
    return this;
  }

  // start a loop that continues to fetch and dispatch messages
  public void start() {
    logger.info("Starting logging...");
    while(this.connection.hasNext()) {
      String msg = this.connection.getMessage();
      try {
        switch(this.connection.getType()) {
          case Config.BT_LOG     : logger.debug(msg); break;
          // TODO: use a appender to send messages to MQ after all
          //       then make a generic class to link a transporter to a logger
          case Config.BT_MQ_RELAY: this.queue.sendMessage(msg); break;
          case Config.BT_MODEL   : modelLogger.info(msg);  break;
          case Config.BT_WALLS   : wallsLogger.info(msg);  break;
          case Config.BT_VALUES  : valuesLogger.info(msg); break;
          case Config.BT_AGENTS  : agentsLogger.info(msg); break;
        }
      } catch (Exception e) {
        logger.error( "Failed to log message: " + msg);
      }
    }
  }
}
