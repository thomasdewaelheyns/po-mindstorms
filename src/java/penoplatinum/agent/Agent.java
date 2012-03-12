package penoplatinum.agent;

/**
 * Agent
 * 
 * Connects to a Robot and dispatches all incoming information to log4j.
 *
 * Author: Team Platinum
 */
import org.apache.log4j.Logger;
import penoplatinum.bluetooth.SimulatedConnection;

public class Agent {
  // setup some loggers
  static CustomLogger modelLogger  = new ModelLogger();    // 123
  static CustomLogger wallsLogger  = new WallsLogger();    // 124
  static CustomLogger valuesLogger = new ValuesLogger();   // 125
  static CustomLogger agentsLogger = new AgentsLogger();   // 126

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

  // connects to a Robot (by bluetooth name => currently ignored)
  public Agent connect(SimulatedConnection conn) {
    this.source = new BluetoothConnection(conn);
    this.mqDispatcher = new MQMessageDispatcher(conn);
    return this;
  }

  // start a loop that continues to fetch and dispatch messages
  public void start() {
    mqDispatcher.startMQDispatcher();
    System.out.println("Agent:> Starting logging...");
    while (this.source.hasNext()) {
      String msg = source.getMessage();
      try {
        // TODO: this switch should be handled using polymorphism ;-)
        switch (source.getType()) {
          case 123:
            modelLogger.log(msg);
            break;
          case 124:
            wallsLogger.log(msg);
            break;
          case 125:
            valuesLogger.log(msg);
            break;
          case 126:
            agentsLogger.log(msg);
            break;
        }
      } catch (Exception e) {
        System.err.println("Failed to log message: " + msg);
      }
    }
  }
}
