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
      loadInternal(file);
      return;
    } catch (Exception ex) {
    }
    try {
      loadInternal("../../" + file);
      return;
    } catch (Exception ex) {
    }
  }

  private static void loadInternal(String file) throws RuntimeException {
    try {
      Properties p = new Properties();
      p.load(new FileInputStream(file));
      extractConfiguration(p);
    } catch (IOException e) {
      throw new RuntimeException("Could not load properties: " + e);
    }
  }

  private static void extractConfiguration(Properties props) {
    Config.DEBUGMODE = props.getProperty("debug").toLowerCase().equals("true");
    Config.ROBOT_NAME = props.getProperty("robot.name", "NXJ Platinum");
    Config.USE_LOCAL_MQ = props.getProperty("mq.type").toLowerCase().equals("local");
    Config.MQ_SERVER = props.getProperty("mq.server");
    Config.GHOST_CHANNEL = props.getProperty("mq.channel");
    Config.IMAGE_DIR = props.getProperty("dir.images");
  }
  public static int SIMULATOR_WAIT = 1;
  public static String IMAGE_DIR;
  public static boolean DEBUGMODE;
  public static String ROBOT_NAME;
  public static boolean USE_LOCAL_MQ;
  public static String MQ_SERVER;
  public static String GHOST_CHANNEL;
  public static String SECRET = "My name is Angie and I am a robot 1337.";
  // These are also important settings and aren't exposed through the 
  // properties file.
  public final static int MOTOR_SPEED_MOVE = 500;
  public final static int MOTOR_SPEED_SONAR = 250;
  // THESE ARE VERY IMPORTANT MAGIC NUMBERS ... DO NOT CHANGE THEM !!!
  // THESE ARE NOT READ FROM THE PROPERTIES FILE                   !!!
  public final static int BT_GHOST_PROTOCOL = 568348043;
  public final static int BT_LOG = 672631252;
  public final static int BT_START_LOG = 356356545;
  public final static int BT_MODEL = 123;
  public final static int BT_WALLS = 124;
  public final static int BT_VALUES = 125;
  public final static int BT_AGENTS = 126;
  // distance within which we need to detect a wall to consider it a wall
  public final static int WALL_DISTANCE = 35;
  // physical information
  public static final double WHEEL_SIZE = 17.5; // circumf. in cm
  public static final double WHEEL_BASE = 16.0; // wheeldist. in cm
  // states for the motor mapping to logica names
  public static final int MOTORSTATE_FORWARD = 1;
  public static final int MOTORSTATE_BACKWARD = 2;
  public static final int MOTORSTATE_STOPPED = 3;
  // we're tracking these sensors/actuators
  public static final int SENSORVALUES_NUM = 16;
  // names for sensor/actuator indexes
  public static final int M1 = 0;
  public static final int M2 = 1;
  public static final int M3 = 2;
  public static final int S1 = 3; // irSensor
  public static final int S2 = 4; //
  public static final int S3 = 5; // sonarsensor
  public static final int S4 = 6; // lightsensor
  public static final int MS1 = 7; // Motor state 1
  public static final int MS2 = 8; // Motor state 2
  public static final int MS3 = 9; // Motor state 3
  public static final int IR0 = 10;
  public static final int IR1 = 11;
  public static final int IR2 = 12;
  public static final int IR3 = 13;
  public static final int IR4 = 14;
  public static final int IR_DISTANCE = 15;
  // mapping logical names to physical indexes
  public static final int MOTOR_RIGHT = M1;
  public static final int MOTOR_LEFT = M2;
  public static final int MOTOR_SONAR = M3;
  public static final int MOTOR_STATE_RIGHT = MS1;
  public static final int MOTOR_STATE_LEFT = MS2;
  public static final int MOTOR_STATE_SONAR = MS3;
  public static final int IR_DIRECTION = S1;
  public static final int SONAR_DISTANCE = S3;
  public static final int SONAR_ANGLE = MOTOR_SONAR;
  public static final int LIGHT_SENSOR = S4;
}
