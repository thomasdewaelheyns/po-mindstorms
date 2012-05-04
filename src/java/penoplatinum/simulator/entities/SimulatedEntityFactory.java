package penoplatinum.simulator.entities;

import penoplatinum.Config;

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
    SimulatedEntity entity = new SimulatedEntity(robot, Config.SENSORVALUES_NUM);
    Motor motorLeft = new Motor().setLabel("L");
    Motor motorRight = new Motor().setLabel("R");
    Motor motorSonar = new Motor().setLabel("S");

    entity.setupMotor(motorLeft, Config.MOTOR_LEFT);
    entity.setupMotor(motorRight, Config.MOTOR_RIGHT);
    entity.setupMotor(motorSonar, Config.MOTOR_SONAR);

    entity.setSensor(Config.MOTOR_LEFT,  motorLeft);
    entity.setSensor(Config.MOTOR_RIGHT, motorRight);
    entity.setSensor(Config.MOTOR_SONAR, motorSonar);
    entity.setSensor(Config.MOTOR_STATE_LEFT,  new MotorState(motorLeft));
    entity.setSensor(Config.MOTOR_STATE_RIGHT, new MotorState(motorRight));
    entity.setSensor(Config.MOTOR_STATE_SONAR, new MotorState(motorSonar));
    entity.setSensor(Config.IR_DIRECTION, new IRSensor());
    entity.setSensor(Config.S2, new NoneSensor());
    entity.setSensor(Config.SONAR_DISTANCE, new Sonar(motorSonar));
    entity.setSensor(Config.LIGHT_SENSOR, new LightSensor());
    entity.setSensor(Config.IR0, new IRdistanceSensor(120));
    entity.setSensor(Config.IR1, new IRdistanceSensor(60));
    entity.setSensor(Config.IR2, new IRdistanceSensor(0));
    entity.setSensor(Config.IR3, new IRdistanceSensor(-60));
    entity.setSensor(Config.IR4, new IRdistanceSensor(-120));
    
    robot.useRobotAPI(new SimulationRobotAPI().setSimulatedEntity(entity));
    return entity;
  }
}
