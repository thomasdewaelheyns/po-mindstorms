package penoplatinum;

import penoplatinum.demo.SensorDemo;
import penoplatinum.sensor.MuurVolgerTest;

public class Main {

    public static void main(String[] args) throws Exception {
        RobotRunner.Run(new Runnable() {

            public void run() {
                try {
                     
                    SensorDemo.main(null);
                    //BarcodeDemo.main(null);

                    /*RobotBluetoothConnection conn = new RobotBluetoothConnection();
                    conn.initializeConnection();
                    LineFollowerFlorian abc = new LineFollowerFlorian(conn);
                    abc.ActionLineFollower();*/
//                    MuurVolgerTest.testPerpendicular();
                    //RobotBluetoothTest test = new RobotBluetoothTest();
                    //SonarTest test = new SonarTest();
                    //test.testBluetoothLogging();
                    //test.distanceTest(new UltrasonicSensor(SensorPort.S3));
                    //SonarTest.testGetDistanceDuration(new UltrasonicSensor(SensorPort.S3));
                    //test.calibrateSonar(new UltrasonicSensor(SensorPort.S3));
                    //test.orientSonarHead(new UltrasonicSensor(SensorPort.S3), Motor.A);
                } catch (Exception ex) {
                    Utils.Log("AAAAAAAh!");
                }
            }
        });

    }
}
