package penoplatinum.gateway;

/**
 * Gateway
 * 
 * Connects to a Robot and dispatches all incoming information to log4j.
 *
 * Author: Team Platinum
 */

import java.security.*;

import org.apache.log4j.Logger;

import penoplatinum.Config;

public class Gateway implements MessageReceiver {
  static Logger logger = Logger.getLogger("Gateway");

  // setup some specific loggers
  static Logger modelLogger    = Logger.getLogger("model");
  static Logger wallsLogger    = Logger.getLogger("walls");
  static Logger valuesLogger   = Logger.getLogger("values");
  static Logger agentsLogger   = Logger.getLogger("agents");
  static Logger protocolLogger = Logger.getLogger("ghostprotocol");

  private Connection connection;
  private Queue  queue;
  
  private String secret = "";

  public Gateway useConnection(Connection connection) {
    this.connection = connection;
    return this;
  }

  public Gateway useQueue(Queue queue) {
    this.queue = queue;
    this.queue.subscribe(this);
    return this;
  }
  
  public Gateway useSecret(String secret) {
    this.secret = secret;
    return this;
  }
  
  private Gateway sendToClient(String message, int channel) {
    this.connection.send(message, channel);
    return this;
  }
  
  public void receive(String message) {
    if( this.isValid(message) ) {
      this.sendToClient(message, Config.BT_GHOST_PROTOCOL);
    }
  }
  
  private boolean isValid(String message) {
    String[] parts = message.split(" ");
    // only check the signature on our custom messages
    if( parts.length > 4 && "PLATINUM_CMD".equals(parts[1]) ) { 
      String signature = parts[2];
      String counter   = parts[3];
      String cmd       = parts[4];
      String expectedSignature = this.md5( this.secret + " " + counter + " " + cmd );
      if( signature.equals(expectedSignature) ) {
        return true;
      } else {
        System.out.println( "!!! received command with invalid signature : " + message );
        return false;
      }
    }
    return true;
  }

  private String md5(String message) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] digest = md.digest(message.getBytes("UTF-8"));
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < digest.length; i++) {
        sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
      }
      return sb.toString();
    } catch(Exception e) {}
    return "";
  }

  // start a loop that continues to fetch and dispatch messages
  public void start() {
    while(this.connection.hasNext()) {
      String msg = this.connection.getMessage();
      try {
        switch(this.connection.getType()) {
          case Config.BT_LOG     : logger.debug(msg); break;
          case Config.BT_GHOST_PROTOCOL: 
            this.queue.send(msg);     // send it to the queue (could go away)
            protocolLogger.info(msg.trim()); // and send a copy to the logging
            break;
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
