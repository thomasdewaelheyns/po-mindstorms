package penoplatinum;

import penoplatinum.sensor.LineFollowerFlorian;

public class Main {
    
    public static void main(String[] args) throws Exception {
        RobotRunner.Run(new Runnable() {
            
            public void run() {
                LineFollowerFlorian abc = new LineFollowerFlorian();
                abc.ActionLineFollower();
//                MuurVolgerTest.testPerpendicular();
                //RobotBluetoothTest test = new RobotBluetoothTest();
                //SonarTest test = new SonarTest();
                //test.testBluetoothLogging();

                //test.distanceTest(new UltrasonicSensor(SensorPort.S3));
                //SonarTest.testGetDistanceDuration(new UltrasonicSensor(SensorPort.S3));
                //test.calibrateSonar(new UltrasonicSensor(SensorPort.S3));

                //test.orientSonarHead(new UltrasonicSensor(SensorPort.S3), Motor.A);
            }
        });
        
    }
}
