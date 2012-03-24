package penoplatinum;

/**
 * Config
 * 
 * Central configuration settings for all Java based implementations.
 * TODO: move this to properties file ?!
 * TODO: make separate classes for debug and production and provide command-
 *       line switch to set configuration: properties file is therefore better
 * 
 * @author: Team Platinum
 */

public class Config {
  public final static boolean DEBUGMODE = true;

  // settings for connecting to a real MQ Server
  public final static String MQ_SERVER     = "localhost";
  // public final static String MQ_SERVER     = "leuven.cs.kotnet.kuleuven.be";
  public final static String GHOST_CHANNEL = "PLATINUM";
  // public final static String GHOST_CHANNEL = "Ghost";
  
  public final static boolean PROTOCOL_USE_RETARDEDNEWLINE = true;

  // a SimulatedGatewayClient can use a local MQ (when all ghosts run in the
  // same Simulator and use a Singleton SimulatedMQ object to exchange msgs.
  // when set to false, we connect to a real MQ_SERVER (see above)
  public final static boolean USE_LOCAL_MQ = true;

  public final static int MOTOR_SPEED_MOVE  = 500;
  public final static int MOTOR_SPEED_SONAR = 250;

  // THESE ARE VERY IMPORTANT MAGIC NUMBERS ... DON NOT CHANGE THEM !!!
  public final static int BT_GHOST_PROTOCOL  = 568348043;
  public final static int BT_LOG             = 672631252;
  public final static int BT_START_LOG       = 356356545;

  public final static int BT_MODEL           = 123;
  public final static int BT_WALLS           = 124;
  public final static int BT_VALUES          = 125;
  public final static int BT_AGENTS          = 126;
}
