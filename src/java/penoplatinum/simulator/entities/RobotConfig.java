package penoplatinum.simulator.entities;

public class RobotConfig {
  
  
  public static final double WHEEL_SIZE = EntityConfig.WHEEL_SIZE; // circumf. in cm
  public static final double WHEEL_BASE = EntityConfig.WHEEL_BASE; // wheeldist. in cm
  
  public static final int MOTORSTATE_FORWARD = 1;
  public static final int MOTORSTATE_BACKWARD = 2;
  public static final int MOTORSTATE_STOPPED = 3;
  public static final int M1 = EntityConfig.MOTOR_RIGHT; 
  public static final int M2 = EntityConfig.MOTOR_LEFT; 
  public static final int M3 = EntityConfig.MOTOR_SONAR; 
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
  public static final int SENSORVALUES_NUM = 15;
  
}
