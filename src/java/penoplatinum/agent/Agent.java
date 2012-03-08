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
  private MQ mq;
  private MQMessageDispatcher mqDispatcher;

  // connects to a Robot (by bluetooth name => currently ignored)
  public Agent connect(String name) {
    this.source = new BluetoothConnection();
    this.mqDispatcher = new MQMessageDispatcher(source.getConnection());
    return this;
  }

  // start a loop that continues to fetch and dispatch messages
  public void start() {
    
    mqDispatcher.startMQDispatcher();
    System.out.println("Agent:> Starting logging...");
    while (this.source.hasNextModelInfo()) {
      String msg = source.getMessage();
      try {
        if (msg.length() > 10) {
          log.info(msg);
        }
      } catch (Exception e) {
        System.err.println("failed to log message: " + msg);
      }
    }
    
  }
}
