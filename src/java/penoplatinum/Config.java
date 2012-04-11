package penoplatinum;

/**
 * Config
 * 
 * Central configuration settings for all Java based implementations.
 * 
 * @author: Team Platinum
 */

import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;


public class Config {
  
  public static void load(String file) {
    try {
      Properties p = new Properties();
      p.load(new FileInputStream(file));
      extractConfiguration(p);
    } catch (IOException e) {
      throw new RuntimeException("Could not load properties: " + e );
    }
  }
  
  private static void extractConfiguration(Properties props) {
    Config.DEBUGMODE     = props.getProperty("debug").toLowerCase().equals("true");
    Config.ROBOT_NAME    = props.getProperty("robot.name");
    Config.USE_LOCAL_MQ  = props.getProperty("mq.type").toLowerCase().equals("local");
    Config.MQ_SERVER     = props.getProperty("mq.server");
    Config.GHOST_CHANNEL = props.getProperty("mq.channel");
  }

  public static boolean DEBUGMODE;
  public static String  ROBOT_NAME;
  public static boolean USE_LOCAL_MQ;
  public static String  MQ_SERVER;
  public static String  GHOST_CHANNEL;

  // These are also important settings and aren't exposed through the 
  // properties file.
  public final static int MOTOR_SPEED_MOVE  = 500;
  public final static int MOTOR_SPEED_SONAR = 250;

  // THESE ARE VERY IMPORTANT MAGIC NUMBERS ... DO NOT CHANGE THEM !!!
  // THESE ARE NOT READ FROM THE PROPERTIES FILE                   !!!
  public final static int BT_GHOST_PROTOCOL  = 568348043;
  public final static int BT_LOG             = 672631252;
  public final static int BT_START_LOG       = 356356545;

  public final static int BT_MODEL           = 123;
  public final static int BT_WALLS           = 124;
  public final static int BT_VALUES          = 125;
  public final static int BT_AGENTS          = 126;
}
