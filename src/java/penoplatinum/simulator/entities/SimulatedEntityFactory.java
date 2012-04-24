package penoplatinum.simulator.entities;

import penoplatinum.robot.Robot;
import penoplatinum.simulator.sensors.IRSensor;
import penoplatinum.simulator.sensors.IRdistanceSensor;
import penoplatinum.simulator.sensors.LightSensor;
import penoplatinum.simulator.sensors.Motor;
import penoplatinum.simulator.sensors.MotorState;
import penoplatinum.simulator.sensors.NoneSensor;
import penoplatinum.simulator.sensors.Sonar;

public class SimulatedEntityFactory {

  private static final int MOTOR_LEFT = 0;
  private static final int MOTOR_RIGHT = 1;
  private static final int MOTOR_SONAR = 2;

  public static SimulatedEntity make(Robot robot) {
    SimulatedEntity entity = new SimulatedEntity(robot, SensorConfig.SENSORVALUES_NUM);
    Motor motorLeft = new Motor().setLabel("L");
    Motor motorRight = new Motor().setLabel("R");
    Motor motorSonar = new Motor().setLabel("S");

    entity.setupMotor(motorLeft, SimulatedEntityFactory.MOTOR_LEFT);
    entity.setupMotor(motorRight, SimulatedEntityFactory.MOTOR_RIGHT);
    entity.setupMotor(motorSonar, SimulatedEntityFactory.MOTOR_SONAR);

    entity.setSensor(SensorConfig.M1, motorLeft);
    entity.setSensor(SensorConfig.M2, motorRight);
    entity.setSensor(SensorConfig.M3, motorSonar);
    entity.setSensor(SensorConfig.MS1, new MotorState(motorLeft));
    entity.setSensor(SensorConfig.MS2, new MotorState(motorRight));
    entity.setSensor(SensorConfig.MS3, new MotorState(motorSonar));
    entity.setSensor(SensorConfig.S1, new IRSensor());
    entity.setSensor(SensorConfig.S2, new NoneSensor());
    entity.setSensor(SensorConfig.S3, new Sonar(motorSonar));
    entity.setSensor(SensorConfig.S4, new LightSensor());
    entity.setSensor(SensorConfig.IR0, new IRdistanceSensor(120));
    entity.setSensor(SensorConfig.IR1, new IRdistanceSensor(60));
    entity.setSensor(SensorConfig.IR2, new IRdistanceSensor(0));
    entity.setSensor(SensorConfig.IR3, new IRdistanceSensor(-60));
    entity.setSensor(SensorConfig.IR4, new IRdistanceSensor(-120));
    return entity;
  }
}
