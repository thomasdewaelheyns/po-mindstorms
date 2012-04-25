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

  public static SimulatedEntity make(Robot robot) {
    SimulatedEntity entity = new SimulatedEntity(robot, RobotConfig.SENSORVALUES_NUM);
    Motor motorLeft = new Motor().setLabel("L");
    Motor motorRight = new Motor().setLabel("R");
    Motor motorSonar = new Motor().setLabel("S");

    entity.setupMotor(motorLeft, EntityConfig.MOTOR_LEFT);
    entity.setupMotor(motorRight, EntityConfig.MOTOR_RIGHT);
    entity.setupMotor(motorSonar, EntityConfig.MOTOR_SONAR);

    entity.setSensor(RobotConfig.M1, motorLeft);
    entity.setSensor(RobotConfig.M2, motorRight);
    entity.setSensor(RobotConfig.M3, motorSonar);
    entity.setSensor(RobotConfig.MS1, new MotorState(motorLeft));
    entity.setSensor(RobotConfig.MS2, new MotorState(motorRight));
    entity.setSensor(RobotConfig.MS3, new MotorState(motorSonar));
    entity.setSensor(RobotConfig.S1, new IRSensor());
    entity.setSensor(RobotConfig.S2, new NoneSensor());
    entity.setSensor(RobotConfig.S3, new Sonar(motorSonar));
    entity.setSensor(RobotConfig.S4, new LightSensor());
    entity.setSensor(RobotConfig.IR0, new IRdistanceSensor(120));
    entity.setSensor(RobotConfig.IR1, new IRdistanceSensor(60));
    entity.setSensor(RobotConfig.IR2, new IRdistanceSensor(0));
    entity.setSensor(RobotConfig.IR3, new IRdistanceSensor(-60));
    entity.setSensor(RobotConfig.IR4, new IRdistanceSensor(-120));
    return entity;
  }
}
